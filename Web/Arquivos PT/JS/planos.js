document.addEventListener('DOMContentLoaded', () => {
  const botaoMensal = document.getElementById('botao-mensal');
  const botaoAnual = document.getElementById('botao-anual');

  const precos = {
    essencial: document.getElementById('preco-essencial'),
    completo: document.getElementById('preco-completo'),
    total: document.getElementById('preco-total'),
    periodoEssencial: document.getElementById('periodo-essencial'),
    periodoCompleto: document.getElementById('periodo-completo'),
    periodoTotal: document.getElementById('periodo-total'),
  };

  function alternarPlano(tipo) {
    if (tipo === 'mensal') {
      botaoMensal.classList.add('ativo');
      botaoAnual.classList.remove('ativo');

      precos.essencial.textContent = 'R$ 80';
      precos.completo.textContent = 'R$ 180';
      precos.total.textContent = 'R$ 350';

      precos.periodoEssencial.textContent = 'Por mês';
      precos.periodoCompleto.textContent = 'Por mês';
      precos.periodoTotal.textContent = 'Por mês';
    } else {
      botaoAnual.classList.add('ativo');
      botaoMensal.classList.remove('ativo');

      precos.essencial.textContent = 'R$ 800';
      precos.completo.textContent = 'R$ 1800';
      precos.total.textContent = 'R$ 3500';

      precos.periodoEssencial.textContent = 'Por ano';
      precos.periodoCompleto.textContent = 'Por ano';
      precos.periodoTotal.textContent = 'Por ano';
    }
  }

  botaoMensal.addEventListener('click', () => alternarPlano('mensal'));
  botaoAnual.addEventListener('click', () => alternarPlano('anual'));
});