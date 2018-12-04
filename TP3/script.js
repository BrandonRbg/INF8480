const fetch = require("node-fetch");

(async function() {
    const time = Date.now();
    const results = (await Promise.all(
        new Array(30).fill(0).map(
            async () => fetch("http://132.207.12.213:8080/")
            .then(r => r.text())
            .then(res => console.log(res))
        )
    ));
    
    console.log(`Total time: ${(Date.now() - time) / 1000} secondes.`);
})();
