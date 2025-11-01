        // Funcionalidade dos botões de agendar
        document.querySelectorAll('.botao-agendar').forEach(button => {
            button.addEventListener('click', function(e) {
                e.preventDefault();
                
                // Pegar o nome do profissional
                const profissionalCard = this.closest('.card-profissional');
                const nomeProfissional = profissionalCard.querySelector('.nome-profissional').textContent;
                
                // Redirecionar para agendar3.html
                window.location.href = 'agendar3.html';
            });
        });