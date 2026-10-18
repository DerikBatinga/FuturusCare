// Dados dos planos para mensal e anual
const plansData = {
    monthly: {
        essential: { price: 'R$ 80', period: 'Per month' },
        total: { price: 'R$ 350', period: 'per month' },
        complete: { price: 'R$ 180', period: 'Per month' }
    },
    annual: {
        essential: { price: 'R$ 60', period: 'Per month (ends after 12 months)' },
        total: { price: 'R$ 250', period: 'per month (ends after 12 months)' },
        complete: { price: 'R$ 140', period: 'Per month (ends after 12 months)' }
    }
};

// Elementos do DOM - CORRIGIDOS para português
const monthlyBtn = document.getElementById('botao-mensal');
const annualBtn = document.getElementById('botao-anual');
const essentialPrice = document.getElementById('preco-essencial');
const essentialPeriod = document.getElementById('periodo-essencial');
const totalPrice = document.getElementById('preco-total');
const totalPeriod = document.getElementById('periodo-total');
const completePrice = document.getElementById('preco-completo');
const completePeriod = document.getElementById('periodo-completo');

// Função para alternar entre planos mensais e anuais
function switchPlan(planType) {
    // Atualizar estado dos botões
    if (planType === 'monthly') {
        monthlyBtn.classList.add('active');
        annualBtn.classList.remove('active');
    } else {
        monthlyBtn.classList.remove('active');
        annualBtn.classList.add('active');
    }

    // Atualizar preços e períodos com animação
    const planData = plansData[planType];
    
    animatePriceChange(essentialPrice, planData.essential.price);
    animatePriceChange(totalPrice, planData.total.price);
    animatePriceChange(completePrice, planData.complete.price);
    
    essentialPeriod.textContent = planData.essential.period;
    totalPeriod.textContent = planData.total.period;
    completePeriod.textContent = planData.complete.period;
}

// Função para animar mudança de preços
function animatePriceChange(element, newPrice) {
    element.style.transform = 'scale(0.8)';
    element.style.opacity = '0.5';
    
    setTimeout(() => {
        element.textContent = newPrice;
        element.style.transform = 'scale(1)';
        element.style.opacity = '1';
    }, 150);
}

// Event listeners para os botões de toggle
monthlyBtn.addEventListener('click', () => switchPlan('monthly'));
annualBtn.addEventListener('click', () => switchPlan('annual'));

// Menu mobile toggle - CORRIGIDO para português
const mobileMenuToggle = document.querySelector('.menu-mobile-toggle');
const navLinks = document.querySelector('.links-navegacao');

mobileMenuToggle.addEventListener('click', () => {
    navLinks.classList.toggle('mobile-active');
    const icon = mobileMenuToggle.querySelector('i');
    
    if (navLinks.classList.contains('mobile-active')) {
        icon.className = 'fas fa-times';
    } else {
        icon.className = 'fas fa-bars';
    }
});

// Fechar menu mobile ao clicar em um link
document.querySelectorAll('.links-navegacao a').forEach(link => {
    link.addEventListener('click', () => {
        navLinks.classList.remove('mobile-active');
        mobileMenuToggle.querySelector('i').className = 'fas fa-bars';
    });
});

// Scroll suave para links âncora
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

// Animação de entrada dos cards - CORRIGIDO para português
const observerOptions = {
    threshold: 0.1,
    rootMargin: '0px 0px -50px 0px'
};

const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.style.opacity = '1';
            entry.target.style.transform = 'translateY(0)';
        }
    });
}, observerOptions);

// Observar todos os cards de planos - CORRIGIDO para português
document.querySelectorAll('.cartao-plano').forEach((card, index) => {
    card.style.opacity = '0';
    card.style.transform = 'translateY(30px)';
    card.style.transition = `all 0.6s ease ${index * 0.2}s`;
    observer.observe(card);
});

// Funcionalidade dos botões dos planos - CORRIGIDO para português
document.querySelectorAll('.botao-plano').forEach(button => {
    button.addEventListener('click', function(e) {
        e.preventDefault();
        
        const planCard = this.closest('.cartao-plano');
        const planName = planCard.querySelector('.nome-plano').textContent;
        const planPrice = planCard.querySelector('.preco-plano').textContent;
        
        // Efeito visual no botão
        this.style.transform = 'scale(0.95)';
        
        setTimeout(() => {
            this.style.transform = 'scale(1)';
            
            // Simular seleção do plano
            showPlanSelection(planName, planPrice);
        }, 150);
    });
});

// Função para mostrar seleção do plano
function showPlanSelection(planName, planPrice) {
    // Criar modal simples para demonstração
    const modal = document.createElement('div');
    modal.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.5);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 10000;
    `;
    
    const modalContent = document.createElement('div');
    modalContent.style.cssText = `
        background: white;
        padding: 40px;
        border-radius: 20px;
        text-align: center;
        max-width: 400px;
        width: 90%;
        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
    `;
    
    modalContent.innerHTML = `
        <div style="font-size: 48px; margin-bottom: 20px;">🎉</div>
        <h3 style="font-size: 24px; margin-bottom: 10px; color: #1a1a1a;">Excelente Escolha!</h3>
        <p style="margin-bottom: 20px; color: #6b7280;">Você selecionou o <strong>${planName}</strong> por <strong>${planPrice}</strong></p>
        <button id="closeModal" style="
            background: linear-gradient(135deg, #8b5cf6, #a855f7);
            color: white;
            border: none;
            padding: 12px 30px;
            border-radius: 10px;
            font-weight: 600;
            cursor: pointer;
            font-size: 16px;
        ">Continuar</button>
    `;
    
    modal.appendChild(modalContent);
    document.body.appendChild(modal);
    
    // Fechar modal
    document.getElementById('closeModal').addEventListener('click', () => {
        document.body.removeChild(modal);
    });
    
    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            document.body.removeChild(modal);
        }
    });
}

// Efeito parallax suave na seção hero - CORRIGIDO para português
window.addEventListener('scroll', () => {
    const scrolled = window.pageYOffset;
    const plansHero = document.querySelector('.hero-planos');
    
    if (plansHero) {
        plansHero.style.transform = `translateY(${scrolled * 0.3}px)`;
    }
});

// Adicionar efeitos hover aos cards - CORRIGIDO para português
document.querySelectorAll('.cartao-plano').forEach(card => {
    card.addEventListener('mouseenter', function() {
        this.style.transform = this.classList.contains('popular') 
            ? 'scale(1.05) translateY(-8px)' 
            : 'translateY(-8px)';
    });
    
    card.addEventListener('mouseleave', function() {
        this.style.transform = this.classList.contains('popular') 
            ? 'scale(1.05)' 
            : '';
    });
});

// Função para adicionar classe quando elemento entra na viewport
function addViewportClass(selector, className) {
    const elements = document.querySelectorAll(selector);
    
    const viewportObserver = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add(className);
            }
        });
    }, { threshold: 0.2 });
    
    elements.forEach(element => {
        viewportObserver.observe(element);
    });
}

// Aplicar animações aos elementos
document.addEventListener('DOMContentLoaded', () => {
    // Inicializar com plano mensal ativo
    switchPlan('monthly');
    
    // Adicionar estilos de animação via JavaScript
    const style = document.createElement('style');
    style.textContent = `
        .animate-in {
            animation: fadeInUp 0.8s ease forwards;
        }
        
        .preco-plano {
            transition: all 0.3s ease;
        }
        
        .botao-alternar.ativo {
            background: linear-gradient(135deg, #8b5cf6, #a855f7);
            color: white;
        }
    `;
    document.head.appendChild(style);

});
