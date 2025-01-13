document.addEventListener("DOMContentLoaded", function () {
    const buttons = document.querySelectorAll(".freeze-button");

    buttons.forEach((button) => {
        button.addEventListener("click", function (event) {
            const clickedButton = event.target;

            // Блокируем кнопку
            clickedButton.disabled = true;

            // Разблокировка через 3 секунды
            setTimeout(() => {
                clickedButton.disabled = false;
            }, 3000); // Время заморозки в миллисекундах
        });
    });
});
