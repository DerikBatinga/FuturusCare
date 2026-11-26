<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-agendamento">
    <div class="booking-container">
        <section class="booking-busca" aria-labelledby="titulo-booking">
            <h1 id="titulo-booking">Um cuidado especial começa aqui</h1>
            <form class="booking-filtros" method="post" action="servico.php">
                <input type="hidden" name="acao" value="escolher_servico">
                <?= csrfCampo() ?>
                <input type="hidden" name="profissional_id" value="<?= $profissionalBookingId ?>">
                <label>Serviço
                    <select name="servico"><option <?= $servicoBooking === 'Passeio com cães' ? 'selected' : '' ?>>Passeio com cães</option><option <?= $servicoBooking === 'Pet sitter' ? 'selected' : '' ?>>Pet sitter</option><option <?= $servicoBooking === 'Hospedagem' ? 'selected' : '' ?>>Hospedagem</option></select>
                </label>
                <label>Local
                    <input type="text" name="local" value="<?= escapar($localBooking) ?>" aria-label="Local do atendimento">
                </label>
                <label>Data
                    <input type="text" name="data" value="Escolher data" aria-label="Data do atendimento" readonly>
                </label>
                <label>Pet
                    <select name="pet_id" aria-label="Pet">
                        <?php foreach ($petsTutor as $pet): ?>
                            <option value="<?= (int) $pet['id'] ?>" <?= (int) $pet['id'] === $petBookingId ? 'selected' : '' ?>><?= escapar($pet['nome']) ?> · <?= escapar($pet['raca']) ?></option>
                        <?php endforeach; ?>
                    </select>
                </label>
                <button class="botao botao-primario" type="submit">Buscar cuidado</button>
            </form>
        </section>

        <nav class="booking-etapas" aria-label="Etapas do agendamento">
            <span class="etapa-atual"><b>1</b> Serviço</span>
            <span><b>2</b> Profissional</span>
            <span><b>3</b> Data e horário</span>
            <span><b>4</b> Revisão</span>
        </nav>

        <form id="form-servico-booking" method="post" action="servico.php">
                <input type="hidden" name="acao" value="escolher_servico">
                <?= csrfCampo() ?>
                <input type="hidden" name="servico" value="<?= escapar($servicoBooking) ?>">
                <input type="hidden" name="pet_id" value="<?= $petBookingId ?>">
                <input type="hidden" name="profissional_id" value="<?= $profissionalBookingId ?>">
                <input type="hidden" name="local" value="<?= escapar($localBooking) ?>">
                <input type="hidden" name="endereco" value="<?= escapar($localBooking) ?>">
        </form>
        <div class="booking-conteudo">
            <section class="booking-escolha" aria-labelledby="titulo-servico-booking">
                <h2 id="titulo-servico-booking">Qual cuidado <?= escapar($petAtual['nome'] ?? 'seu pet') ?> precisa?</h2>
                <div class="booking-servicos">
                    <?php foreach ($servicosDisponiveis as $servico): ?>
                        <button class="booking-servico <?= $servico['selecionado'] ? 'booking-servico-selecionado' : '' ?>" type="button" value="<?= escapar($servico['nome']) ?>" aria-pressed="<?= $servico['selecionado'] ? 'true' : 'false' ?>">
                            <span class="booking-servico-icone"><?= iconeSvg($servico['icone']) ?></span>
                            <span class="booking-servico-texto"><strong><?= htmlspecialchars($servico['nome'], ENT_QUOTES, 'UTF-8') ?></strong><small><?= htmlspecialchars($servico['descricao'], ENT_QUOTES, 'UTF-8') ?></small><small><?= htmlspecialchars($servico['detalhe'], ENT_QUOTES, 'UTF-8') ?></small></span>
                            <span class="booking-servico-valor"><strong><?= htmlspecialchars($servico['valor'], ENT_QUOTES, 'UTF-8') ?></strong><small><?= $servico['selecionado'] ? 'Selecionado' : 'Ver serviços' ?></small></span>
                        </button>
                    <?php endforeach; ?>
                </div>
                <a class="booking-todos-servicos" href="servicos.php">Ver todos os serviços</a>
            </section>

            <aside class="booking-resumo" aria-labelledby="titulo-resumo-booking">
                <h2 id="titulo-resumo-booking">Seu agendamento</h2>
                <div class="booking-pet-resumo">
                    <span class="booking-pet-imagem"><img src="<?= escapar($petAtual ? imagemPet($petAtual) : 'assets/img/pet-home-destaque.png') ?>" alt="<?= escapar($petAtual['nome'] ?? 'Pet') ?>"></span>
                    <span><strong><?= escapar($petAtual['nome'] ?? 'Cadastre seu pet') ?></strong><small><?= escapar(($petAtual['raca'] ?? '') . (isset($petAtual['idade']) ? ' · ' . $petAtual['idade'] . ' anos' : '')) ?></small></span>
                </div>
                <div class="booking-resumo-servico">
                    <strong><?= escapar($servicoAtual['nome']) ?></strong>
                    <p><?= escapar($servicoAtual['descricao']) ?></p>
                    <div><span><?= escapar($servicoAtual['detalhe']) ?></span><strong><?= escapar($servicoAtual['valor']) ?></strong></div>
                </div>
                <div class="booking-total"><strong>Total</strong><b><?= escapar(moeda($valorServicoBooking)) ?></b></div>
                <?php if ($petsTutor): ?><button class="botao botao-primario botao-cheio" type="submit" form="form-servico-booking">Escolher profissional</button><?php else: ?><a class="botao botao-primario botao-cheio" href="pets.php">Cadastre um pet para continuar</a><?php endif; ?>
                <p class="booking-observacao">Você poderá revisar os detalhes antes de confirmar.</p>
            </aside>
            <aside class="booking-dica">
                <?= iconeSvg('heart') ?>
                <div><h2>Um passeio pensado para Thor</h2><p>Compartilhe a rotina e os cuidados do seu pet com o profissional pelo chat.</p></div>
            </aside>
        </div>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
