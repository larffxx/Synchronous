(() => {
    const list = document.getElementById('message-list');
    const emptyState = document.getElementById('empty-state');
    const errorState = document.getElementById('error-state');
    const loadMore = document.getElementById('load-more');
    const platformFilter = document.getElementById('platform-filter');
    const conversationFilter = document.getElementById('conversation-filter');
    const applyFilters = document.getElementById('apply-filters');
    const connectionStatus = document.getElementById('connection-status');
    const connectionLabel = document.getElementById('connection-label');
    const authPanel = document.getElementById('auth-panel');
    const accessToken = document.getElementById('access-token');
    const unlockChat = document.getElementById('unlock-chat');
    const mediaModal = document.getElementById('media-modal');
    const mediaModalImage = document.getElementById('media-modal-image');
    const mediaModalClose = document.getElementById('media-modal-close');
    let page = 0;
    let hasNext = true;
    let token = sessionStorage.getItem('synchronous_chat_access_token') || '';
    let streamConnection;

    function queryString() {
        const params = new URLSearchParams({ page: String(page), size: '50' });
        if (platformFilter.value !== 'ALL') params.set('platform', platformFilter.value);
        if (conversationFilter.value.trim()) params.set('conversation', conversationFilter.value.trim());
        return params.toString();
    }

    function setError(message) {
        errorState.textContent = message;
        errorState.hidden = !message;
    }

    function buildMessage(message) {
        const card = document.createElement('article');
        card.className = 'message-card';
        card.dataset.platform = message.platform;
        card.dataset.externalId = message.externalId;

        const meta = document.createElement('div');
        meta.className = 'message-meta';
        const platform = document.createElement('span');
        platform.className = 'platform-label';
        platform.textContent = message.platform;
        const conversation = document.createElement('span');
        conversation.textContent = message.conversationName;
        const sender = document.createElement('span');
        sender.textContent = `· ${message.senderName}`;
        const time = document.createElement('time');
        time.dateTime = message.sentAt;
        time.textContent = new Intl.DateTimeFormat(undefined, {
            dateStyle: 'medium', timeStyle: 'short'
        }).format(new Date(message.sentAt));
        meta.append(platform, conversation, sender, time);

        const content = document.createElement('p');
        content.className = 'message-content';
        content.textContent = message.content || '(attachment)';
        card.append(meta, content);

        if (message.attachments && message.attachments.length > 0) {
            const gallery = document.createElement('div');
            gallery.className = 'image-gallery';
            message.attachments.forEach((attachment) => {
                const image = document.createElement('img');
                image.className = 'message-image';
                image.src = attachment.url;
                image.alt = attachment.originalName || 'Message image';
                image.loading = 'lazy';
                image.addEventListener('click', () => openMedia(attachment.url, image.alt));
                gallery.append(image);
            });
            card.append(gallery);
        } else if (message.attachmentCount > 0) {
            const attachment = document.createElement('small');
            attachment.className = 'attachment-note';
            attachment.textContent = `${message.attachmentCount} attachment${message.attachmentCount === 1 ? '' : 's'}`;
            card.append(attachment);
        }
        return card;
    }

    function openMedia(url, alt) {
        mediaModalImage.src = url;
        mediaModalImage.alt = alt;
        mediaModal.hidden = false;
        mediaModalClose.focus();
    }

    function closeMedia() {
        mediaModal.hidden = true;
        mediaModalImage.removeAttribute('src');
    }

    function renderMessages(messages, append) {
        if (!append) list.replaceChildren();
        const existing = new Set([...list.children].map((node) => node.dataset.externalId));
        messages.forEach((message) => {
            if (!existing.has(message.externalId)) list.append(buildMessage(message));
        });
        const isEmpty = list.children.length === 0;
        emptyState.hidden = !isEmpty;
        list.setAttribute('aria-busy', 'false');
    }

    async function loadMessages(reset = false) {
        if (reset) {
            page = 0;
            hasNext = true;
            list.setAttribute('aria-busy', 'true');
            setError('');
        }
        if (!hasNext) return;
        loadMore.disabled = true;
        try {
            const headers = { Accept: 'application/json' };
            if (token) headers['X-Chat-Access-Token'] = token;
            const response = await fetch(`/api/messages?${queryString()}`, { headers, credentials: 'same-origin' });
            if (response.status === 401) {
                authPanel.hidden = false;
                throw new Error('Enter the chat access token to continue.');
            }
            if (!response.ok) throw new Error(`Unable to load messages (${response.status})`);
            const result = await response.json();
            renderMessages(result.content || [], page > 0);
            hasNext = !result.last;
            page += 1;
            loadMore.hidden = !hasNext;
        } catch (error) {
            setError(error.message);
            list.setAttribute('aria-busy', 'false');
        } finally {
            loadMore.disabled = false;
        }
    }

    function matchesCurrentFilter(message) {
        const platformMatches = platformFilter.value === 'ALL' || platformFilter.value === message.platform;
        const conversation = conversationFilter.value.trim().toLowerCase();
        const conversationMatches = !conversation
            || message.conversationId.toLowerCase().includes(conversation)
            || message.conversationName.toLowerCase().includes(conversation);
        return platformMatches && conversationMatches;
    }

    function connectStream() {
        if (streamConnection) streamConnection.close();
        const events = new EventSource('/api/messages/stream');
        streamConnection = events;
        events.addEventListener('open', () => {
            connectionStatus.dataset.state = 'connected';
            connectionLabel.textContent = 'Live';
        });
        events.addEventListener('message', (event) => {
            const message = JSON.parse(event.data);
            if (!matchesCurrentFilter(message)) return;
            const duplicate = [...list.children].some((node) => node.dataset.externalId === message.externalId);
            if (duplicate) return;
            list.prepend(buildMessage(message));
            emptyState.hidden = true;
        });
        events.addEventListener('error', () => {
            connectionStatus.dataset.state = 'offline';
            connectionLabel.textContent = 'Reconnecting';
        });
    }

    async function unlock() {
        const candidate = accessToken.value.trim();
        if (!candidate) return;
        const response = await fetch('/api/messages/session', {
            method: 'POST',
            headers: { 'X-Chat-Access-Token': candidate },
            credentials: 'same-origin'
        });
        if (!response.ok) {
            setError('The access token was rejected.');
            return;
        }
        token = candidate;
        sessionStorage.setItem('synchronous_chat_access_token', token);
        authPanel.hidden = true;
        setError('');
        await loadMessages(true);
        connectStream();
    }

    applyFilters.addEventListener('click', () => loadMessages(true));
    conversationFilter.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') loadMessages(true);
    });
    loadMore.addEventListener('click', () => loadMessages(false));
    unlockChat.addEventListener('click', unlock);
    accessToken.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') unlock();
    });
    mediaModalClose.addEventListener('click', closeMedia);
    mediaModal.addEventListener('click', (event) => {
        if (event.target === mediaModal) closeMedia();
    });
    document.addEventListener('keydown', (event) => {
        if (event.key === 'Escape' && !mediaModal.hidden) closeMedia();
    });
    loadMessages(true);
    connectStream();
})();
