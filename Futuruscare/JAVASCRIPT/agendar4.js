
// Sistema final de confirmação e redirecionamento

document.addEventListener('DOMContentLoaded', function() {
    console.log('Agendar4 carregado');
    carregarTodosDados();
    adicionarAnimacoes();
});

function carregarTodosDados() {
    // Carrega dados do serviço
    const servicoData = localStorage.getItem('servicoSelecionado');
    if (servicoData) {
        const servico = JSON.parse(servicoData);
        console.log('Carregando serviço:', servico);
        
        // Atualiza elementos do serviço
        atualizarElemento('.icone-servico', servico.icone);
        atualizarElemento('.titulo-servico', servico.nome);
        atualizarElemento('.descricao-servico', servico.descricao);
    }
    
    // Carrega dados do profissional
    const profissionalData = localStorage.getItem('profissionalSelecionado');
    if (profissionalData) {
        const profissional = JSON.parse(profissionalData);
        console.log('Carregando profissional:', profissional);
        
        // Atualiza elementos do profissional
        atualizarElemento('.avatar-profissional-resumo', profissional.inicial);
        atualizarElemento('.nome-profissional-resumo', profissional.nome);
        atualizarElemento('.especialidade-profissional', profissional.descricao); // Usando a descrição do profissional como especialidade
        atualizarElemento('.nota-avaliacao-resumo', profissional.avaliacao);
        atualizarElemento('.valor-servico', profissional.preco);
    }
    
    // Carrega dados do agendamento
    const agendamentoData = localStorage.getItem('agendamentoData');
    if (agendamentoData) {
        const agendamento = JSON.parse(agendamentoData);
        console.log('Carregando agendamento:', agendamento);
        
        // Atualiza elementos de data/horário
        const valoresAgendamento = document.querySelectorAll('.valor-agendamento');
        if (valoresAgendamento.length >= 2) {
            valoresAgendamento[0].textContent = agendamento.data;
            valoresAgendamento[1].textContent = agendamento.horario;
        }
    }
}

function atualizarElemento(seletor, valor) {
    const elemento = document.querySelector(seletor);
    if (elemento && valor) {
        elemento.textContent = valor;
    } else if (!elemento) {
        console.warn(`Elemento ${seletor} não encontrado`);
    }
}

function adicionarAnimacoes() {
    const cards = document.querySelectorAll('.card-servico, .card-profissional-resumo, .card-data-horario');
    cards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            card.style.transition = 'all 0.6s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, index * 200);
    });
}

function finalizarAgendamento() {
    // Recupera todos os dados para o resumo final
    const servicoData = JSON.parse(localStorage.getItem('servicoSelecionado') || '{}');
    const profissionalData = JSON.parse(localStorage.getItem('profissionalSelecionado') || '{}');
    const agendamentoData = JSON.parse(localStorage.getItem('agendamentoData') || '{}');
    
    const agendamentoCompleto = {
        servico: servicoData,
        profissional: profissionalData,
        agendamento: agendamentoData,
        total: servicoData.preco || 'R$ 70', // Usa o preço do serviço, ou um padrão
        finalizado: new Date().toISOString()
    };
    
    console.log('Agendamento completo:', agendamentoCompleto);
    
    // Salva dados completos para a página de pagamento (se houver)
    localStorage.setItem('dadosCompletos', JSON.stringify(agendamentoCompleto));
    
    // Exibe confirmação
    const mensagem = `Resumo do Agendamento:
    
Serviço: ${servicoData.nome || 'N/A'}
Profissional: ${profissionalData.nome || 'N/A'}
Data: ${agendamentoData.data || 'N/A'} às ${agendamentoData.horario || 'N/A'}
Valor: ${agendamentoCompleto.total}

Redirecionando para pagamento...`;
    
    alert(mensagem);
    
    // Redireciona para página de pagamento (descomente quando tiver a página)
    // window.location.href = 'pagamento.html';
    
    // Por enquanto, apenas loga os dados
    console.log('Dados prontos para pagamento:', agendamentoCompleto);
}

// Função auxiliar para debugging
function debug() {
    console.log('=== DEBUG AGENDAMENTO ===');
    console.log('Serviço:', localStorage.getItem('servicoSelecionado'));
    console.log('Profissional:', localStorage.getItem('profissionalSelecionado'));
    console.log('Agendamento:', localStorage.getItem('agendamentoData'));
    console.log('========================');
}

// Torna a função debug global para uso no console
window.debug = debug;