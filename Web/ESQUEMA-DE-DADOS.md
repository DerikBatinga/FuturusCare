# Esquema de dados FuturusCare

Este documento descreve as entidades usadas nas telas. É uma referência para modelagem futura; não contém SQL. Os dados atuais ficam em arrays PHP e as alterações sobrevivem apenas enquanto a sessão do navegador estiver ativa.

## Entidades

- **Usuário** — `id: inteiro`, `nome: texto`, `email: texto único`, `telefone: texto`, `tipo: tutor|profissional`, `senha: hash`, `endereco: texto`, `data_cadastro: data`, `foto: caminho opcional`. Um usuário pode ter um perfil de tutor ou profissional.
- **Perfil profissional** — `id: inteiro`, `usuario_id: inteiro`, `regiao: texto`, `sobre: texto`, `servicos: lista de texto`, `preco: decimal`, `disponibilidade: lista de dias`. Pertence a um usuário profissional (1:1).
- **Pet** — `id: inteiro`, `tutor_id: inteiro`, `nome: texto`, `especie: texto`, `raca: texto`, `idade: inteiro`, `peso: decimal`, `observacoes: texto`, `foto: caminho opcional`. Um tutor pode cadastrar vários pets (1:N).
- **Serviço** — `id: inteiro`, `nome: texto`, `descricao: texto`, `duracao_minutos: inteiro`, `preco: decimal`, `unidade: texto`. Um perfil profissional pode oferecer vários serviços e cada serviço pode ser oferecido por vários profissionais (N:N).
- **Agendamento** — `id: inteiro`, `tutor_id: inteiro`, `profissional_id: inteiro`, `pet_id: inteiro`, `servico_id: inteiro`, `data: data`, `horario: hora`, `valor: decimal`, `status: texto`, `observacoes: texto`. Tutor, profissional e pet têm vários agendamentos (1:N).
- **Mensagem** — `id: inteiro`, `de_usuario_id: inteiro`, `para_usuario_id: inteiro`, `agendamento_id: inteiro opcional`, `texto: texto`, `data_hora: data e hora`, `lida: booleano`. Usuários participam de várias mensagens; conversa pode se relacionar a um agendamento (1:N).
- **Registro de diário** — `id: inteiro`, `pet_id: inteiro`, `autor_id: inteiro`, `agendamento_id: inteiro opcional`, `categoria: texto`, `titulo: texto`, `relato: texto`, `data: data`, `horario: hora`, `foto: caminho opcional`. Um pet pode ter muitos registros (1:N).
- **Pagamento** — `id: inteiro`, `agendamento_id: inteiro`, `metodo: pix|cartao`, `valor: decimal`, `status: texto`, `data_hora: data e hora`. Cada agendamento pode ter um ou mais registros de tentativa de pagamento (1:N).
- **Avaliação** — `id: inteiro`, `agendamento_id: inteiro`, `tutor_id: inteiro`, `profissional_id: inteiro`, `nota: inteiro`, `comentario: texto`, `data: data`. Uma avaliação pertence a um atendimento concluído.
- **Foto de galeria** — `id: inteiro`, `usuario_id: inteiro ou perfil_profissional_id: inteiro`, `caminho: texto`, `descricao: texto`, `ordem: inteiro`. Cada perfil pode ter várias fotos (1:N).

## Relações principais

- Usuário tutor 1:N Pet e 1:N Agendamento.
- Usuário profissional 1:1 Perfil profissional e 1:N Agendamento.
- Pet 1:N Agendamento e 1:N Registro de diário.
- Perfil profissional N:N Serviço.
- Agendamento 1:N Mensagem opcional, 1:N Pagamento e 0:1 Avaliação.
- Usuários relacionam-se entre si por Mensagens (N:N, resolvida pelos campos remetente e destinatário).

## Observações da demonstração

Senhas de demonstração são armazenadas como hash. Contas disponíveis: `bruno@exemplo.com` / `ThorPet2026!` e `ana@exemplo.com` / `Cuidar2026!`. Nenhuma transação real é feita. As fotos são gravadas em `uploads/`, e seus caminhos permanecem associados ao perfil durante a sessão. A recuperação de senha não envia e-mail.
