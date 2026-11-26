<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="home">
    <section class="home-hero" aria-labelledby="titulo-home">
        <div class="home-hero-conteudo">
            <p class="home-sobretitulo">SEGURANÇA <span>•</span> CUIDADO <span>•</span> AMOR</p>
            <h1 id="titulo-home">Cuidar de quem<br>você ama<br><span>ficou fácil.</span></h1>
            <p class="home-intro">Encontre quem cuida como você. Conectamos seu pet a profissionais para uma rotina com mais carinho e tranquilidade.</p>
            <div class="home-acoes">
                <a class="botao botao-primario" href="servico.php">Encontrar um cuidador <span aria-hidden="true">→</span></a>
                <a class="botao botao-secundario" href="profissional.php">Sou profissional</a>
            </div>
        </div>

        <div class="home-hero-arte" aria-label="Um gato e um cachorro juntos">
            <img class="forma forma-hero-laranja" src="assets/img/forma-laranja.svg" alt="">
            <img class="forma forma-hero-lavanda" src="assets/img/forma-lavanda.svg" alt="">
            <img class="forma forma-hero-amarela" src="assets/img/detalhe-amarelo.svg" alt="">
            <div class="home-hero-mascara">
                <img src="assets/img/pets-home-principal.png" alt="Gato caramelo ao lado de um cachorro Border Collie">
            </div>
        </div>
    </section>

    <section class="home-diferenciais" aria-label="Por que escolher a FuturusCare">
        <article class="diferencial-cartao">
            <h2>Carinho em cada cuidado</h2>
            <p>Atenção à rotina e às necessidades do seu pet.</p>
        </article>
        <article class="diferencial-cartao">
            <h2>Tudo em um só lugar</h2>
            <p>Encontre serviços e organize seus agendamentos.</p>
        </article>
        <article class="diferencial-cartao diferencial-lilas">
            <h2>Perto, mesmo de longe</h2>
            <p>Converse com o cuidador pelo chat do aplicativo.</p>
        </article>
    </section>

    <section class="home-servicos" id="servicos" aria-labelledby="titulo-servicos">
        <div class="secao-titulo">
            <h2 id="titulo-servicos">Um cuidado para cada momento.</h2>
            <p>Conheça os serviços para deixar a rotina do seu pet mais feliz.</p>
        </div>
        <div class="lista-servicos">
            <article class="servico-home">
                <img src="assets/img/icone-servico-passeio.svg" alt="" aria-hidden="true">
                <h3>Dog Walking</h3>
                <p>Passeios para gastar energia e explorar o mundo.</p>
            </article>
            <article class="servico-home">
                <img src="assets/img/icone-servico-pet-sitting.svg" alt="" aria-hidden="true">
                <h3>Pet Sitting</h3>
                <p>Companhia e cuidado no conforto de casa.</p>
            </article>
            <article class="servico-home">
                <img src="assets/img/icone-servico-banho-tosa.svg" alt="" aria-hidden="true">
                <h3>Banho &amp; Tosa</h3>
                <p>Higiene, bem-estar e aquele carinho extra.</p>
            </article>
            <article class="servico-home">
                <img src="assets/img/icone-servico-veterinario.svg" alt="" aria-hidden="true">
                <h3>Veterinário</h3>
                <p>Atenção à saúde em cada fase da vida.</p>
            </article>
        </div>
    </section>

    <section class="home-destaques" aria-label="Destaques da FuturusCare">
        <article class="destaque-viagem">
            <img class="forma forma-viagem-laranja" src="assets/img/forma-passeio.svg" alt="">
            <img class="destaque-cachorro" src="assets/img/pet-home-destaque.png" alt="Border Collie sentado">
            <p class="home-sobretitulo">PARA VOCÊ E SEU MELHOR AMIGO</p>
            <h2>Sua viagem leve.<br>Seu pet feliz.</h2>
            <p class="destaque-descricao">Encontre companhia e carinho para ele enquanto você estiver fora.</p>
            <a class="botao botao-primario" href="servicos.php#pet-sitting">Conhecer Pet Sitting <span aria-hidden="true">→</span></a>
        </article>
        <div class="destaques-laterais">
            <article class="destaque-passeios">
                <img class="icone-pet destaque-icone-passeio" src="assets/img/icone-cao.svg" alt="">
                <img class="forma destaque-forma-amarela" src="assets/img/forma-destaque-passeio.svg" alt="">
                <h2>Mais passeios. Mais alegria.</h2>
                <p>Movimento e novas descobertas para o seu pet.</p>
                <a href="#servicos">Explorar Dog Walking <span aria-hidden="true">→</span></a>
            </article>
            <article class="destaque-aplicativo">
                <img class="forma destaque-forma-rosa" src="assets/img/detalhe-lavanda.svg" alt="">
                <h2>O cuidado continua no app.</h2>
                <p>Seus pets, agendamentos e conversas sempre juntos.</p>
                <a href="#como-funciona">Conheça a FuturusCare <span aria-hidden="true">→</span></a>
            </article>
        </div>
    </section>

    <section class="home-sobre" id="sobre" aria-labelledby="titulo-sobre">
        <div class="home-sobre-arte">
            <img class="forma-sobre-creme" src="assets/img/forma-creme.svg" alt="">
            <img class="forma-sobre-lavanda" src="assets/img/detalhe-lavanda.svg" alt="">
            <img class="home-cachorro-cuidado" src="assets/img/pet-home-cuidado.png" alt="Cachorro aproveitando um momento tranquilo">
        </div>
        <div class="home-sobre-texto">
            <p class="home-sobretitulo">CONEXÕES QUE FAZEM BEM</p>
            <h2 id="titulo-sobre">Mais cuidado para eles.<br>Mais tranquilidade<br>para você.</h2>
            <p>A FuturusCare aproxima quem ama pets de quem ama cuidar. Encontre profissionais, organize a rotina do seu companheiro e acompanhe tudo pelo aplicativo.</p>
            <ul class="lista-beneficios">
                <li>Perfil do seu pet</li>
                <li>Agendamentos organizados</li>
                <li>Chat com o cuidador</li>
                <li>Serviços para sua rotina</li>
            </ul>
            <a class="botao botao-primario" href="sobre.php">Quero conhecer <span aria-hidden="true">→</span></a>
        </div>
    </section>

    <section class="home-comecar" id="como-funciona" aria-labelledby="titulo-comecar">
        <div class="home-comecar-arte">
            <img class="forma-comecar-lavanda" src="assets/img/forma-lavanda-suave.svg" alt="">
            <img class="pets-comecar" src="assets/img/pets-home-cta.png" alt="Cachorro, gato e papagaio reunidos">
        </div>
        <div class="home-comecar-texto">
            <p>1. ENCONTRE <span>·</span> 2. CONVERSE <span>·</span> 3. AGENDE</p>
            <h2 id="titulo-comecar">Seu pet merece<br>esse carinho.</h2>
            <p>Escolha o serviço, conheça o profissional e combine o melhor cuidado para quem faz parte da sua família.</p>
            <a class="botao botao-claro" href="servico.php">Começar agora <span aria-hidden="true">→</span></a>
        </div>
    </section>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
