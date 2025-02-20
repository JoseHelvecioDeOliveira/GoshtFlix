# GoshtFlix

### Contexto

**O objetivo deste desafio é desenvolver um aplicativo Android simples, porém funcional, para exibir informações sobre filmes utilizando a API gratuita The Movie DB.**

O App foi desenvolvido seguindo as Stacks atuais do mercado e também as utilizadas dentro da empresa que forneceu o desafio.

O aplicativo permite ao usuário que visualize uma lista de filmes, procure e filtre filmes clicando nele (um Search e um filtro de categorai), consulte detalhes de um filme selecionado(um MovieDetal) e também que salve filmes como favoritos para consulta posterior(onde o usuário pode implementar filmes na listagem de favoritos em uma chamada da API para (ADD Favorites) e também possa remover da sua listagem de favoritos na hora que quiser).

Como a API já permitia adicionar filmes a listagem de favoritos, buscar esses filmes e remover, não foi necessária a implementação de DataBase.

### Stack (Tecnologias utilizadas no APP)

O aplicativo foi desenvolvido utilizando as seguintes tecnologias:

**Kotlin** → Linguagem principal para o desenvolvimento do Android.

**Corrotines e Flow** → Para gerenciar chamadas assíncronas de forma eficiente.

**Retrofit** → Para consumo e tratamento das requisições (HTTP).

**LiveData** → Para observação de dados e atualização da UI.

**XML** → Para construção da interface do usuário.

**Koin** → Para injeção de dependências.

**Coil** → Para carregamento eficiente de imagens.

**Paging 3** → Para facilitar a paginação.

### Requisitos do Aplicativo

O aplicativo contém as seguintes funcionalidades:

**Listagem de Filmes**

**Consume a API do The Movie DB e exibir os filmes.**

**Implementar paginação para carregar mais filmes conforme o usuário rola a tela.**

**Detalhes do Filme**

**Exibir título, sinopse, data de lançamento e imagem do filme ao selecioná-lo.**

**Filtragem**

**Pesquisa**

**Permitir ao usuário pesquisar filmes específicos.**

**Implementar filtros para refinar os resultados exibidos.**

### Bônus

**Implementações adicionais:**

**Exibição de Estados de Carregamento (Loading)** → Mostrar um indicador de carregamento durante a busca de dados.

**Tratamento de Erros** → Exibir mensagens corretas para erros de conexão, requisição inválida.

**Aprimoramento da Experiência do Usuário** → Criada uma interface intuitiva e agradável, seguindo princípios de design moderno e interação com o usuário.

**Favoritos** → Busca, salvamento e exclusão de cada item dentro da própria chamada da API do TMDB.

