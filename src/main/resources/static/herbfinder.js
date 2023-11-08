const SERVER_URL = 'http://localhost:8080';
document.getElementById("generateButton").addEventListener("click",generateText);

function generateText() {
    const userInput = document.getElementById("search-input").value;

    const apiUrl = 'https://api.openai.com/v1/chat/completions';

    const data = {
        prompt: userInput,
        max_tokens: 300
    };
    fetch(apiUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${API_KEY}`
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(result => {
            const generatedText = result.choices[0].text;
            document.getElementById("output").textContent = generatedText;
        })
        .catch(error => {
            console.error("API error", error)
        });
}

