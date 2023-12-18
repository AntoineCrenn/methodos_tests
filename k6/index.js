import http from 'k6/http';
import {Rate} from 'k6/metrics';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/2.4.0/dist/bundle.js";
import { textSummary } from "https://jslib.k6.io/k6-summary/0.0.1/index.js";

const failureRate = new Rate('failed requests');
const requetes = JSON.parse(open('./tests/requete.json'));
const donnees = JSON.parse(open('./tests/donnnees.json'));


// https://k6.io/docs/get-started/running-k6/
export function test_api_endpoints_config() {
    let requete = requetes[Math.floor(Math.random() * requetes.length)];
    if (requete.type === 'GET') {
        let resultat = http.get(`http://localhost:8080/livres`);
        failureRate.add(resultat.status !== 200);
    } else {
        let resultat = http.post(
            `http://localhost:8080/livres`,
            `
            {
                "titre": "${requete.titre}",
                "auteur": "${requete.auteur}"
            }
            `,
            {
                headers: {
                    'Content-Type': 'application/json'
                }
            }
        );
        failureRate.add(resultat.status !== 201);
    }
}

export function setup() {
    console.log("Setup started");

    donnees.forEach(item =>
        http.post(
            `http://localhost:8080/livres`,
            JSON.stringify(item),
            {
                headers: {
                    'Content-Type': 'application/json'
                }
            }
        )
    )

    console.log("Setup ended");
}

export function handleSummary(data) {
    return {
        "summary.html": htmlReport(data),
        stdout: textSummary(data, { indent: " ", enableColors: true }),
    };
}