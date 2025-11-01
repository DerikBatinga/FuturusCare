document.addEventListener('DOMContentLoaded', function() {
    
    // Smooth scrolling para links do menu
    initSmoothScrolling();
    
    // Efeitos de hover nos botões
    initButtonEffects();
    
    // Animações de entrada dos elementos
    initScrollAnimations();
    
    // Funcionalidades dos botões
    initButtonFunctionalities();
    
    // Menu mobile (se necessário)
    initMobileMenu();
});

/**
 * Inicializar scroll suave para links âncora
 */
function initSmoothScrolling() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
}

/**
 * Inicializar efeitos de hover nos botões
 */
function initButtonEffects() {
    const buttons = document.querySelectorAll('button, .btn-cadastrar, .btn-entrar, .btn-seguir, .btn-message');
    
    buttons.forEach(button => {
        // Efeito de hover
        button.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-2px)';
            this.style.transition = 'all 0.3s ease';
        });
        
        button.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
        
        // Efeito de clique
        button.addEventListener('mousedown', function() {
            this.style.transform = 'translateY(0) scale(0.98)';
        });
        
        button.addEventListener('mouseup', function() {
            this.style.transform = 'translateY(-2px) scale(1)';
        });
    });
}

/**
 * Inicializar animações ao fazer scroll
 */
function initScrollAnimations() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
                entry.target.classList.add('animate-fade-in');
            }
        });
    }, observerOptions);

    // Aplicar animação aos elementos principais
    const animatedElements = document.querySelectorAll('.pet-card, .profile-card, .footer-section');
    animatedElements.forEach(el => {
        el.style.opacity = '0';
        el.style.transform = 'translateY(20px)';
        el.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        observer.observe(el);
    });
}

/**
 * Funcionalidades dos botões principais
 */
function initButtonFunctionalities() {
    // Botão Seguir
    const btnSeguir = document.querySelector('.btn-seguir');
    if (btnSeguir) {
        btnSeguir.addEventListener('click', function() {
            if (this.textContent === 'Seguir') {
                this.textContent = 'Seguindo';
                this.style.background = '#10b981';
                showNotification('Você está seguindo João Manuel Augusto!');
            } else {
                this.textContent = 'Seguir';
                this.style.background = '#8b5cf6';
                showNotification('Você deixou de seguir.');
            }
        });
    }
    
    // Botão Mandar Mensagem
    const btnMessage = document.querySelector('.btn-message');
    if (btnMessage) {
        btnMessage.addEventListener('click', function() {
            showNotification('Abrindo chat...');
            // Aqui você pode abrir um modal de mensagem ou redirecionar
        });
    }
    
    // Botões de autenticação
    const btnCadastrar = document.querySelector('.btn-cadastrar');
    const btnEntrar = document.querySelector('.btn-entrar');
    
    if (btnCadastrar) {
        btnCadastrar.addEventListener('click', function(e) {
            e.preventDefault();
            showNotification('Redirecionando para cadastro...');
            // Aqui você pode abrir modal ou redirecionar
        });
    }
    
    if (btnEntrar) {
        btnEntrar.addEventListener('click', function(e) {
            e.preventDefault();
            showNotification('Redirecionando para login...');
            // Aqui você pode abrir modal ou redirecionar
        });
    }
}

/**
 * Menu mobile responsivo
 */
function initMobileMenu() {
    // Criar botão hamburger para mobile se não existir
    const navContainer = document.querySelector('.nav-container');
    const navMenu = document.querySelector('.nav-menu');
    
    if (navContainer && navMenu && window.innerWidth <= 768) {
        const hamburger = document.createElement('button');
        hamburger.innerHTML = '☰';
        hamburger.classList.add('hamburger-menu');
        hamburger.style.cssText = `
            display: none;
            background: none;
            border: none;
            color: white;
            font-size: 1.5rem;
            cursor: pointer;
        `;
        
        // Adicionar hamburger em mobile
        navContainer.insertBefore(hamburger, navMenu);
        
        hamburger.addEventListener('click', function() {
            navMenu.style.display = navMenu.style.display === 'flex' ? 'none' : 'flex';
            navMenu.style.position = 'absolute';
            navMenu.style.top = '100%';
            navMenu.style.left = '0';
            navMenu.style.right = '0';
            navMenu.style.background = 'linear-gradient(135deg, #8b5cf6, #a855f7)';
            navMenu.style.flexDirection = 'column';
            navMenu.style.padding = '1rem';
            navMenu.style.borderRadius = '0 0 10px 10px';
        });
    }
}

/**
 * Sistema de notificações
 */
function showNotification(message, type = 'info') {
    // Remover notificação existente
    const existingNotification = document.querySelector('.notification');
    if (existingNotification) {
        existingNotification.remove();
    }
    
    // Criar nova notificação
    const notification = document.createElement('div');
    notification.classList.add('notification');
    notification.textContent = message;
    
    // Estilos da notificação
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: ${type === 'success' ? '#10b981' : '#8b5cf6'};
        color: white;
        padding: 1rem 1.5rem;
        border-radius: 10px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.3);
        z-index: 10000;
        font-weight: 500;
        transform: translateX(100%);
        transition: transform 0.3s ease;
    `;
    
    document.body.appendChild(notification);
    
    // Animação de entrada
    setTimeout(() => {
        notification.style.transform = 'translateX(0)';
    }, 10);