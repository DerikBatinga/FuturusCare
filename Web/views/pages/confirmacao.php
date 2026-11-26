<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-agendamento">
    <div class="booking-container">
        <section class="booking-busca booking-confirmacao-busca" aria-labelledby="titulo-booking">
            <h1 id="titulo-booking">Cuidado agendado!</h1>
            <div class="booking-filtros booking-confirmacao-filtros">
                <div><small>Serviço</small><strong><?= escapar($agendamentoAtual['servico']) ?></strong></div>
                <div><small>Local</small><strong><?= escapar($agendamentoAtual['endereco'] ?? '') ?></strong></div>
                <div><small>Data</small><strong><?= escapar($agendamentoAtual['data']) ?> · <?= escapar($agendamentoAtual['horario']) ?></strong></div>
                <div><small>Pet</small><strong><?= escapar($petAtual['nome'] ?? 'Pet') ?></strong></div>
                <a class="botao botao-primario" href="agenda.php">Ver reserva</a>
            </div>
        </section>

        <div class="booking-confirmacao-conteudo">
            <div class="booking-confirmacao-principal">
                <section class="booking-confirmacao-sucesso" aria-labelledby="titulo-confirmacao">
                    <span class="booking-confirmacao-check"><?= iconeSvg('check') ?></span>
                    <p class="booking-confirmacao-status">Agendamento confirmado</p>
                    <h2 id="titulo-confirmacao">Um cuidado cheio de carinho espera por <?= escapar($petAtual['nome'] ?? 'seu pet') ?>.</h2>
                    <p><?= escapar($profissionalAtual['nome'] ?? 'A pessoa profissional') ?> atenderá <?= escapar($petAtual['nome'] ?? 'seu pet') ?> em <?= escapar(dataPorExtenso($agendamentoAtual['data'])) ?>, às <?= escapar($agendamentoAtual['horario']) ?>. Combine os últimos detalhes diretamente pelo chat.</p>
                    <p class="booking-confirmacao-codigo">Código do agendamento: <strong><?= escapar($agendamentoAtual['codigo'] ?? ('FC-' . str_pad((string) $agendamentoAtual['id'], 6, '0', STR_PAD_LEFT))) ?></strong></p>
                    <div class="booking-confirmacao-acoes">
                        <a class="botao botao-primario" href="mensagens.php">Conversar com <?= escapar($profissionalAtual['nome'] ?? 'profissional') ?></a>
                        <a class="botao botao-secundario" href="servicos.php">Voltar aos serviços</a>
                    </div>
                </section>

                <section class="booking-preparacao" aria-labelledby="titulo-preparacao">
                    <h2 id="titulo-preparacao">Antes de sair</h2>
                    <ul>
                        <li><?= iconeSvg('heart') ?><div><strong>Separe a guia e a coleira</strong><small>Deixe os itens de Thor por perto para facilitar o encontro.</small></div></li>
                        <li><?= iconeSvg('paw') ?><div><strong>Conte como ele gosta de passear</strong><small>Compartilhe preferências e cuidados importantes com Ana.</small></div></li>
                        <li><?= iconeSvg('calendar') ?><div><strong>Acompanhe pelo diário</strong><small>Consulte os registros do passeio e os cuidados do seu pet.</small></div></li>
                    </ul>
                    <a href="diario.php">Abrir diário de <?= escapar($petAtual['nome'] ?? 'seu pet') ?></a>
                </section>
            </div>

            <div class="booking-confirmacao-lateral">
                <aside class="booking-resumo booking-resumo-data" aria-labelledby="titulo-resumo-booking">
                    <h2 id="titulo-resumo-booking">Seu agendamento</h2>
                    <div class="booking-pet-resumo">
                        <span class="booking-pet-imagem"><img src="<?= escapar($petAtual ? imagemPet($petAtual) : 'assets/img/pet-home-destaque.png') ?>" alt="<?= escapar($petAtual['nome'] ?? 'Pet') ?>"></span>
                        <span><strong><?= escapar($petAtual['nome'] ?? 'Pet') ?></strong><small><?= escapar($petAtual['raca'] ?? '') ?> · <?= (int) ($petAtual['idade'] ?? 0) ?> anos</small></span>
                    </div>
                    <div class="booking-resumo-servico">
                        <strong><?= escapar($agendamentoAtual['servico']) ?></strong>
                        <p>45 minutos de atenção e movimento</p>
                        <div><span class="booking-ana-mini"><?= escapar(substr($profissionalAtual['nome'] ?? 'PR', 0, 2)) ?></span><span><?= escapar($profissionalAtual['nome'] ?? 'Profissional') ?><br><small>Profissional</small></span></div>
                        <div class="booking-data-linha"><?= iconeSvg('calendar') ?><span><?= escapar(dataPorExtenso($agendamentoAtual['data'], false)) ?><br><small><?= escapar($agendamentoAtual['horario']) ?>–<?= escapar($horaFim) ?></small></span></div>
                        <div><span><?= escapar($agendamentoAtual['servico']) ?></span><strong><?= escapar(moeda((float) $agendamentoAtual['valor'])) ?></strong></div>
                    </div>
                    <div class="booking-total"><strong>Total</strong><b><?= escapar(moeda((float) $agendamentoAtual['valor'])) ?></b></div>
                </aside>

                <aside class="booking-dica booking-dica-data">
                    <?= iconeSvg('heart') ?>
                    <div><h2>Um passeio pensado para Thor</h2><p>Compartilhe a rotina e os cuidados do seu pet com a profissional pelo chat.</p></div>
                </aside>
            </div>
        </div>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
