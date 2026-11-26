<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-profissionais">
    <div class="container profissionais-cabecalho">
        <div>
            <p class="profissionais-etiqueta">CUIDADO PERTO DE VOCÊ</p>
            <h1>Encontre seu profissional</h1>
            <p>Conheça quem oferece o cuidado que combina com a rotina do seu pet.</p>
        </div>
        <form class="profissionais-busca" method="get" action="profissionais.php" role="search">
            <label for="busca-servico">Qual serviço você procura?</label>
            <div>
                <input id="busca-servico" type="search" name="servico" value="<?= escapar($servicoBuscado) ?>" placeholder="Ex.: passeio, hospedagem, pet sitter">
                <button class="botao botao-primario" type="submit">Buscar</button>
            </div>
        </form>
    </div>

    <div class="container profissionais-layout">
        <aside class="booking-filtros-profissionais profissionais-filtros" aria-labelledby="titulo-filtros-profissionais">
            <div class="booking-filtro-titulo">
                <h2 id="titulo-filtros-profissionais">Filtrar profissionais</h2>
                <a href="profissionais.php">Limpar filtros</a>
            </div>
            <form method="get" action="profissionais.php">
                <fieldset>
                    <legend>Serviço</legend>
                    <label class="profissionais-campo-servico" for="filtro-servico">Digite ou escolha abaixo</label>
                    <input id="filtro-servico" class="profissionais-input-servico" type="search" name="servico" value="<?= escapar($servicoBuscado) ?>" placeholder="Nome do serviço">
                    <div class="profissionais-servicos-rapidos">
                        <?php foreach ($servicosDisponiveis as $servico): ?>
                            <a href="profissionais.php?servico=<?= urlencode($servico) ?>" <?= strcasecmp($servicoBuscado, $servico) === 0 ? 'aria-current="page"' : '' ?>><?= escapar($servico) ?></a>
                        <?php endforeach; ?>
                    </div>
                    <button class="botao botao-primario botao-cheio" type="submit">Aplicar</button>
                </fieldset>
            </form>
            <fieldset>
                <legend>Tipo de pet</legend>
                <label><input type="checkbox" checked> Cachorro</label>
                <label><input type="checkbox"> Gato</label>
            </fieldset>
            <fieldset>
                <legend>Porte do pet</legend>
                <label><input type="checkbox" checked> Todos os portes</label>
                <label><input type="checkbox"> Pequeno · até 10 kg</label>
                <label><input type="checkbox"> Médio · 10 a 25 kg</label>
                <label><input type="checkbox"> Grande · acima de 25 kg</label>
            </fieldset>
            <fieldset class="booking-filtro-preco">
                <legend>Valor máximo</legend>
                <strong>Até R$ 150,00</strong>
                <input type="range" min="10" max="150" value="150" aria-label="Valor máximo">
                <div><span>R$ 10</span><span>R$ 150</span></div>
            </fieldset>
            <fieldset>
                <legend>Experiência e cuidados</legend>
                <label><input type="checkbox"> Cuida de pets idosos</label>
                <label><input type="checkbox"> Administração de medicação</label>
                <label><input type="checkbox"> Primeiros socorros</label>
            </fieldset>
            <fieldset>
                <legend>Disponibilidade</legend>
                <label><input type="checkbox" checked> Atende na minha região</label>
            </fieldset>
        </aside>

        <section class="profissionais-resultados" aria-labelledby="titulo-resultados-profissionais">
            <header class="profissionais-resultados-topo">
                <div>
                    <h2 id="titulo-resultados-profissionais"><?= count($profissionais) ?> <?= count($profissionais) === 1 ? 'profissional encontrado' : 'profissionais encontrados' ?></h2>
                    <?php if ($servicoBuscado !== ''): ?><p>Resultados para “<?= escapar($servicoBuscado) ?>”</p><?php else: ?><p>Todos os profissionais disponíveis</p><?php endif; ?>
                </div>
            </header>

            <?php if ($profissionais): ?>
                <div class="booking-lista-profissionais">
                    <?php foreach ($profissionais as $profissional): ?>
                        <article class="booking-profissional profissional-listagem-card">
                            <span class="booking-profissional-avatar">
                                <?php if (!empty($profissional['foto'])): ?><img src="<?= escapar($profissional['foto']) ?>" alt="Foto de <?= escapar($profissional['nome']) ?>"><?php else: ?><?= escapar($profissional['iniciais']) ?><?php endif; ?>
                            </span>
                            <div class="booking-profissional-info">
                                <h3><?= escapar($profissional['nome']) ?></h3>
                                <p class="booking-profissional-nota"><span aria-label="5 estrelas">★</span> <?= escapar($profissional['nota']) ?> · <?= escapar($profissional['avaliacoes']) ?></p>
                                <p><?= escapar($profissional['sobre']) ?></p>
                                <small><?= iconeSvg('pin') ?> Atende em <?= escapar($profissional['regiao']) ?></small>
                                <ul class="profissional-listagem-servicos" aria-label="Serviços oferecidos">
                                    <?php foreach ($profissional['servicos'] as $servico): ?><li><?= escapar($servico) ?></li><?php endforeach; ?>
                                </ul>
                            </div>
                            <div class="booking-profissional-acoes">
                                <strong>A partir de <?= moeda($profissional['menor_preco']) ?></strong>
                                <small>Atende na região</small>
                                <a class="botao botao-primario" href="profissionalpublico.php?id=<?= (int) $profissional['id'] ?>">Ver perfil</a>
                                <a class="profissional-agendar-link" href="servico.php?profissional_id=<?= (int) $profissional['id'] ?>">Agendar cuidado</a>
                            </div>
                        </article>
                    <?php endforeach; ?>
                </div>
            <?php else: ?>
                <div class="profissionais-vazio" role="status">
                    <?= iconeSvg('paw') ?>
                    <h3>Nenhum profissional encontrado</h3>
                    <p>Tente outro termo, como passeio, hospedagem ou pet sitter.</p>
                    <a class="botao botao-secundario" href="profissionais.php">Ver todos os profissionais</a>
                </div>
            <?php endif; ?>
        </section>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
