document.addEventListener("DOMContentLoaded", () => {
  const botoes = document.querySelectorAll(".continue-btn");
  const voltar = document.querySelector(".back-arrow");

  const rotas = [
    "../HTML/cadastroprof.html",
    "../HTML/cadastroprof.html",
    "../HTML/cadastroprof.html"
  ];

  botoes.forEach((btn, index) => {
    btn.addEventListener("click", () => {
      window.location.href = rotas[index];
    });
  });

  if (voltar) {
    voltar.addEventListener("click", (e) => {
      e.preventDefault();
      window.history.back();
    });
  }
});