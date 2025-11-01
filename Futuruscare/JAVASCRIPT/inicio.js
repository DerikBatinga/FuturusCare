// Array com os diferentes depoimentos para o carrossel
const testimonials = [
    {
        text: "A Futurus Care salvou minha viagem! Minha Luna ficou super bem cuidada e eu recebi fotos dela todos os dias. Recomendo de olhos fechados!",
        author: "- Maria Silva",
        meta: "Tutora da Luna"
    },
    {
        text: "Minha Mel é idosinha e precisa de medicação diária. Fiquei muito impressionada com o cuidado e atenção que ela recebeu. A comunicação foi perfeita e recebíamos fotos e vídeos durante todo o dia.",
        author: "- Roberto Fernandes",
        meta: "Tutor da Mel (Poodle)"
    },
    {
        text: "Primeira vez que deixo meu Simba com pet sitters e foi uma experiência incrível. Ele é muito medroso com estranhos, mas criou um vínculo incrível com a cuidadora e voltou para casa muito feliz.",
        author: "- Juliana Reis", 
        meta: "Tutora do Simba (Golden Retriever)"
    },
    {
        text: "Serviço de emergência salvou a vida da minha Bela! No meio da madrugada ela passou mal e a equipe foi super rápida em nos auxiliar e levar ela ao veterinário. Profissionalismo total em um momento de desespero.",
        author: "- Pedro Santos",
        meta: "Tutor da Bela (Labrador)"
    },
    {
        text: "Precisei viajar a trabalho por 10 dias e estava preocupada em deixar meus dois gatos. O serviço foi impecável! Recebi fotos e atualizações todos os dias, e eles voltaram muito bem cuidados e carinhosos.",
        author: "- Carlos Mendez",
        meta: "Tutor do Pecos e Amendoim (Gatos SRD)"
    },
    {
        text: "O Thor é um cão grande e enérgico, e eu estava com medo de como ele reagiria. Mas os profissionais da FuturusCare demonstraram total conhecimento sobre comportamento canino e ele ficou super calmo e feliz durante os passeios.",
        author: "- Amanda Costa",
        meta: "Tutora do Thor (Pastor Alemão)"
    }
];

// Variáveis globais para controle do carrossel de depoimentos
let currentIndex = 0;
const testimonialCard = document.getElementById('testimonialCard');
const testimonialText = document.getElementById('testimonialText');
const testimonialAuthor = document.getElementById('testimonialAuthor');
const testimonialMeta = document.getElementById('testimonialMeta');
const prevBtn = document.getElementById('prevBtn');
const nextBtn = document.getElementById('nextBtn');

/**
 * Atualiza o conteúdo do depoimento com animação de fade
 * @param {Object} testimonial - Objeto contendo os dados do depoimento
 */
function updateTestimonial(testimonial) {
    // Inicia animação de saída
    testimonialCard.classList.add('fade-out');
    
    // Aguarda a animação de saída completar antes de trocar o conteúdo
    setTimeout(() => {
        testimonialText.textContent = testimonial.text;
        testimonialAuthor.textContent = testimonial.author;
        testimonialMeta.textContent = testimonial.meta;
        
        // Remove animação de saída e aplica animação de entrada
        testimonialCard.classList.remove('fade-out');
        testimonialCard.classList.add('fade-in');
        
        // Remove a animação de entrada após completar
        setTimeout(() => {
            testimonialCard.classList.remove('fade-in');
        }, 100);
    }, 300);
}

/**
 * Atualiza o estado dos botões de navegação
 * Desabilita botões quando necessário
 */
function updateButtons() {
    prevBtn.disabled = currentIndex === 0;
    nextBtn.disabled = currentIndex === testimonials.length - 1;
}

/**
 * Avança para o próximo depoimento
 */
function nextTestimonial() {
    if (currentIndex < testimonials.length - 1) {
        currentIndex++;
        updateTestimonial(testimonials[currentIndex]);
        updateButtons();
    }
}

/**
 * Volta para o depoimento anterior
 */
function prevTestimonial() {
    if (currentIndex > 0) {
        currentIndex--;
        updateTestimonial(testimonials[currentIndex]);
        updateButtons();
    }
}

/**
 * Animação de contagem progressiva dos números das estatísticas
 */
function animateNumbers() {
    // Configuração das estatísticas a serem animadas
    const stats = [
        { element: document.querySelectorAll('.stat-number')[0], target: 500, suffix: '+' },
        { element: document.querySelectorAll('.stat-number')[1], target: 200, suffix: '+' },
        { element: document.querySelectorAll('.stat-number')[2], target: 5, suffix: '' },
        { element: document.querySelectorAll('.stat-number')[3], target: 4.9, suffix: '', decimal: true }
    ];

    // Anima cada estatística individualmente
    stats.forEach(stat => {
        let current = 0;
        const increment = stat.decimal ? stat.target / 100 : stat.target / 50;
        
        // Adiciona classe de animação
        stat.element.classList.add('counting');
        
        // Timer para incrementar o número gradualmente
        const timer = setInterval(() => {
            current += increment;
            
            // Para a animação quando atinge o valor alvo
            if (current >= stat.target) {
                current = stat.target;
                clearInterval(timer);
                stat.element.classList.remove('counting');
            }
            
            // Atualiza o texto com formatação apropriada
            if (stat.decimal) {
                stat.element.textContent = current.toFixed(1) + stat.suffix;
            } else {
                stat.element.textContent = Math.floor(current) + stat.suffix;
            }
        }, 40); // Intervalo de 40ms para animação suave
    });
}

/**
 * Inicialização quando o DOM estiver carregado
 */
document.addEventListener('DOMContentLoaded', function() {
    
    // ==================== MENU MOBILE ====================
    const mobileToggle = document.querySelector('.mobile-menu-toggle');
    const navLinks = document.querySelector('.nav-links');
    
    if (mobileToggle && navLinks) {
        // Abrir/fechar menu mobile
        mobileToggle.addEventListener('click', function() {
            navLinks.classList.toggle('mobile-active');
            
            // Trocar ícone do menu
            const icon = mobileToggle.querySelector('i');
            if (navLinks.classList.contains('mobile-active')) {
                icon.className = 'fas fa-times';
            } else {
                icon.className = 'fas fa-bars';
            }
            
            // Prevenir scroll quando menu estiver aberto
            document.body.style.overflow = navLinks.classList.contains('mobile-active') ? 'hidden' : '';
        });
        
        // Fechar menu ao clicar em um link
        navLinks.addEventListener('click', function(e) {
            if (e.target.tagName === 'A') {
                navLinks.classList.remove('mobile-active');
                mobileToggle.querySelector('i').className = 'fas fa-bars';
                document.body.style.overflow = '';
            }
        });
        
        // Fechar menu ao clicar fora dele
        document.addEventListener('click', function(e) {
            if (!navLinks.contains(e.target) && !mobileToggle.contains(e.target)) {
                navLinks.classList.remove('mobile-active');
                mobileToggle.querySelector('i').className = 'fas fa-bars';
                document.body.style.overflow = '';
            }
        });
    }
    
    // ===== CONFIGURAÇÃO DO OBSERVER PARA ANIMAÇÃO DAS ESTATÍSTICAS =====
    const statsSection = document.querySelector('.stats-section');
    let hasAnimated = false; // Flag para evitar múltiplas animações

    // Intersection Observer para detectar quando a seção fica visível
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            // Inicia animação quando 50% da seção estiver visível
            if (entry.isIntersecting && !hasAnimated) {
                animateNumbers();
                hasAnimated = true;
            }
        });
    }, {
        threshold: 0.5 // Ativa quando 50% do elemento estiver visível
    });

    // Inicia a observação da seção de estatísticas
    if (statsSection) {
        observer.observe(statsSection);
    }

    // ===== CONFIGURAÇÃO DOS EVENT LISTENERS =====
    
    // Botões de navegação dos depoimentos
    if (nextBtn && prevBtn) {
        nextBtn.addEventListener('click', nextTestimonial);
        prevBtn.addEventListener('click', prevTestimonial);
        
        // Inicializa o estado dos botões
        updateButtons();
    }

    // ===== FUNCIONALIDADES EXTRAS =====
    
    /**
     * Smooth scroll para links internos (se houver)
     */
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth'
                });
            }
        });
    });

    /**
     * Adiciona efeito de hover aos cards de serviços para dispositivos touch
     */
    const serviceCards = document.querySelectorAll('.service-card');
    serviceCards.forEach(card => {
        // Para dispositivos touch, adiciona toggle no tap
        card.addEventListener('touchstart', function() {
            // Remove active de outros cards
            serviceCards.forEach(otherCard => {
                if (otherCard !== card) {
                    otherCard.classList.remove('touch-active');
                }
            });
            // Toggle no card atual
            this.classList.toggle('touch-active');
        });
    });

    /**
     * Lazy loading para imagens (se implementado futuramente)
     */
    if ('IntersectionObserver' in window) {
        const imageObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    img.src = img.dataset.src;
                    img.classList.remove('lazy');
                    imageObserver.unobserve(img);
                }
            });
        });

        // Observa todas as imagens com classe 'lazy'
        document.querySelectorAll('img[data-src]').forEach(img => {
            imageObserver.observe(img);
        });
    }

    /**
     * Analytics tracking para cliques em botões importantes (placeholder)
     */
    document.querySelectorAll('.hero-btn, .business-btn').forEach(button => {
        button.addEventListener('click', function(e) {
            // Aqui você pode adicionar tracking de analytics
            console.log('Botão clicado:', this.textContent.trim());
            
            // Exemplo de Google Analytics (descomente quando configurar)
            // gtag('event', 'click', {
            //     'event_category': 'CTA',
            //     'event_label': this.textContent.trim()
            // });
        });
    });

    /**
     * Melhor experiência em dispositivos móveis
     */
    const isMobile = window.innerWidth <= 768;
    if (isMobile) {
        // Desabilita animações 3D em dispositivos móveis para melhor performance
        document.body.classList.add('mobile-device');
        
        // Adiciona classe para ajustes específicos de mobile
        serviceCards.forEach(card => {
            card.classList.add('mobile-card');
        });
    }

    /**
     * Otimização de performance - reduz animações se o usuário preferir
     */
    if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
        // Remove animações para usuários que preferem movimento reduzido
        document.body.classList.add('reduced-motion');
    }
});

/**
 * Função para redimensionamento da janela
 */
window.addEventListener('resize', function() {
    // Ajusta layout se necessário
    const isMobile = window.innerWidth <= 768;
    const serviceCards = document.querySelectorAll('.service-card');
    
    if (isMobile) {
        document.body.classList.add('mobile-device');
        serviceCards.forEach(card => {
            card.classList.add('mobile-card');
        });
    } else {
        document.body.classList.remove('mobile-device');
        serviceCards.forEach(card => {
            card.classList.remove('mobile-card', 'touch-active');
        });
    }
});

/**
 * Função para melhorar a acessibilidade
 */
document.addEventListener('keydown', function(e) {
    // Navegação por teclado no carrossel de depoimentos
    if (e.key === 'ArrowLeft' && !prevBtn.disabled) {
        prevTestimonial();
    } else if (e.key === 'ArrowRight' && !nextBtn.disabled) {
        nextTestimonial();
    }
});

// ===== EXPORTAÇÕES PARA USO GLOBAL (SE NECESSÁRIO) =====
window.FuturusCare = {
    nextTestimonial,
    prevTestimonial,
    animateNumbers
};