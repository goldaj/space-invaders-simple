(function(root,factory){const api=factory();if(typeof module==="object"&&module.exports)module.exports=api;if(root)root.Invaders=api;})(typeof globalThis!=="undefined"?globalThis:this,function(){
"use strict";
const W=480,H=700;
function rng(seed){let x=(seed>>>0)||1;return()=>{x^=x<<13;x^=x>>>17;x^=x<<5;return(x>>>0)/4294967296;};}
function bounds(a,b){return Math.abs(a.x-b.x)<(a.w+b.w)/2&&Math.abs(a.y-b.y)<(a.h+b.h)/2;}
function createGame(seed=7){
 let random=rng(seed);const g={width:W,height:H,score:0,lives:3,level:1,mode:"playing",player:{x:W/2,y:635,w:46,h:24},shots:[],enemyShots:[],enemies:[],shields:[],items:[],direction:1,shotTimer:0,enemyTimer:.8,rapid:0,invulnerable:0};
 function wave(){
  g.enemies=[];g.shots=[];g.enemyShots=[];g.direction=1;g.enemyTimer=.7;
  for(let row=0;row<4;row++)for(let col=0;col<6;col++)
   g.enemies.push({x:60+col*70,y:120+row*45,w:34,h:24,row});
 }
 function reset(){
  g.score=0;g.lives=3;g.level=1;g.mode="playing";g.items=[];g.rapid=0;g.invulnerable=0;g.player.x=W/2;g.shields=[];
  for(const x of [105,240,375])g.shields.push({x,y:570,w:52,h:20,hp:5});
  wave();
 }
 function move(x){if(Number.isFinite(x))g.player.x=Math.min(W-28,Math.max(28,x));}
 function togglePause(){if(g.mode==="playing")g.mode="paused";else if(g.mode==="paused")g.mode="playing";return g.mode;}
 function hitPlayer(){if(g.invulnerable>0)return;g.lives--;g.invulnerable=1.3;g.enemyShots=[];if(g.lives<=0)g.mode="gameover";}
 function step(dt){
  if(g.mode!=="playing")return;
  if(!Number.isFinite(dt)||dt<0)throw new RangeError("Invalid delta");dt=Math.min(dt,.05);
  g.rapid=Math.max(0,g.rapid-dt);g.invulnerable=Math.max(0,g.invulnerable-dt);
  g.shotTimer-=dt;
  if(g.shotTimer<=0){
   g.shots.push({x:g.player.x,y:g.player.y-18,w:5,h:15,vy:-430});
   if(g.rapid>0)for(const offset of [-14,14])g.shots.push({x:g.player.x+offset,y:g.player.y-18,w:5,h:15,vy:-430});
   g.shotTimer=g.rapid>0?.17:.32;
  }
  const speed=Math.min(112,25+g.level*7);
  let left=Infinity,right=-Infinity;
  for(const e of g.enemies){left=Math.min(left,e.x-e.w/2);right=Math.max(right,e.x+e.w/2);}
  if(g.enemies.length && (g.direction>0&&right+speed*dt>W-13||g.direction<0&&left-speed*dt<13)){
   g.direction*=-1;
   for(const e of g.enemies)e.y+=19;
  }else for(const e of g.enemies)e.x+=g.direction*speed*dt;
  if(g.enemies.some(e=>e.y+e.h/2>=g.player.y-30)){hitPlayer();if(g.mode==="playing")wave();}
  g.enemyTimer-=dt;
  if(g.enemyTimer<=0&&g.enemies.length){
   const candidates=g.enemies.filter(e=>!g.enemies.some(other=>other!==e&&other.y>e.y&&Math.abs(e.x-other.x)<28));
   const source=candidates[Math.floor(random()*candidates.length)];
   g.enemyShots.push({x:source.x,y:source.y+17,w:7,h:14,vy:150+g.level*11});
   g.enemyTimer=Math.max(.33,1.35-g.level*.06)+random()*.45;
  }
  function updateShots(shots){
   for(let i=shots.length-1;i>=0;i--){const b=shots[i];b.y+=b.vy*dt;if(b.y<0||b.y>H)shots.splice(i,1);}
  }
  updateShots(g.shots);updateShots(g.enemyShots);
  for(let i=g.shots.length-1;i>=0;i--){
   const b=g.shots[i];const target=g.enemies.find(e=>bounds(b,e));
   if(target){g.enemies.splice(g.enemies.indexOf(target),1);g.shots.splice(i,1);g.score+=10*(4-target.row)*g.level;
    if(random()<.08)g.items.push({x:target.x,y:target.y,w:18,h:18,type:random()<.45?"heal":"rapid"});
   }
  }
  for(let i=g.enemyShots.length-1;i>=0;i--){
   const b=g.enemyShots[i];const shield=g.shields.find(s=>s.hp>0&&bounds(b,s));
   if(shield){shield.hp--;g.enemyShots.splice(i,1);continue;}
   if(bounds(b,g.player)){g.enemyShots.splice(i,1);hitPlayer();}
  }
  for(let i=g.items.length-1;i>=0;i--){
   const item=g.items[i];item.y+=92*dt;
   if(item.y>H){g.items.splice(i,1);continue;}
   if(bounds(item,g.player)){
    if(item.type==="heal")g.lives=Math.min(5,g.lives+1);
    else g.rapid=8;
    g.items.splice(i,1);
   }
  }
  if(!g.enemies.length&&g.mode==="playing"){g.score+=250*g.level;g.level++;wave();g.shields.forEach(s=>s.hp=Math.min(5,s.hp+1));}
 }
 reset();
 return Object.freeze({state:g,reset,move,step,togglePause,hitPlayer});
}
return Object.freeze({W,H,createGame,rng,bounds});
});