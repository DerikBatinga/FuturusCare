<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-componentes">
    <div class="container pilha">
        <header class="intro-componentes">
            <p class="rotulo-etapa">ETAPA 3 · COMPONENTES REUTILIZÁVEIS</p>
            <h1>Peças para cada cuidado.</h1>
            <p class="texto-suave">Uma demonstração dos elementos que serão usados nas telas da FuturusCare.</p>
        </header>

        <section class="grupo-componentes" aria-labelledby="titulo-botoes">
            <h2 id="titulo-botoes">Botões</h2>
            <div class="amostras-linha">
                <button class="botao botao-primario" type="button">Encontrar cuidador <span aria-hidden="true">→</span></button>
                <button class="botao botao-secundario" type="button">Acessar conta</button>
                <button class="botao botao-contorno" type="button">Ver perfil</button>
                <button class="botao botao-primario" type="button" disabled>Indisponível</button>
            </div>
        </section>

        <section class="grupo-componentes" aria-labelledby="titulo-campos">
            <h2 id="titulo-campos">Campos e seleção</h2>
            <form class="cartao cartao-formulario" action="#" method="get">
                <label class="campo">
                    <span>Nome do pet</span>
                    <input type="text" name="nome-pet" placeholder="Ex.: Thor">
                </label>
                <label class="campo">
                    <span>Serviço</span>
                    <select name="servico">
                        <option value="">Escolha um serviço</option>
                        <option>Pet Sitting</option>
                        <option>Passeio</option>
                        <option>Hospedagem</option>
                    </select>
                </label>
                <label class="campo campo-largo">
                    <span>Conte um pouco sobre a rotina</span>
                    <textarea name="rotina" rows="3" placeholder="Preferências, horários e cuidados especiais"></textarea>
                </label>
                <label class="opcao-checkbox campo-largo">
                    <input type="checkbox" name="vacinas">
                    <span>As vacinas do pet estão em dia</span>
                </label>
            </form>
        </section>

        <section class="grupo-componentes" aria-labelledby="titulo-etiquetas">
            <h2 id="titulo-etiquetas">Etiquetas</h2>
            <div class="amostras-linha">
                <span class="etiqueta etiqueta-lilas">Pet Sitting</span>
                <span class="etiqueta etiqueta-pessego">Próximo de você</span>
                <span class="etiqueta etiqueta-verde">Verificado</span>
                <span class="etiqueta etiqueta-neutra">Agendado</span>
            </div>
        </section>

        <section class="grupo-componentes" aria-labelledby="titulo-avatares">
            <h2 id="titulo-avatares">Avatares</h2>
            <div class="amostras-linha amostras-avatares">
                <div class="perfil-resumo">
                    <span class="avatar avatar-pessoa" aria-label="Ana Martins">AM</span>
                    <span><strong>Ana Martins</strong><small>Cuidadora verificada</small></span>
                </div>
                <div class="perfil-resumo">
                    <span class="avatar avatar-pet" aria-label="Thor">T</span>
                    <span><strong>Thor</strong><small>Golden retriever · 3 anos</small></span>
                </div>
            </div>
        </section>

        <section class="grupo-componentes" aria-labelledby="titulo-cards">
            <h2 id="titulo-cards">Cards</h2>
            <div class="grade-cards">
                <article class="cartao cartao-servico">
                    <span class="icone-servico icone-passeio"><?= iconeSvg('walk') ?></span>
                    <div>
                        <h3>Passeio</h3>
                        <p class="texto-suave">Movimento e novas descobertas para seu pet.</p>
                    </div>
                    <span class="preco-servico">A partir de <strong>R$ 45</strong></span>
                </article>

                <article class="cartao cartao-profissional">
                    <div class="cartao-profissional-topo">
                        <span class="avatar avatar-pessoa avatar-grande" aria-label="Ana Martins">AM</span>
                        <span class="etiqueta etiqueta-verde">Verificado</span>
                    </div>
                    <h3>Ana Martins</h3>
                    <p class="texto-suave">Pet sitter · Pinheiros, São Paulo</p>
                    <p class="avaliacao"><span aria-label="5 estrelas">★★★★★</span> 5,0 <small>(28 avaliações)</small></p>
                    <a class="botao botao-contorno botao-cheio" href="#titulo-cards">Conhecer perfil</a>
                </article>

                <article class="cartao cartao-pet">
                    <span class="avatar avatar-pet avatar-grande" aria-label="Thor">T</span>
                    <div>
                        <p class="rotulo-card">MEU PET</p>
                        <h3>Thor</h3>
                        <p class="texto-suave">Golden retriever · 3 anos</p>
                    </div>
                    <span class="etiqueta etiqueta-lilas">Perfil completo</span>
                </article>

                <article class="cartao cartao-agendamento">
                    <div class="linha-entre">
                        <div>
                            <p class="rotulo-card">PRÓXIMO AGENDAMENTO</p>
                            <h3>Passeio com Thor</h3>
                        </div>
                        <span class="etiqueta etiqueta-neutra">Confirmado</span>
                    </div>
                    <p class="texto-suave">Quinta-feira, 24 de outubro · 10h</p>
                    <div class="divisor"></div>
                    <div class="linha-entre">
                        <span>Cuidadora</span>
                        <strong>Ana Martins</strong>
                    </div>
                    <div class="linha-entre">
                        <span>Total</span>
                        <strong>R$ 45,00</strong>
                    </div>
                </article>
            </div>
        </section>

        <aside class="nota-componentes">
            Os avatares desta demonstração usam iniciais. Nas telas, serão substituídos pelas imagens correspondentes do Figma.
        </aside>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
