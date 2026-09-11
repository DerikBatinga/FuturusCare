
// Funcionalidade completa do calendário e seleção de horários

class Calendar {
    constructor() {
        // Configurações iniciais do calendário
        this.currentDate = new Date();
        this.selectedDate = null;
        this.selectedTime = null;
        
        // Nomes dos meses 
        this.months = [
            'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
            'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'
        ];
        
        console.log('Calendar construtor chamado');
        this.init();
    }
    
    // Método principal de inicialização
    init() {
        console.log('Calendar init chamado');
        
        // Aguarda o DOM estar completamente carregado
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', () => {
                console.log('DOM carregado, iniciando calendar');
                this.carregarDadosAnteriores();
                this.render();
                this.addEventListeners();
            });
        } else {
            // DOM já está carregado
            console.log('DOM já carregado, iniciando calendar imediatamente');
            this.carregarDadosAnteriores();
            this.render();
            this.addEventListeners();
        }
    }
    
    // Carrega dados das etapas anteriores
    carregarDadosAnteriores() {
        const servicoData = localStorage.getItem('servicoSelecionado');
        const profissionalData = localStorage.getItem('profissionalSelecionado');
        
        if (servicoData) {
            const servico = JSON.parse(servicoData);
            console.log('Serviço carregado na página 3:', servico);
            
            // Atualiza título se necessário
            const titulo = document.querySelector('.titulo-agendar');
            if (titulo) {
                titulo.innerHTML = `<span class="highlight">Data e Horário</span> para ${servico.nome}`;
            }
        }
        
        if (profissionalData) {
            const profissional = JSON.parse(profissionalData);
            console.log('Profissional carregado na página 3:', profissional);
        }
    }
    
    // Adiciona todos os event listeners necessários
    addEventListeners() {
        console.log('Adicionando event listeners');
        
        // Event listener para botão "mês anterior"
        const prevButton = document.getElementById('prevMonth');
        if (prevButton) {
            prevButton.addEventListener('click', () => {
                console.log('Botão mês anterior clicado');
                this.currentDate.setMonth(this.currentDate.getMonth() - 1);
                this.render();
            });
        } else {
            console.error('Botão prevMonth não encontrado');
        }
        
        // Event listener para botão "próximo mês"
        const nextButton = document.getElementById('nextMonth');
        if (nextButton) {
            nextButton.addEventListener('click', () => {
                console.log('Botão próximo mês clicado');
                this.currentDate.setMonth(this.currentDate.getMonth() + 1);
                this.render();
            });
        } else {
            console.error('Botão nextMonth não encontrado');
        }
        
        // Event listeners para seleção de horários
        const timeSlots = document.querySelectorAll('.time-slot');
        console.log('Time slots encontrados:', timeSlots.length);
        
        timeSlots.forEach(slot => {
            slot.addEventListener('click', (e) => {
                console.log('Time slot clicado');
                // Remove a classe 'selected' de todos os horários
                timeSlots.forEach(s => s.classList.remove('selected'));
                
                // Adiciona a classe 'selected' ao horário clicado
                e.currentTarget.classList.add('selected');
                
                // Salva o horário selecionado
                this.selectedTime = e.currentTarget.getAttribute('data-time');
                
                // Atualiza o estado do botão de continuar
                this.updateSubmitButton();
                
                console.log('Horário selecionado:', this.selectedTime);
            });
        });
        
        // Event listener para o botão "Continuar"
        const submitBtn = document.getElementById('submitBtn');
        if (submitBtn) {
            console.log('Botão submitBtn encontrado, adicionando listener');
            submitBtn.addEventListener('click', (e) => {
                console.log('Botão Continuar clicado');
                console.log('Data selecionada:', this.selectedDate);
                console.log('Horário selecionado:', this.selectedTime);
                
                // Verifica se tanto data quanto horário foram selecionados
                if (this.selectedDate && this.selectedTime) {
                    // Salva dados de data e horário
                    const agendamentoData = {
                        data: this.selectedDate.toLocaleDateString('pt-BR'),
                        horario: this.selectedTime,
                        dataCompleta: this.selectedDate.toISOString(),
                        timestamp: new Date().getTime()
                    };
                    
                    localStorage.setItem('agendamentoData', JSON.stringify(agendamentoData));
                    
                    console.log('Dados do agendamento salvos:', agendamentoData);
                    
                    // Redireciona diretamente para a página final (sem alerta ou delay)
                    window.location.href = 'agendar4.html';
                    
                } else {
                    // Alerta caso dados estejam faltando
                    alert('Por favor, selecione uma data e um horário antes de continuar.');
                    console.log('Dados faltando - Data:', this.selectedDate, 'Horário:', this.selectedTime);
                }
            });
        } else {
            console.error('Botão submitBtn não encontrado');
        }
    }
    
    // Renderiza o calendário completo
    render() {
        console.log('Renderizando calendário');
        
        // Atualiza o título do mês/ano no cabeçalho
        const monthYearElement = document.getElementById('monthYear');
        if (monthYearElement) {
            monthYearElement.textContent = `${this.months[this.currentDate.getMonth()]} ${this.currentDate.getFullYear()}`;
        } else {
            console.error('Elemento monthYear não encontrado');
        }
        
        // Obtém o container do calendário
        const calendarGrid = document.getElementById('calendarGrid');
        if (!calendarGrid) {
            console.error('Elemento calendarGrid não encontrado');
            return;
        }
        
        // Remove todos os dias existentes (mantém os cabeçalhos)
        const existingDays = calendarGrid.querySelectorAll('.calendar-day');
        existingDays.forEach(day => day.remove());
        
        // Calcula as datas necessárias para renderizar o mês
        const year = this.currentDate.getFullYear();
        const month = this.currentDate.getMonth();
        
        // Primeiro dia do mês
        const firstDay = new Date(year, month, 1);
        
        // Data de início (pode ser do mês anterior para preencher a primeira semana)
        const startDate = new Date(firstDay);
        startDate.setDate(firstDay.getDate() - firstDay.getDay());
        
        // Data atual para comparação (sem horas)
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        
        // Gera 42 dias (6 semanas x 7 dias) para preencher o calendário
        for (let i = 0; i < 42; i++) {
            const date = new Date(startDate);
            date.setDate(startDate.getDate() + i);
            
            // Cria elemento do dia
            const dayElement = document.createElement('div');
            dayElement.className = 'calendar-day';
            dayElement.textContent = date.getDate();
            
            // Marca dias que pertencem a outros meses
            if (date.getMonth() !== month) {
                dayElement.classList.add('other-month');
            } else {
                // Para dias do mês atual
                
                // Verifica se é um dia passado
                if (date < today) {
                    dayElement.classList.add('disabled');
                } else {
                    // Dia válido - adiciona funcionalidade de clique
                    dayElement.addEventListener('click', () => {
                        console.log('Dia clicado:', date.toLocaleDateString('pt-BR'));
                        
                        // Remove seleção anterior
                        const selectedDays = calendarGrid.querySelectorAll('.calendar-day.selected');
                        selectedDays.forEach(d => d.classList.remove('selected'));
                        
                        // Marca o dia atual como selecionado
                        dayElement.classList.add('selected');
                        
                        // Salva a data selecionada
                        this.selectedDate = new Date(date);
                        
                        // Atualiza o estado do botão
                        this.updateSubmitButton();
                        
                        console.log('Data selecionada:', this.selectedDate.toLocaleDateString('pt-BR'));
                    });
                    
                    // Adiciona hover effect
                    dayElement.style.cursor = 'pointer';
                }
            }
            
            // Adiciona o dia ao calendário
            calendarGrid.appendChild(dayElement);
        }
        
        console.log('Calendário renderizado com sucesso');
    }
    
    // Atualiza o estado do botão "Continuar" baseado nas seleções
    updateSubmitButton() {
        const submitBtn = document.getElementById('submitBtn');
        if (!submitBtn) {
            console.error('Botão submitBtn não encontrado para atualização');
            return;
        }
        
        console.log('Atualizando botão - Data:', this.selectedDate, 'Horário:', this.selectedTime);
        
        // Verifica se ambos data e horário foram selecionados
        if (this.selectedDate && this.selectedTime) {
            // Habilita o botão
            submitBtn.disabled = false;
            submitBtn.textContent = 'Continuar';
            submitBtn.style.opacity = '1';
            submitBtn.style.cursor = 'pointer';
            submitBtn.style.backgroundColor = '#8b5cf6';
            console.log('Botão habilitado');
        } else {
            // Desabilita o botão
            submitBtn.disabled = true;
            submitBtn.textContent = 'Selecione data e horário';
            submitBtn.style.opacity = '0.6';
            submitBtn.style.cursor = 'not-allowed';
            submitBtn.style.backgroundColor = '#cbd5e1';
            console.log('Botão desabilitado');
        }
    }
    
    // Método para resetar seleções (útil para debugging)
    reset() {
        this.selectedDate = null;
        this.selectedTime = null;
        
        // Remove seleções visuais
        document.querySelectorAll('.calendar-day.selected').forEach(day => {
            day.classList.remove('selected');
        });
        
        document.querySelectorAll('.time-slot.selected').forEach(slot => {
            slot.classList.remove('selected');
        });
        
        // Atualiza botão
        this.updateSubmitButton();
        
        console.log('Seleções resetadas');
    }
    
    // Método para obter dados selecionados (útil para debugging)
    getSelectedData() {
        return {
            date: this.selectedDate,
            time: this.selectedTime,
            dateFormatted: this.selectedDate ? this.selectedDate.toLocaleDateString('pt-BR') : null
        };
    }
}

// Variável global para acesso ao calendário
let calendarInstance;

// Inicializa o calendário quando o DOM estiver pronto
document.addEventListener('DOMContentLoaded', () => {
    console.log('DOM Content Loaded - Inicializando calendário');
    try {
        calendarInstance = new Calendar();
        console.log('Calendário inicializado com sucesso');
        
        // Torna as funções de debug disponíveis globalmente
        window.calendarInstance = calendarInstance;
        window.debugCalendar = function() {
            if (calendarInstance) {
                console.log('Estado atual do calendário:', calendarInstance.getSelectedData());
            } else {
                console.log('Calendário não inicializado');
            }
        };
        window.resetCalendar = function() {
            if (calendarInstance) {
                calendarInstance.reset();
            } else {
                console.log('Calendário não inicializado');
            }
        };
        
    } catch (error) {
        console.error('Erro ao inicializar calendário:', error);
    }
});

// Fallback - tenta inicializar novamente se o DOM já estiver carregado
if (document.readyState === 'complete' || document.readyState === 'interactive') {
    console.log('DOM já está pronto, tentando inicializar calendário');
    setTimeout(() => {
        if (!calendarInstance) {
            try {
                calendarInstance = new Calendar();
                console.log('Calendário inicializado via fallback');
            } catch (error) {
                console.error('Erro no fallback de inicialização:', error);
            }
        }
    }, 100);
}