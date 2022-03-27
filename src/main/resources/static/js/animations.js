function fadeInAnimation(element){
    anime({
        targets: element,
        opacity: ['0','1'],
        duration: 700,
        scale: ["0%", "100%"],
        easing: 'easeInOutSine'
    })
}

function startTimerAnimation(timer){
    anime({
        targets: timer,
        easing: 'linear',
        direction: "alternate",
        duration: 2000,
        scale: ["125%", "100%"],
        loop: true
    })
}

function startLetterEnteredAnimation(targetElement){
    anime({
        targets:  targetElement,
        scale: ["100%", "120%"],
        direction: "alternate",
        easing: 'easeInOutSine',
        duration: 250

    })
}

function startRotateGridSabotage(target){
    anime({
        targets: target,
        duration: 5000,
        easing: "linear",
        rotate: '1turn',
        loop: true
    })
}
