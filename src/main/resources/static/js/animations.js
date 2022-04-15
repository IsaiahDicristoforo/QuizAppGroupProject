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

function flashScreenSabotage(target){

    anime({
        loop: true,
        targets: target,
        duration: 300,
        opacity: ['100%', '0%'],
        direction: 'alternate'
    })

}

function startSabotageNotificationAnimation(sabotuer, sabotageType, target){
    if(sabotageType == 'RotatingGrid'){
        $("#gameMessage").text("You were sabotaged by " + sabotuer)
        startRotateGridSabotage($("#wordleGridContainer").get())
    }
    else if (sabotageType == 'FreezeScreen'){
        flashScreenSabotage($("#wordleGridContainer").get())
    }

    anime({
        targets: target,
        duration: 1500,
        scale: [0, 1],
        background: "rgb(245, 78, 66)",
        opacity: [0,1],
        width: [0, '50%'],
        easing: 'easeInOutSine'

    })

}
