# Выбор CLi библиотеки

Были рассмотрены следующие варианты:

* [Commons-CLI](http://commons.apache.org/proper/commons-cli/index.html)
* [args4j](http://args4j.kohsuke.org/)
* [JOpt simple](https://pholser.github.io/jopt-simple/index.html)
* [Google options](https://github.com/pcj/google-options)
* [Некоторые другие из этого прекрасного ответа](https://stackoverflow.com/questions/1200054/java-library-for-parsing-command-line-parameters)

Commons-CLI сразу же попался по top-ссылке гугла. Также, до этого, когда нужно было работать с аргументами в одном из дз, я также остановил свой выбор на этой библиотеке.
Она имеет достаточно straight-forward синтаксис и почти не нужно дополнительно о чем-то задумываться
На самом деле, остальные библиотеки рассматривались по принципу: "ну может быть еще как-то проще можно парсить или с какими-нибудь дополнительными фичами"

Я хотел бы использовать [args4j](http://args4j.kohsuke.org/), которая вроде и предлагает то же самое, но я застопорился на одном моменте и начал использовать [Commons-CLI](http://commons.apache.org/proper/commons-cli/index.html). На самом деле этот момент легко исправлялся, но код уже был написан :)
Поэтому вроде ничего не останавливает использовать именно ее, потому что она значения аргументов опций сразу же записывает в переменные. Думаю кода обработки CLI будет примерно столько же сколько и с [Commons-CLI](http://commons.apache.org/proper/commons-cli/index.html).

Остальные библиотеки на самом деле не предлагали ничего нового или интересного. Каждая из них либо сомнительна и непонятно кем написаная(потому предыдущие две все же в топ-поиске находятся) либо предлагают если не такой же, то немного более verbose-ный интерфейс