document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("contactForm");
  const btnOrcamento = document.querySelector(".button-orcamento");

  function criarPopup(mensagem) {
    const overlay = document.createElement("div");
    overlay.classList.add("popup-overlay");

    const popup = document.createElement("div");
    popup.classList.add("popup-box");

    const texto = document.createElement("p");
    texto.textContent = mensagem;

    const botaoFechar = document.createElement("button");
    botaoFechar.textContent = "Fechar";
    botaoFechar.classList.add("popup-btn");

    botaoFechar.addEventListener("click", () => {
      overlay.remove();
    });

    popup.appendChild(texto);
    popup.appendChild(botaoFechar);
    overlay.appendChild(popup);
    document.body.appendChild(overlay);
  }

  form.addEventListener("submit", (e) => {
    e.preventDefault();

    const nome = document.getElementById("nome").value.trim();
    const email = document.getElementById("email").value.trim();
    const telefone = document.getElementById("telefone").value.trim();
    const comentario = document.getElementById("comentario").value.trim();

    if (!nome || !email || !telefone || !comentario) {
      criarPopup("Por favor, preencha todos os campos corretamente.");
      return;
    }

    criarPopup(`Obrigado pelo contato, ${nome}! Sua mensagem foi enviada com sucesso.`);

    form.reset();
  });

  btnOrcamento.addEventListener("click", () => {
    window.location.href = "../HTML/planos.html";
  });
});
