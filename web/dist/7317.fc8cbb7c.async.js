(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[7317],{7317:function(u,r,R){"use strict";R.r(r),R.d(r,{forth:function(){return S}});function O(n){var i=[];return n.split(" ").forEach(function(E){i.push({name:E})}),i}var L=O("INVERT AND OR XOR 2* 2/ LSHIFT RSHIFT 0= = 0< < > U< MIN MAX 2DROP 2DUP 2OVER 2SWAP ?DUP DEPTH DROP DUP OVER ROT SWAP >R R> R@ + - 1+ 1- ABS NEGATE S>D * M* UM* FM/MOD SM/REM UM/MOD */ */MOD / /MOD MOD HERE , @ ! CELL+ CELLS C, C@ C! CHARS 2@ 2! ALIGN ALIGNED +! ALLOT CHAR [CHAR] [ ] BL FIND EXECUTE IMMEDIATE COUNT LITERAL STATE ; DOES> >BODY EVALUATE SOURCE >IN <# # #S #> HOLD SIGN BASE >NUMBER HEX DECIMAL FILL MOVE . CR EMIT SPACE SPACES TYPE U. .R U.R ACCEPT TRUE FALSE <> U> 0<> 0> NIP TUCK ROLL PICK 2>R 2R@ 2R> WITHIN UNUSED MARKER I J TO COMPILE, [COMPILE] SAVE-INPUT RESTORE-INPUT PAD ERASE 2LITERAL DNEGATE D- D+ D0< D0= D2* D2/ D< D= DMAX DMIN D>S DABS M+ M*/ D. D.R 2ROT DU< CATCH THROW FREE RESIZE ALLOCATE CS-PICK CS-ROLL GET-CURRENT SET-CURRENT FORTH-WORDLIST GET-ORDER SET-ORDER PREVIOUS SEARCH-WORDLIST WORDLIST FIND ALSO ONLY FORTH DEFINITIONS ORDER -TRAILING /STRING SEARCH COMPARE CMOVE CMOVE> BLANK SLITERAL"),T=O("IF ELSE THEN BEGIN WHILE REPEAT UNTIL RECURSE [IF] [ELSE] [THEN] ?DO DO LOOP +LOOP UNLOOP LEAVE EXIT AGAIN CASE OF ENDOF ENDCASE");function t(n,i){var E;for(E=n.length-1;E>=0;E--)if(n[E].name===i.toUpperCase())return n[E]}const S={name:"forth",startState:function(){return{state:"",base:10,coreWordList:L,immediateWordList:T,wordList:[]}},token:function(n,i){var E;if(n.eatSpace())return null;if(i.state===""){if(n.match(/^(\]|:NONAME)(\s|$)/i))return i.state=" compilation","builtin";if(E=n.match(/^(\:)\s+(\S+)(\s|$)+/),E)return i.wordList.push({name:E[2].toUpperCase()}),i.state=" compilation","def";if(E=n.match(/^(VARIABLE|2VARIABLE|CONSTANT|2CONSTANT|CREATE|POSTPONE|VALUE|WORD)\s+(\S+)(\s|$)+/i),E)return i.wordList.push({name:E[2].toUpperCase()}),"def";if(E=n.match(/^(\'|\[\'\])\s+(\S+)(\s|$)+/),E)return"builtin"}else{if(n.match(/^(\;|\[)(\s)/))return i.state="",n.backUp(1),"builtin";if(n.match(/^(\;|\[)($)/))return i.state="","builtin";if(n.match(/^(POSTPONE)\s+\S+(\s|$)+/))return"builtin"}if(E=n.match(/^(\S+)(\s+|$)/),E)return t(i.wordList,E[1])!==void 0?"variable":E[1]==="\\"?(n.skipToEnd(),"comment"):t(i.coreWordList,E[1])!==void 0?"builtin":t(i.immediateWordList,E[1])!==void 0?"keyword":E[1]==="("?(n.eatWhile(function(e){return e!==")"}),n.eat(")"),"comment"):E[1]===".("?(n.eatWhile(function(e){return e!==")"}),n.eat(")"),"string"):E[1]==='S"'||E[1]==='."'||E[1]==='C"'?(n.eatWhile(function(e){return e!=='"'}),n.eat('"'),"string"):E[1]-68719476735?"number":"atom"}}}}]);