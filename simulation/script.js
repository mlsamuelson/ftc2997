var canvas = document.getElementById("simulation_canvas");
var ctx = canvas.getContext("2d");
var width = canvas.width, height = canvas.height;

var bot = {
    color: "blue",
    score: 0,
    size: 75,
    angle: 270,
    r_angle: this.angle*Math.PI/180,
    x: 562.5,
    y: 350,
    cx: 0,
    cy: 0,
    pos: [],
    setPos: function(){
        this.r_angle = this.angle*Math.PI/180;
        
        this.pos = [];
        this.pos.push([this.x - 37.5, this.y - 37.5]);
        this.pos.push([this.x + 37.5, this.y - 37.5]);
        this.pos.push([this.x + 37.5, this.y + 37.5]);
        this.pos.push([this.x - 37.5, this.y + 37.5]);
        
        this.cx = (this.pos[2][0] + this.pos[3][0])/2;
        this.cy = (this.pos[2][1] + this.pos[3][1])/2;
    },
    turn: function(direction){
        this.angle += direction;
        this.r_angle = this.angle*Math.PI/180;
        var sinTheta = Math.sin(this.r_angle);
        var cosTheta = Math.cos(this.r_angle);
        for (var n in this.pos){
            var x = this.pos[n][0];
            var y = this.pos[n][1];
            this.pos[n][0] = cosTheta*(x-this.x) + sinTheta*(y-this.y) + this.x;
            this.pos[n][1] = sinTheta*(x-this.x) - cosTheta*(y-this.y) + this.y;
        }
        x = this.cx;
        y = this.cy+2;
        this.cx = cosTheta*(x-this.x) + sinTheta*(y-this.y) + this.x;
        this.cy = sinTheta*(x-this.x) - cosTheta*(y-this.y) + this.y;
    },
    move: function(direction){
        var sinTheta = Math.sin(this.r_angle);
        var cosTheta = Math.cos(this.r_angle);
        this.y -= cosTheta*direction;
        this.x += sinTheta*direction;
    },
    draw: function(){
        ctx.fillStyle = "#551A8B";
        ctx.strokeStyle = "black";
        
        ctx.beginPath();
        ctx.moveTo(this.pos[0][0], this.pos[0][1]);
        ctx.lineTo(this.pos[1][0], this.pos[1][1]);
        ctx.lineTo(this.pos[2][0], this.pos[2][1]);
        ctx.lineTo(this.pos[3][0], this.pos[3][1]);
        ctx.lineTo(this.pos[0][0], this.pos[0][1]);
        ctx.lineWidth = 2;
        ctx.fill();
        ctx.stroke();
        
        ctx.beginPath();
        ctx.moveTo(this.pos[2][0], this.pos[2][1]);
        ctx.lineTo(this.pos[3][0], this.pos[3][1]);
        ctx.lineWidth = 3;
        ctx.stroke();
    },
    launch: function(){
        console.log("PEW!!!");
    },
    pressBeacon: function(){
        console.log("LAUNCH!!!");
    },
    checkColor: function(){
        var imgDat = ctx.getImageData(this.cx, this.cy, 1, 1);
        var r = imgDat.data[0];
        var g = imgDat.data[1];
        var b = imgDat.data[2];
        if (r > b) {
            return "red";
        } else if (b > r) {
            return "blue";
        } else if (b == r && b == g) {
            if (b == 0) {
                return "black";
            } else if (b == 255) {
                return "white";
            } else {
                return "gray";
            }
        } else {
            return "green";
        }
    }
};

function drawBackground(){
    ctx.fillStyle = "gray";
    ctx.fillRect(0, 0, width, height);
    
    ctx.strokeStyle = "black";
    for (var X = 0; X <= width; X += 100) {
        for (var Y = 0; Y <= height; Y += 100) {
            ctx.beginPath();
            ctx.moveTo(X, 0);
            ctx.lineTo(X, height);
            ctx.closePath();
            ctx.stroke();
            
            ctx.beginPath();
            ctx.moveTo(0, Y);
            ctx.lineTo(width, Y);
            ctx.closePath();
            ctx.stroke();
        }
    }

    ctx.lineWidth = 8;
    
    ctx.strokeStyle = "white";
    for (X = -50; X < width-100; X += 200) {
        ctx.beginPath();
        ctx.moveTo(X, height);
        ctx.lineTo(X, height-100);
        ctx.stroke();
        
        ctx.beginPath();
        ctx.moveTo(0, X+100);
        ctx.lineTo(100, X+100);
        ctx.stroke();
    }
    
    ctx.fillStyle = "black";
    
    ctx.strokeStyle = "red";
    ctx.beginPath();
    ctx.moveTo(width/2+54, height/2-54);
    ctx.lineTo(width/2-54, height/2-54);
    ctx.lineTo(width/2-54, height/2+54);
    ctx.closePath();
    ctx.fill();
    ctx.stroke();
    
    ctx.strokeStyle = "blue";
    ctx.beginPath();
    ctx.moveTo(width/2-54, height/2+54);
    ctx.lineTo(width/2+54, height/2+54);
    ctx.lineTo(width/2+54, height/2-54);
    ctx.closePath();
    ctx.fill();
    ctx.stroke();
    
    ctx.strokeStyle = "red";
    ctx.beginPath();
    ctx.moveTo(-5, height);
    ctx.lineTo(width, -5);
    ctx.stroke();
    
    ctx.strokeStyle = "blue";
    ctx.beginPath();
    ctx.moveTo(+5, height);
    ctx.lineTo(width, +5);
    ctx.stroke();
    
    ctx.fillRect(width/2-50, height/2-50, 100, 100);
    
    ctx.fillStyle = "red";
    ctx.beginPath();
    ctx.moveTo(0, 0);
    ctx.lineTo(150, 0);
    ctx.lineTo(0, 150);
    ctx.closePath();
    ctx.fill();
    
    ctx.fillStyle = "blue";
    ctx.beginPath();
    ctx.moveTo(width, height);
    ctx.lineTo(width-150, height);
    ctx.lineTo(width, height-150);
    ctx.closePath();
    ctx.fill();
}

// TRY IT WITHOUT THE MAIN LOOP (LINEAR)
/*setInterval(function() {
    ctx.clearRect(0, 0, width, height);
    drawBackground();
    
    STATE = paths[path][path_pos];
    
    bot.setPos();
    bot.turn();
    bot.draw();
    
    switch(STATE){
        case "BEGIN1":{
            bot.x = 562.5;
            bot.y = 350;
            bot.angle = 270;
            bot.pew = 0;
            path_pos ++;
            break;}
        case "BEACON1":{
            switch (num) {
                case 1:{
                    bot.move(1, 0);
                    if (bot.checkColor() == bot.color){
                        setInterval(function(){
                            setTimeout(bot.move(1, 0), 33.3)}, 1000/30);
                        bot.angle -= 90;
                        num = 2;
                    }
                    break;}
                case 2:{
                    bot.move(1, 0);
                    if (bot.checkColor() == "white"){
                        bot.move(99, 0);
                        bot.pressBeacon();
                        path_pos ++;
                    }
                }
            }
            break;}
        case "BEACON2":{
            
            break;}
        case "CENTER":{
            
            break;}
        case "LAUNCH":{
            bot.launch();
            break;}
    }
    //console.log(STATE);
    console.log(bot.checkColor());
}, 1000/30);*/
function BEGIN1(){
    console.log("BEGIN1");
    ctx.clearRect(0, 0, width, height);
    drawBackground();
    if (bot.checkColor() == bot.color){
        path_pos ++;
        console.log("SWITCH!");
    }
    bot.setPos();
    bot.turn(0);
    bot.move(1);
    bot.draw();
}
function BEACON1(){
    console.log("BEACON1");
    ctx.clearRect(0, 0, width, height);
    drawBackground();
}
function BEACON2(){
    console.log("BEACON2");
    ctx.clearRect(0, 0, width, height);
    drawBackground();
}
function CENTER(){
    console.log("CENTER");
    ctx.clearRect(0, 0, width, height);
    drawBackground();
}
function LAUNCH(){
    console.log("LAUNCH");
    ctx.clearRect(0, 0, width, height);
    drawBackground();
}

var path = 0;
var path_pos = 0;
var path1 = [BEGIN1, BEACON1, CENTER, LAUNCH];
var path2 = [BEGIN1, BEACON1, BEACON2, LAUNCH];
var paths = [path1, path2];

var loop = setInterval(paths[path][path_pos], 100/3);