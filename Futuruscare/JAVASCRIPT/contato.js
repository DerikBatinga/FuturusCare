// Form submission handler
document.getElementById('contactForm').addEventListener('submit', function(e) {
    e.preventDefault();  // Impede a submissão padrão do formulário
    
    // Get form data
    const formData = new FormData(this);
    const data = Object.fromEntries(formData);
    
    // Simple validation
    if (!data.nome || !data.email || !data.telefone || !data.comentario) {
        alert('Por favor, preencha todos os campos obrigatórios.');
        return;
    }
    
    // Simulate form submission
    const submitBtn = document.querySelector('.submit-btn');
    const originalText = submitBtn.textContent;
    
    submitBtn.textContent = 'Enviando...';
    submitBtn.disabled = true;
    
    setTimeout(() => {
        alert('Formulário enviado com sucesso! Entraremos em contato em breve.');
        this.reset();  // Limpa os campos do formulário
        submitBtn.textContent = originalText;
        submitBtn.disabled = false;
    }, 2000);
});

// Phone number formatting without forcing '+55'
document.getElementById('telefone').addEventListener('input', function(e) {
    let value = e.target.value.replace(/\D/g, '');  // Remove todos os caracteres não numéricos
    
    // Formatação para números brasileiros (considerando 11 dígitos no total)
    if (value.length >= 11) {
        value = value.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
    } else if (value.length >= 7) {
        value = value.replace(/(\d{2})(\d{4})(\d{0,4})/, '($1) $2-$3');
    } else if (value.length >= 3) {
        value = value.replace(/(\d{2})(\d{0,5})/, '($1) $2');
    }
    
    e.target.value = value;  // Atualiza o valor do campo de telefone com a formatação
});

// Smooth animations for form elements
const formInputs = document.querySelectorAll('input, textarea');
formInputs.forEach(input => {
    input.addEventListener('focus', function() {
        this.style.transform = 'scale(1.02)';
        this.style.transition = 'all 0.3s ease';
    });
    
    input.addEventListener('blur', function() {
        this.style.transform = 'scale(1)';
    });
});

// CTA button functionality (scrolling to the form)
document.querySelector('.cta-btn').addEventListener('click', function() {
    document.querySelector('#nome').focus();  // Foca no campo 'nome'
    document.querySelector('.form-container').scrollIntoView({ behavior: 'smooth' });
});
