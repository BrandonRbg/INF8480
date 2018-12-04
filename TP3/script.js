const fetch = require("node-fetch");

(async function () {
    const time = Date.now();
    const results = (await Promise.all(
        new Array(30).fill().map(async () => {
            const beginTime = Date.now();
            const r = await fetch("http://132.207.12.217:8080/")
                .catch((err) => console.log(err.message));
            console.log(`Temps de la requête: ${(Date.now() - beginTime) / 1000} secondes.`)
            console.log(await r.text());
        })
    ));

    console.log(`Total time: ${(Date.now() - time) / 1000} secondes.`);
})();
