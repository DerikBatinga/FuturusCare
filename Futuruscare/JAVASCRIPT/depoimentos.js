// Array com os diferentes depoimentos
const testimonials = [
    {
        text: "Minha Mel é idosinha e precisa de medicação diária. Fiquei muito impressionada com o cuidado e atenção que ela recebeu. A comunicação foi perfeita e recebíamos fotos e vídeos durante todo o dia.",
        author: "Roberto Fernandes",
        meta: "Mel (Poodle)<br>Cuidados Especiais"
    },
    {
        text: "Primeira vez que deixo meu Simba com pet sitters e foi uma experiência incrível. Ele é muito medroso com estranhos, mas criou um vínculo incrível com a cuidadora e voltou para casa muito feliz.",
        author: "Juliana Reis", 
        meta: "Simba (Golden Retriever)<br>Hospedagem em Casa"
    },
    {
        text: "A FuturusCare salvou minha viagem de trabalho! A Luna ficou super bem cuidada e eu recebi fotos todos os dias. A Ana Clara é um amor de pessoa e tratou minha cachorra como se fosse filha dela.",
        author: "Maria Silva",
        meta: "Luna (Golden Retriever)<br>Hospedagem em Casa"
    },
    {
        text: "Serviço de emergência salvou a vida da minha Bela! No meio da madrugada ela passou mal e a equipe foi super rápida em nos auxiliar e levar ela ao veterinário. Profissionalismo total em um momento de desespero.",
        author: "Pedro Santos",
        meta: "Bela (Labrador)<br>Emergência"
    },
    {
        text: "Precisei viajar a trabalho por 10 dias e estava preocupada em deixar meus dois gatos. O serviço foi impecável! Recebi fotos e atualizações todos os dias, e eles voltaram muito bem cuidados e carinhosos.",
        author: "Carlos Mendez",
        meta: "Pecos e Amendoim (Gatos SRD)<br>Pet Sitting Diário"
    },
    {
        text: "O Thor é um cão grande e enérgico, e eu estava com medo de como ele reagiria. Mas os profissionais da FuturusCare demonstraram total conhecimento sobre comportamento canino e ele ficou super calmo e feliz durante os passeios.",
        author: "Amanda Costa",
        meta: "Thor (Pastor Alemão)<br>Passeios Diários"
    }
];

let currentIndex = 0;
const testimonialsRow = document.querySelector('.testimonials-row');
const prevBtn = document.getElementById('prevBtn');
const nextBtn = document.getElementById('nextBtn');

function updateCard(cardElement, testimonial) {
    cardElement.classList.add('fade-out');
    
    setTimeout(() => {
        cardElement.querySelector('.testimonial-text').textContent = testimonial.text;
        cardElement.querySelector('.testimonial-author').textContent = testimonial.author;
        cardElement.querySelector('.testimonial-meta').innerHTML = testimonial.meta;
        
        cardElement.classList.remove('fade-out');
        cardElement.classList.add('fade-in');
        
        setTimeout(() => {
            cardElement.classList.remove('fade-in');
        }, 100);
    }, 300);
}

function updateButtons() {
    prevBtn.disabled = currentIndex === 0;
    nextBtn.disabled = currentIndex >= testimonials.length - 2;
}

function nextTestimonial() {
    if (currentIndex < testimonials.length - 2) {
        currentIndex += 2;
        const cards = testimonialsRow.querySelectorAll('.testimonial-card');
        
        updateCard(cards[0], testimonials[currentIndex]);
        if (testimonials[currentIndex + 1]) {
            updateCard(cards[1], testimonials[currentIndex + 1]);
        }
        
        updateButtons();
    }
}

function prevTestimonial() {
    if (currentIndex > 0) {
        currentIndex -= 2;
        const cards = testimonialsRow.querySelectorAll('.testimonial-card');
        
        updateCard(cards[0], testimonials[currentIndex]);
        if (testimonials[currentIndex + 1]) {
            updateCard(cards[1], testimonials[currentIndex + 1]);
        }
        
        updateButtons();
    }
}

// Animação de contagem dos números
function animateNumbers() {
    const stats = [
        { element: document.querySelectorAll('.stat-number')[0], target: 500, suffix: '+' },
        { element: document.querySelectorAll('.stat-number')[1], target: 200, suffix: '+' },
        { element: document.querySelectorAll('.stat-number')[2], target: 98, suffix: '%' },
        { element: document.querySelectorAll('.stat-number')[3], target: 4.9, suffix: '', decimal: true }
    ];

    stats.forEach(stat => {
        let current = 0;
        const increment = stat.decimal ? stat.target / 100 : stat.target / 50;
        stat.element.classList.add('counting');
        
        const timer = setInterval(() => {
            current += increment;
            
            if (current >= stat.target) {
                current = stat.target;
                clearInterval(timer);
                stat.element.classList.remove('counting');
            }
            
            if (stat.decimal) {
                stat.element.textContent = current.toFixed(1) + stat.suffix;
            } else {
                stat.element.textContent = Math.floor(current) + stat.suffix;
            }
        }, 40);
    });
}

// Observer para detectar quando a seção de estatísticas fica visível
const statsSection = document.querySelector('.stats-section');
let hasAnimated = false;

const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting && !hasAnimated) {
            animateNumbers();
            hasAnimated = true;
        }
    });
}, {
    threshold: 0.5
});

observer.observe(statsSection);

// Event listeners para os depoimentos
nextBtn.addEventListener('click', nextTestimonial);
prevBtn.addEventListener('click', prevTestimonial);

// Inicializar estado dos botões
updateButtons();