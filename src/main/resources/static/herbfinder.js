const SERVER_URL = 'http://localhost:8080/api/';

document.getElementById('btn-get-herb').addEventListener('click', getHerb);


async function getHerb() {
    const URL = `${SERVER_URL}herbs?about=${document.getElementById('search-input').value}`
    const result = document.getElementById('result');
    const loadingSpinner = document.getElementById('loading-spinner');
    const loadingText = document.getElementById('loading-text');

    loadingSpinner.style.display = "block";
    loadingText.style.display = "block";
    try {
        const response = await fetch(URL).then(handleHttpErrors)
        document.getElementById('result').innerText = response.answer;
        result.style.backgroundColor = 'rgba(0, 128, 0, 0.7)';
        result.style.color = 'white';
        result.style.padding = '4rem';

    } catch (e) {
        result.style.color = "red";
        result.innerText = e.message;
    } finally {
        loadingSpinner.style.display = "none";
        loadingText.style.display = "none";
    }
}
async function handleHttpErrors(res) {
    if (!res.ok) {
        const errorResponse = await res.json();
        const msg = errorResponse.message ? errorResponse.message : "No error details provided"
        throw new Error(msg)
    }
    return res.json()
}

