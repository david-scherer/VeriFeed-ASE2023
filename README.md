# VeriFeed

## Development setup

Code quality pre-commit is ensured through linters and formatters such as Prettier, ESLint, black, and Pylint. These need to be installed within the directories of the services (through virtualenvs in Python, node_modules in frontend, etc.).

In order to prepare the directories, do the following:

- For the services [extraction](./extraction), [recognition](./recognition), and [scraping_mastodon](./scraping_mastodon), follow the instructions in their respective READMEs.

- [Install](https://github.com/evilmartians/lefthook/blob/master/docs/install.md) [lefthook](https://github.com/evilmartians/lefthook) (ideally globally)
- Run `lefthook install` to install the pre-commit hook

After that, the hook should be run whenever you commit code changes and should inform you about any linting errors. Formatting is performed automatically.

Regarding the Java backend, download and enable the [SonarLint](https://plugins.jetbrains.com/plugin/7973-sonarlint) extension and run with the default settings for Java enabled.

## Run Configuration

1) First create the mastodon network with `docker network create mastodon-instance_mastonet`.
   This network is used to connect the mastodon instance with the scraping service.

2) Then if needed start the mastodon instance with `docker-compose -f mastodon-instance/docker-compose.yml up` in the root directory.

3) At the end start VeriFeed with `docker-compose up` in the root directory.

### Run Configuration MR2 - Merlin Zalodek
start_all.sh

npm start (in frontend)

login in on http://localhost:3000/
with admin@localhost mastodonadmin

curl http://localhost:5003/api/v1/mastodon/scrape


## Frontend
### Initialization
In the [frontend](./frontend) directory, run `npm install`

### Angular
- [Link](https://angular.io/docs) to the documentation

### Tailwind CSS
- [Tailwind UI](https://tailwindui.com/)
- [Free Components from Hyperui](https://www.hyperui.dev/compenents)
- [SVG Icons](https://iconsvg.xyz)
- [Flexbox Help](https://angrytools.com/css-flex/)

## QuickFix
### Docker on Mac
Run `sudo chown -R $(id -u):$(id -g) $HOME/.docker` if permission error
