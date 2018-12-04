const fetch = require("node-fetch");

(async function() {
    const time = Date.now();
    const results = (await Promise.all(
        new Array(30).fill().map(async () => {
            try {
                const beginTime = Date.now();
                const r = await fetch("http://132.207.12.217:8080/");
                console.log(`Temps de la requête: ${(Date.now() - beginTime) / 1000} secondes.`)
                console.log(await r.text());
            } catch(e) {
                console.log("Échec de la requête.")
            }
        })
    ));
    
    console.log(`Total time: ${(Date.now() - time) / 1000} secondes.`);
})();
