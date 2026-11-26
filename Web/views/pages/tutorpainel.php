<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-perfil-tutor">
    <div class="perfil-tutor-container">
        <section class="perfil-tutor-capa" aria-label="Perfil de <?= escapar($usuarioPerfil['nome']) ?>">
            <div class="perfil-tutor-foto" aria-label="<?= escapar($usuarioPerfil['nome']) ?>"><?php if (imagemPerfil($usuarioPerfil)): ?><img src="<?= escapar(imagemPerfil($usuarioPerfil)) ?>" alt="Foto de <?= escapar($usuarioPerfil['nome']) ?>"><?php else: ?><?= escapar(strtoupper(substr($usuarioPerfil['nome'], 0, 1) . substr(strrchr(' ' . $usuarioPerfil['nome'], ' ') ?: $usuarioPerfil['nome'], 1, 1))) ?><?php endif; ?></div>
            <div class="perfil-tutor-identidade"><p class="perfil-tutor-sobretitulo">MEU PERFIL</p><h1><?= escapar($usuarioPerfil['nome']) ?></h1><p><?= iconeSvg('pin') ?> <?= escapar($localizacaoPerfil) ?></p><small>Membro FuturusCare desde <?= escapar($dataCadastroPerfil) ?></small></div>
            <a class="botao botao-secundario" href="edicao.php">Editar perfil</a>
        </section>

        <div class="perfil-tutor-layout">
            <div class="perfil-tutor-principal">
                <section class="perfil-tutor-sobre" aria-labelledby="titulo-sobre-larissa">
                    <h2 id="titulo-sobre-larissa">Sobre <?= escapar($usuarioPerfil['nome']) ?></h2>
                    <p>Olá, eu sou <?= escapar(explode(' ', $usuarioPerfil['nome'])[0]) ?>! Amo meus pets e gosto de acompanhar cada momento da rotina deles.</p>
                </section>

                <section class="perfil-tutor-pets" aria-labelledby="titulo-meus-pets">
                    <div class="perfil-tutor-secao-topo"><div><h2 id="titulo-meus-pets">Meus pets</h2><p><?= count($petsTutor) ?> companheiro(s)</p></div><a href="pets.php">Adicionar pet</a></div>
                    <?php foreach ($petsTutor as $pet): ?>
                    <article class="perfil-tutor-pet-card">
                        <div class="perfil-tutor-pet-arte"><img class="perfil-tutor-pet-forma" src="assets/img/forma-lavanda.svg" alt=""><img class="perfil-tutor-pet-imagem <?= !empty($pet['foto']) ? 'perfil-tutor-pet-imagem-enviada' : '' ?>" src="<?= escapar(imagemPet($pet)) ?>" alt="<?= escapar($pet['nome']) ?>"></div>
                        <div class="perfil-tutor-pet-info"><h3><?= escapar($pet['nome']) ?></h3><p><?= escapar($pet['raca']) ?> · <?= escapar($pet['idade']) ?> anos</p><p><?= escapar($pet['observacoes'] ?: 'Curioso, carinhoso e sempre pronto para passear.') ?></p><a href="diario.php">Abrir diário de <?= escapar($pet['nome']) ?></a></div>
                        <a class="perfil-tutor-pet-editar" href="pets.php?id=<?= (int) $pet['id'] ?>" aria-label="Editar cadastro de <?= escapar($pet['nome']) ?>">Editar</a>
                    </article>
                    <?php endforeach; ?>
                </section>

                <nav class="perfil-tutor-atalhos" aria-label="Acesso rápido">
                    <a class="perfil-tutor-atalho" href="diario.php"><span aria-hidden="true">▧</span><strong>Diário do pet</strong><small>Veja os registros do dia.</small><b>Acessar →</b></a>
                    <a class="perfil-tutor-atalho" href="mensagens.php"><?= iconeSvg('chat') ?><strong>Conversas</strong><small>Fale com quem cuida.</small><b>Acessar →</b></a>
                    <a class="perfil-tutor-atalho" href="servico.php"><?= iconeSvg('calendar') ?><strong>Agendar cuidado</strong><small>Encontre um serviço.</small><b>Acessar →</b></a>
                </nav>

                <section class="perfil-tutor-galeria galeria-perfil" aria-labelledby="titulo-galeria-tutor">
                    <div class="perfil-tutor-secao-topo"><div><h2 id="titulo-galeria-tutor"><?php if (count($galeriaPet) < 12): ?><button class="galeria-titulo-acao galeria-abrir-seletor" type="button">Galeria de fotos</button><?php else: ?>Galeria de fotos<?php endif; ?></h2><p><?= count($galeriaPet) ?> de 12 fotos</p></div></div>
                    <?php if ($galeriaPet): ?>
                        <div class="perfil-tutor-fotos">
                            <?php foreach ($galeriaPet as $indice => $imagem): ?>
                                <figure class="galeria-item-gerenciavel">
                                    <img src="<?= escapar($imagem) ?>" alt="Foto <?= $indice + 1 ?> da galeria de <?= escapar($usuarioPerfil['nome']) ?>">
                                    <div class="galeria-item-acoes">
                                        <form class="galeria-trocar-form" method="post" action="tutorpainel.php" enctype="multipart/form-data">
                                            <input type="hidden" name="acao" value="galeria_trocar"><?= csrfCampo() ?><input type="hidden" name="indice" value="<?= $indice ?>">
                                            <label>Trocar<input class="galeria-troca-arquivo" type="file" name="foto" accept="image/jpeg,image/png,image/webp" aria-label="Trocar foto <?= $indice + 1 ?>"></label>
                                            <button type="submit" disabled>Salvar troca</button>
                                        </form>
                                        <form method="post" action="tutorpainel.php" data-confirmacao="Excluir esta foto da galeria?">
                                            <input type="hidden" name="acao" value="galeria_excluir"><?= csrfCampo() ?><input type="hidden" name="indice" value="<?= $indice ?>">
                                            <button type="submit">Excluir</button>
                                        </form>
                                    </div>
                                </figure>
                            <?php endforeach; ?>
                        </div>
                    <?php else: ?>
                        <button class="galeria-vazia galeria-abrir-seletor" type="button"><?= iconeSvg('camera') ?><span><strong>Sua galeria ainda está vazia</strong><small>Adicione fotos dos seus pets e dos momentos que vocês compartilham.</small></span></button>
                    <?php endif; ?>
                    <?php if (count($galeriaPet) < 12): ?>
                        <form class="galeria-upload-form" method="post" action="tutorpainel.php" enctype="multipart/form-data">
                            <input type="hidden" name="acao" value="galeria_adicionar"><?= csrfCampo() ?>
                            <input class="galeria-arquivos" type="file" name="galeria[]" accept="image/jpeg,image/png,image/webp" data-restantes="<?= 12 - count($galeriaPet) ?>" multiple>
                            <dialog class="galeria-confirmacao" aria-labelledby="titulo-confirmar-galeria-tutor">
                                <div class="galeria-confirmacao-conteudo">
                                    <h3 id="titulo-confirmar-galeria-tutor">Confirmar fotos</h3>
                                    <p>Revise as imagens antes de adicioná-las à sua galeria.</p>
                                    <div class="galeria-previas" aria-live="polite"></div>
                                    <small>JPG, PNG ou WebP · até 5 MB por foto · máximo de 12 fotos.</small>
                                    <div class="galeria-confirmacao-acoes"><button class="botao botao-secundario galeria-cancelar" type="button">Cancelar</button><button class="botao botao-primario galeria-confirmar" type="submit" disabled>Confirmar</button></div>
                                </div>
                            </dialog>
                        </form>
                    <?php endif; ?>
                </section>

                <section class="perfil-tutor-dados" id="meus-dados" aria-labelledby="titulo-dados-tutor">
                    <div><h2 id="titulo-dados-tutor">Meus dados</h2><p>Mantenha suas informações de contato atualizadas.</p></div>
                    <form class="perfil-tutor-formulario" method="post" action="tutorpainel.php">
                <input type="hidden" name="acao" value="editar_perfil">
                <?= csrfCampo() ?>
                        <label>Nome completo<input type="text" name="nome" value="<?= escapar($usuarioPerfil['nome']) ?>"></label>
                        <label>E-mail<input type="email" name="email" value="<?= escapar($usuarioPerfil['email']) ?>"></label>
                        <label>Telefone<input type="tel" name="telefone" value="<?= escapar($usuarioPerfil['telefone']) ?>"></label>
                        <label>Bairro e cidade<input type="text" name="endereco" value="<?= escapar($usuarioPerfil['endereco'] ?? '') ?>"></label>
                        <div class="perfil-tutor-form-acoes"><button class="botao botao-primario" type="submit">Salvar alterações</button><small>Suas informações ficam visíveis apenas para você.</small></div>
                    </form>
                </section>
            </div>

            <aside class="perfil-tutor-lateral">
                <section class="perfil-tutor-proximo" aria-labelledby="titulo-proximo-cuidado">
                    <div class="perfil-tutor-proximo-topo"><?= iconeSvg('calendar') ?><span class="perfil-tutor-status">Confirmado</span></div>
                    <h2 id="titulo-proximo-cuidado">Próximo cuidado</h2>
                    <h3>Passeio com Thor</h3>
                    <dl><div><dt>Data</dt><dd>02/10/2026</dd></div><div><dt>Horário</dt><dd>09:00–09:45</dd></div><div><dt>Profissional</dt><dd>Ana Martins</dd></div><div><dt>Local</dt><dd>Vila Mariana, São Paulo</dd></div></dl>
                    <a class="botao botao-primario botao-cheio" href="mensagens.php">Conversar com Ana</a>
                    <a class="perfil-tutor-link-agendamento" href="agenda.php">Ver agendamento</a>
                </section>
                <section class="perfil-tutor-ajuda"><?= iconeSvg('heart') ?><h2>A gente está por perto.</h2><p>Conte com a FuturusCare para tirar dúvidas sobre sua experiência.</p><a href="contato.php">Entrar em contato →</a></section>
            </aside>
        </div>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
