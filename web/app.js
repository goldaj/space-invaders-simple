(function(){
"use strict";
const canvas=document.getElementById("screen"),ctx=canvas.getContext("2d"),game=Invaders.createGame();
const $=id=>document.getElementById(id);
let heldLeft=false,heldRight=false,high=0,previous=0;
try{high=Math.max(0,Number(localStorage.getItem("invaders-high-v1"))||0);}catch(e){}
function message(s){$("status").textContent=s;}
function movePointer(e){const rect=canvas.getBoundingClientRect();game.move((e.clientX-rect.left)*Invaders.W/rect.width);}
canvas.addEventListener("pointerdown",e=>{canvas.setPointerCapture(e.pointerId);movePointer(e);});
canvas.addEventListener("pointermove",e=>{if(e.buttons||e.pointerType==="touch")movePointer(e);});
document.addEventListener("keydown",e=>{
 if(["ArrowLeft","ArrowRight"," ","p","P","r","R"].includes(e.key))e.preventDefault();
 if(["ArrowLeft","a","q"].includes(e.key))heldLeft=true;
 if(["ArrowRight","d"].includes(e.key))heldRight=true;
 if(e.key===" "||e.key.toLowerCase()==="p"){game.togglePause();message("Pause : "+game.state.mode);}
 if(e.key.toLowerCase()==="r"){game.reset();message("Nouvelle partie.");}
});
document.addEventListener("keyup",e=>{if(["ArrowLeft","a","q"].includes(e.key))heldLeft=false;if(["ArrowRight","d"].includes(e.key))heldRight=false;});
$("pause").onclick=()=>{game.togglePause();message("État : "+game.state.mode);};
$("restart").onclick=()=>{game.reset();message("Nouvelle partie.");};
function draw(){
 const s=game.state;
 ctx.fillStyle="#04081c";ctx.fillRect(0,0,Invaders.W,Invaders.H);
 ctx.fillStyle="#374264";for(let i=0;i<85;i++){const x=(i*137+19)%Invaders.W,y=(i*97+11)%Invaders.H;ctx.fillRect(x,y,2,2);}
 for(const shield of s.shields){if(shield.hp<=0)continue;ctx.fillStyle="#37928e";ctx.fillRect(shield.x-shield.w/2,shield.y-shield.h/2,shield.w*shield.hp/5,shield.h);}
 for(const e of s.enemies){ctx.fillStyle=["#ff7bba","#fac577","#7fdaec","#82b8ff"][e.row];ctx.fillRect(e.x-e.w/2,e.y-e.h/2,e.w,e.h);ctx.fillStyle="#07152e";ctx.fillRect(e.x-8,e.y-2,5,5);ctx.fillRect(e.x+4,e.y-2,5,5);}
 for(const b of s.shots){ctx.fillStyle="#8cffe3";ctx.fillRect(b.x-b.w/2,b.y-b.h/2,b.w,b.h);}
 for(const b of s.enemyShots){ctx.fillStyle="#ff6378";ctx.fillRect(b.x-b.w/2,b.y-b.h/2,b.w,b.h);}
 for(const item of s.items){ctx.fillStyle=item.type==="heal"?"#59efb8":"#ffd16a";ctx.fillRect(item.x-9,item.y-9,18,18);}
 if(s.invulnerable<=0||Math.floor(s.invulnerable*10)%2===0){ctx.fillStyle="#54f9b6";ctx.beginPath();ctx.moveTo(s.player.x,s.player.y-26);ctx.lineTo(s.player.x+24,s.player.y+15);ctx.lineTo(s.player.x-24,s.player.y+15);ctx.closePath();ctx.fill();}
 ctx.font="bold 21px monospace";ctx.fillStyle="#f6f1ff";ctx.fillText("SCORE "+s.score,14,35);ctx.fillText("VIES "+s.lives,345,35);
 ctx.font="18px monospace";ctx.fillStyle="#8ccfff";ctx.fillText("VAGUE "+s.level,190,68);ctx.fillText("RECORD "+high,14,65);
 if(s.rapid>0){ctx.fillStyle="#ffd16a";ctx.fillText("TIR RAPIDE "+s.rapid.toFixed(0)+"s",155,96);}
 if(s.mode!=="playing"){ctx.fillStyle="#000b";ctx.fillRect(0,0,480,700);ctx.textAlign="center";ctx.font="bold 38px sans-serif";ctx.fillStyle="#fff";ctx.fillText(s.mode==="paused"?"PAUSE":"GAME OVER",240,340);ctx.font="20px sans-serif";ctx.fillText("P pour reprendre / R pour rejouer",240,385);ctx.textAlign="start";}
 $("score").textContent=s.score;$("lives").textContent=s.lives;$("wave").textContent=s.level;$("record").textContent=high;
}
function frame(timestamp){
 const dt=previous?Math.min(.05,(timestamp-previous)/1000):0;previous=timestamp;
 if(game.state.mode==="playing"&&(heldLeft||heldRight))game.move(game.state.player.x+(Number(heldRight)-Number(heldLeft))*260*dt);
 game.step(dt);
 if(game.state.score>high){high=game.state.score;try{localStorage.setItem("invaders-high-v1",String(high));}catch(e){}}
 draw();requestAnimationFrame(frame);
}
requestAnimationFrame(frame);
})();
