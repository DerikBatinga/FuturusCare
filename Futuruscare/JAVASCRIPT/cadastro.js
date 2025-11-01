const buttons = document.querySelectorAll('.continue-btn');

  buttons.forEach(button => {
    button.addEventListener('click', () => {
      window.location.href = 'cadastroprof.html'; // <-- Altere para o link desejado
    });
  });