(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[77651],{5953:function(Ni,Ui,nr){var rr=nr(48764).Buffer;(function(Ke,tn){Ni.exports=tn()})(self,()=>(()=>{var Ke={916:(q,j,Z)=>{var le=Z(471);q.exports=function(ce){"use strict";var ee,R="",ie=(ce=ce||{}).video,se=ce.options,V=le.$escape,ue=ce.tran,ve=ce.icons,de=ce.index,Se=le.$each;return ce.$value,ce.$index,R+=`<div class="dplayer-mask"></div>
<div class="dplayer-video-wrap">
    `,ee=Z(568)(ie),R+=ee,R+=`
    `,se.logo&&(R+=`
    <div class="dplayer-logo">
        <img src="`,R+=V(se.logo),R+=`">
    </div>
    `),R+=`
    <div class="dplayer-danmaku"`,se.danmaku&&se.danmaku.bottom&&(R+=' style="margin-bottom:',R+=V(se.danmaku.bottom),R+='"'),R+=`>
        <div class="dplayer-danmaku-item dplayer-danmaku-item--demo"></div>
    </div>
    <div class="dplayer-subtitle"></div>
    <div class="dplayer-bezel">
        <span class="dplayer-bezel-icon"></span>
        `,se.danmaku&&(R+=`
        <span class="dplayer-danloading">`,R+=V(ue("danmaku-loading")),R+=`</span>
        `),R+=`
        <span class="diplayer-loading-icon">`,R+=ve.loading,R+=`</span>
    </div>
</div>
<div class="dplayer-controller-mask"></div>
<div class="dplayer-controller">
    <div class="dplayer-icons dplayer-comment-box">
        <button class="dplayer-icon dplayer-comment-setting-icon" data-balloon="`,R+=V(ue("setting")),R+=`" data-balloon-pos="up">
            <span class="dplayer-icon-content">`,R+=ve.pallette,R+=`</span>
        </button>
        <div class="dplayer-comment-setting-box">
            <div class="dplayer-comment-setting-color">
                <div class="dplayer-comment-setting-title">`,R+=V(ue("set-danmaku-color")),R+=`</div>
                <label>
                    <input type="radio" name="dplayer-danmaku-color-`,R+=V(de),R+=`" value="#fff" checked>
                    <span style="background: #fff;"></span>
                </label>
                <label>
                    <input type="radio" name="dplayer-danmaku-color-`,R+=V(de),R+=`" value="#e54256">
                    <span style="background: #e54256"></span>
                </label>
                <label>
                    <input type="radio" name="dplayer-danmaku-color-`,R+=V(de),R+=`" value="#ffe133">
                    <span style="background: #ffe133"></span>
                </label>
                <label>
                    <input type="radio" name="dplayer-danmaku-color-`,R+=V(de),R+=`" value="#64DD17">
                    <span style="background: #64DD17"></span>
                </label>
                <label>
                    <input type="radio" name="dplayer-danmaku-color-`,R+=V(de),R+=`" value="#39ccff">
                    <span style="background: #39ccff"></span>
                </label>
                <label>
                    <input type="radio" name="dplayer-danmaku-color-`,R+=V(de),R+=`" value="#D500F9">
                    <span style="background: #D500F9"></span>
                </label>
            </div>
            <div class="dplayer-comment-setting-type">
                <div class="dplayer-comment-setting-title">`,R+=V(ue("set-danmaku-type")),R+=`</div>
                <label>
                    <input type="radio" name="dplayer-danmaku-type-`,R+=V(de),R+=`" value="1">
                    <span>`,R+=V(ue("top")),R+=`</span>
                </label>
                <label>
                    <input type="radio" name="dplayer-danmaku-type-`,R+=V(de),R+=`" value="0" checked>
                    <span>`,R+=V(ue("rolling")),R+=`</span>
                </label>
                <label>
                    <input type="radio" name="dplayer-danmaku-type-`,R+=V(de),R+=`" value="2">
                    <span>`,R+=V(ue("bottom")),R+=`</span>
                </label>
            </div>
        </div>
        <input class="dplayer-comment-input" type="text" placeholder="`,R+=V(ue("input-danmaku-enter")),R+=`" maxlength="30">
        <button class="dplayer-icon dplayer-send-icon" data-balloon="`,R+=V(ue("send")),R+=`" data-balloon-pos="up">
            <span class="dplayer-icon-content">`,R+=ve.send,R+=`</span>
        </button>
    </div>
    <div class="dplayer-icons dplayer-icons-left">
        <button class="dplayer-icon dplayer-play-icon">
            <span class="dplayer-icon-content">`,R+=ve.play,R+=`</span>
        </button>
        <div class="dplayer-volume">
            <button class="dplayer-icon dplayer-volume-icon">
                <span class="dplayer-icon-content">`,R+=ve.volumeDown,R+=`</span>
            </button>
            <div class="dplayer-volume-bar-wrap" data-balloon-pos="up">
                <div class="dplayer-volume-bar">
                    <div class="dplayer-volume-bar-inner" style="background: `,R+=V(se.theme),R+=`;">
                        <span class="dplayer-thumb" style="background: `,R+=V(se.theme),R+=`"></span>
                    </div>
                </div>
            </div>
        </div>
        <span class="dplayer-time">
            <span class="dplayer-ptime">0:00</span> /
            <span class="dplayer-dtime">0:00</span>
        </span>
        `,se.live&&(R+=`
        <span class="dplayer-live-badge"><span class="dplayer-live-dot" style="background: `,R+=V(se.theme),R+=';"></span>',R+=V(ue("live")),R+=`</span>
        `),R+=`
    </div>
    <div class="dplayer-icons dplayer-icons-right">
        `,se.video.quality&&(R+=`
        <div class="dplayer-quality">
            <button class="dplayer-icon dplayer-quality-icon">`,R+=V(se.video.quality[se.video.defaultQuality].name),R+=`</button>
            <div class="dplayer-quality-mask">
                <div class="dplayer-quality-list">
                `,Se(se.video.quality,function(ge,st){R+=`
                    <div class="dplayer-quality-item" data-index="`,R+=V(st),R+='">',R+=V(ge.name),R+=`</div>
                `}),R+=`
                </div>
            </div>
        </div>
        `),R+=`
        `,se.screenshot&&(R+=`
        <div class="dplayer-icon dplayer-camera-icon" data-balloon="`,R+=V(ue("screenshot")),R+=`" data-balloon-pos="up">
            <span class="dplayer-icon-content">`,R+=ve.camera,R+=`</span>
        </div>
        `),R+=`
        `,se.airplay&&(R+=`
        <div class="dplayer-icon dplayer-airplay-icon" data-balloon="`,R+=V(ue("airplay")),R+=`" data-balloon-pos="up">
            <span class="dplayer-icon-content">`,R+=ve.airplay,R+=`</span>
        </div>
        `),R+=`
        `,se.chromecast&&(R+=`
        <div class="dplayer-icon dplayer-chromecast-icon" data-balloon="`,R+=V(ue("chromecast")),R+=`" data-balloon-pos="up">
            <span class="dplayer-icon-content">`,R+=ve.chromecast,R+=`</span>
        </div>
        `),R+=`
        <div class="dplayer-comment">
            <button class="dplayer-icon dplayer-comment-icon" data-balloon="`,R+=V(ue("send-danmaku")),R+=`" data-balloon-pos="up">
                <span class="dplayer-icon-content">`,R+=ve.comment,R+=`</span>
            </button>
        </div>
        `,se.subtitle&&(R+=`
        `,typeof se.subtitle.url=="string"?(R+=`
        <div class="dplayer-subtitle-btn">
            <button class="dplayer-icon dplayer-subtitle-icon" data-balloon="`,R+=V(ue("hide-subs")),R+=`" data-balloon-pos="up">
                <span class="dplayer-icon-content">`,R+=ve.subtitle,R+=`</span>
            </button>
        </div>
        `):(R+=`
        <div class="dplayer-subtitles">
            <button class="dplayer-icon dplayer-subtitles-icon" data-balloon="`,R+=V(ue("subtitle")),R+=`" data-balloon-pos="up">
                <span class="dplayer-icon-content">`,R+=ve.subtitle,R+=`</span>
            </button>
            <div class="dplayer-subtitles-box">
                <div class="dplayer-subtitles-panel">
                    `,Se(se.subtitle.url,function(ge,st){R+=`
                        <div class="dplayer-subtitles-item" data-subtitle="`,R+=V(ge.url),R+=`">
                            <!-- if lang, show tran(lang). if lang and name, show name + (tran(lang)). if name, show name. off option use lang for translation. -->
                            <span class="dplayer-label">`,R+=V(ge.lang?ge.name?ge.name+" ("+ue(ge.lang)+")":ue(ge.lang):ge.name),R+=`</span>
                        </div>
                    `}),R+=`
                </div>
            </div>
        </div>
        `),R+=`
        `),R+=`
        <div class="dplayer-setting">
            <button class="dplayer-icon dplayer-setting-icon" data-balloon="`,R+=V(ue("setting")),R+=`" data-balloon-pos="up">
                <span class="dplayer-icon-content">`,R+=ve.setting,R+=`</span>
            </button>
            <div class="dplayer-setting-box">
                <div class="dplayer-setting-origin-panel">
                    <div class="dplayer-setting-item dplayer-setting-speed">
                        <span class="dplayer-label">`,R+=V(ue("speed")),R+=`</span>
                        <div class="dplayer-toggle">`,R+=ve.right,R+=`</div>
                    </div>
                    <div class="dplayer-setting-item dplayer-setting-loop">
                        <span class="dplayer-label">`,R+=V(ue("loop")),R+=`</span>
                        <div class="dplayer-toggle">
                            <input class="dplayer-toggle-setting-input" type="checkbox" name="dplayer-toggle">
                            <label for="dplayer-toggle"></label>
                        </div>
                    </div>
                    <div class="dplayer-setting-item dplayer-setting-showdan">
                        <span class="dplayer-label">`,R+=V(ue("show-danmaku")),R+=`</span>
                        <div class="dplayer-toggle">
                            <input class="dplayer-showdan-setting-input" type="checkbox" name="dplayer-toggle-dan">
                            <label for="dplayer-toggle-dan"></label>
                        </div>
                    </div>
                    <div class="dplayer-setting-item dplayer-setting-danunlimit">
                        <span class="dplayer-label">`,R+=V(ue("unlimited-danmaku")),R+=`</span>
                        <div class="dplayer-toggle">
                            <input class="dplayer-danunlimit-setting-input" type="checkbox" name="dplayer-toggle-danunlimit">
                            <label for="dplayer-toggle-danunlimit"></label>
                        </div>
                    </div>
                    <div class="dplayer-setting-item dplayer-setting-danmaku">
                        <span class="dplayer-label">`,R+=V(ue("opacity-danmaku")),R+=`</span>
                        <div class="dplayer-danmaku-bar-wrap">
                            <div class="dplayer-danmaku-bar">
                                <div class="dplayer-danmaku-bar-inner">
                                    <span class="dplayer-thumb"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="dplayer-setting-speed-panel">
                    `,Se(se.playbackSpeed,function(ge,st){R+=`
                        <div class="dplayer-setting-speed-item" data-speed="`,R+=V(ge),R+=`">
                            <span class="dplayer-label">`,R+=V(ge===1?ue("normal"):ge),R+=`</span>
                        </div>
                    `}),R+=`
                </div>
            </div>
        </div>
        <div class="dplayer-full">
            <button class="dplayer-icon dplayer-full-in-icon" data-balloon="`,R+=V(ue("web-fullscreen")),R+=`" data-balloon-pos="up">
                <span class="dplayer-icon-content">`,R+=ve.fullWeb,R+=`</span>
            </button>
            <button class="dplayer-icon dplayer-full-icon" data-balloon="`,R+=V(ue("fullscreen")),R+=`" data-balloon-pos="up">
                <span class="dplayer-icon-content">`,R+=ve.full,R+=`</span>
            </button>
        </div>
    </div>
    <div class="dplayer-bar-wrap">
        <div class="dplayer-bar-time hidden">00:00</div>
        <div class="dplayer-bar-preview"></div>
        <div class="dplayer-bar">
            <div class="dplayer-loaded" style="width: 0;"></div>
            <div class="dplayer-played" style="width: 0; background: `,R+=V(se.theme),R+=`">
                <span class="dplayer-thumb" style="background: `,R+=V(se.theme),R+=`"></span>
            </div>
        </div>
    </div>
</div>
<div class="dplayer-info-panel dplayer-info-panel-hide">
    <div class="dplayer-info-panel-close">[x]</div>
    <div class="dplayer-info-panel-item dplayer-info-panel-item-version">
        <span class="dplayer-info-panel-item-title">Player version</span>
        <span class="dplayer-info-panel-item-data"></span>
    </div>
    <div class="dplayer-info-panel-item dplayer-info-panel-item-fps">
        <span class="dplayer-info-panel-item-title">Player FPS</span>
        <span class="dplayer-info-panel-item-data"></span>
    </div>
    <div class="dplayer-info-panel-item dplayer-info-panel-item-type">
        <span class="dplayer-info-panel-item-title">Video type</span>
        <span class="dplayer-info-panel-item-data"></span>
    </div>
    <div class="dplayer-info-panel-item dplayer-info-panel-item-url">
        <span class="dplayer-info-panel-item-title">Video url</span>
        <span class="dplayer-info-panel-item-data"></span>
    </div>
    <div class="dplayer-info-panel-item dplayer-info-panel-item-resolution">
        <span class="dplayer-info-panel-item-title">Video resolution</span>
        <span class="dplayer-info-panel-item-data"></span>
    </div>
    <div class="dplayer-info-panel-item dplayer-info-panel-item-duration">
        <span class="dplayer-info-panel-item-title">Video duration</span>
        <span class="dplayer-info-panel-item-data"></span>
    </div>
    `,se.danmaku&&(R+=`
    <div class="dplayer-info-panel-item dplayer-info-panel-item-danmaku-id">
        <span class="dplayer-info-panel-item-title">Danmaku id</span>
        <span class="dplayer-info-panel-item-data"></span>
    </div>
    <div class="dplayer-info-panel-item dplayer-info-panel-item-danmaku-api">
        <span class="dplayer-info-panel-item-title">Danmaku api</span>
        <span class="dplayer-info-panel-item-data"></span>
    </div>
    <div class="dplayer-info-panel-item dplayer-info-panel-item-danmaku-amount">
        <span class="dplayer-info-panel-item-title">Danmaku amount</span>
        <span class="dplayer-info-panel-item-data"></span>
    </div>
    `),R+=`
</div>
<div class="dplayer-menu">
    `,Se(se.contextmenu,function(ge,st){R+=`
        <div class="dplayer-menu-item">
            <a`,ge.link&&(R+=' target="_blank"'),R+=' href="',R+=V(ge.link||"javascript:void(0);"),R+='">',ge.key&&(R+=" ",R+=V(ue(ge.key))),ge.text&&(R+=" ",R+=V(ge.text)),R+=`</a>
        </div>
    `}),R+=`
</div>
<div class="dplayer-notice-list"></div>
<button class="dplayer-mobile-play">
    `,R+=ve.play,R+=`
</button>`}},568:(q,j,Z)=>{var le=Z(471);q.exports=function(ce){"use strict";var ee="",R=(ce=ce||{}).enableSubtitle,ie=ce.subtitle,se=ce.current,V=ce.airplay,ue=ce.pic,ve=le.$escape,de=ce.screenshot,Se=ce.preload,ge=ce.url;return R=ie&&ie.type==="webvtt",ee+=`
<video
    class="dplayer-video `,se&&(ee+="dplayer-video-current"),ee+=`"
    webkit-playsinline
    `,V&&(ee+=' x-webkit-airplay="allow" '),ee+=`
    playsinline
    `,ue&&(ee+='poster="',ee+=ve(ue),ee+='"'),ee+=`
    `,(de||R)&&(ee+='crossorigin="anonymous"'),ee+=`
    `,Se&&(ee+='preload="',ee+=ve(Se),ee+='"'),ee+=`
    `,V?ee+=`
    nosrc
    `:ge&&(ee+=`
    src="`,ee+=ve(ge),ee+=`"
    `),ee+=`
    >
    `,V&&(ee+=`
    <source src="`,ee+=ve(ge),ee+=`">
    `),ee+=`
    `,R&&(ee+=`
    <track class="dplayer-subtrack" kind="metadata" default src="`,ee+=ve(typeof ie.url=="string"?ie.url:ie.url[ie.index].url),ee+=`"></track>
    `),ee+`
</video>`}},49:(q,j,Z)=>{"use strict";Z.d(j,{Z:()=>ie});var le=Z(415),ce=Z.n(le),ee=Z(352),R=Z.n(ee)()(ce());R.push([q.id,`:root {
  --balloon-border-radius: 2px;
  --balloon-color: rgba(16, 16, 16, 0.95);
  --balloon-text-color: #fff;
  --balloon-font-size: 12px;
  --balloon-move: 4px; }

button[aria-label][data-balloon-pos] {
  overflow: visible; }

[aria-label][data-balloon-pos] {
  position: relative;
  cursor: pointer; }
  [aria-label][data-balloon-pos]:after {
    opacity: 0;
    pointer-events: none;
    transition: all 0.18s ease-out 0.18s;
    text-indent: 0;
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
    font-weight: normal;
    font-style: normal;
    text-shadow: none;
    font-size: 12px;
    font-size: var(--balloon-font-size);
    background: rgba(16, 16, 16, 0.95);
    background: var(--balloon-color);
    border-radius: 2px;
    color: #fff;
    color: var(--balloon-text-color);
    border-radius: var(--balloon-border-radius);
    content: attr(aria-label);
    padding: .5em 1em;
    position: absolute;
    white-space: nowrap;
    z-index: 10; }
  [aria-label][data-balloon-pos]:before {
    width: 0;
    height: 0;
    border: 5px solid transparent;
    border-top-color: rgba(16, 16, 16, 0.95);
    border-top-color: var(--balloon-color);
    opacity: 0;
    pointer-events: none;
    transition: all 0.18s ease-out 0.18s;
    content: "";
    position: absolute;
    z-index: 10; }
  [aria-label][data-balloon-pos]:hover:before, [aria-label][data-balloon-pos]:hover:after, [aria-label][data-balloon-pos][data-balloon-visible]:before, [aria-label][data-balloon-pos][data-balloon-visible]:after, [aria-label][data-balloon-pos]:not([data-balloon-nofocus]):focus:before, [aria-label][data-balloon-pos]:not([data-balloon-nofocus]):focus:after {
    opacity: 1;
    pointer-events: none; }
  [aria-label][data-balloon-pos].font-awesome:after {
    font-family: FontAwesome, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif; }
  [aria-label][data-balloon-pos][data-balloon-break]:after {
    white-space: pre; }
  [aria-label][data-balloon-pos][data-balloon-break][data-balloon-length]:after {
    white-space: pre-line;
    word-break: break-word; }
  [aria-label][data-balloon-pos][data-balloon-blunt]:before, [aria-label][data-balloon-pos][data-balloon-blunt]:after {
    transition: none; }
  [aria-label][data-balloon-pos][data-balloon-pos="up"]:hover:after, [aria-label][data-balloon-pos][data-balloon-pos="up"][data-balloon-visible]:after, [aria-label][data-balloon-pos][data-balloon-pos="down"]:hover:after, [aria-label][data-balloon-pos][data-balloon-pos="down"][data-balloon-visible]:after {
    transform: translate(-50%, 0); }
  [aria-label][data-balloon-pos][data-balloon-pos="up"]:hover:before, [aria-label][data-balloon-pos][data-balloon-pos="up"][data-balloon-visible]:before, [aria-label][data-balloon-pos][data-balloon-pos="down"]:hover:before, [aria-label][data-balloon-pos][data-balloon-pos="down"][data-balloon-visible]:before {
    transform: translate(-50%, 0); }
  [aria-label][data-balloon-pos][data-balloon-pos*="-left"]:after {
    left: 0; }
  [aria-label][data-balloon-pos][data-balloon-pos*="-left"]:before {
    left: 5px; }
  [aria-label][data-balloon-pos][data-balloon-pos*="-right"]:after {
    right: 0; }
  [aria-label][data-balloon-pos][data-balloon-pos*="-right"]:before {
    right: 5px; }
  [aria-label][data-balloon-pos][data-balloon-po*="-left"]:hover:after, [aria-label][data-balloon-pos][data-balloon-po*="-left"][data-balloon-visible]:after, [aria-label][data-balloon-pos][data-balloon-pos*="-right"]:hover:after, [aria-label][data-balloon-pos][data-balloon-pos*="-right"][data-balloon-visible]:after {
    transform: translate(0, 0); }
  [aria-label][data-balloon-pos][data-balloon-po*="-left"]:hover:before, [aria-label][data-balloon-pos][data-balloon-po*="-left"][data-balloon-visible]:before, [aria-label][data-balloon-pos][data-balloon-pos*="-right"]:hover:before, [aria-label][data-balloon-pos][data-balloon-pos*="-right"][data-balloon-visible]:before {
    transform: translate(0, 0); }
  [aria-label][data-balloon-pos][data-balloon-pos^="up"]:before, [aria-label][data-balloon-pos][data-balloon-pos^="up"]:after {
    bottom: 100%;
    transform-origin: top;
    transform: translate(0, 4px);
    transform: translate(0, var(--balloon-move)); }
  [aria-label][data-balloon-pos][data-balloon-pos^="up"]:after {
    margin-bottom: 10px; }
  [aria-label][data-balloon-pos][data-balloon-pos="up"]:before, [aria-label][data-balloon-pos][data-balloon-pos="up"]:after {
    left: 50%;
    transform: translate(-50%, 4px);
    transform: translate(-50%, var(--balloon-move)); }
  [aria-label][data-balloon-pos][data-balloon-pos^="down"]:before, [aria-label][data-balloon-pos][data-balloon-pos^="down"]:after {
    top: 100%;
    transform: translate(0, calc(4px * -1));
    transform: translate(0, calc(var(--balloon-move) * -1)); }
  [aria-label][data-balloon-pos][data-balloon-pos^="down"]:after {
    margin-top: 10px; }
  [aria-label][data-balloon-pos][data-balloon-pos^="down"]:before {
    width: 0;
    height: 0;
    border: 5px solid transparent;
    border-bottom-color: rgba(16, 16, 16, 0.95);
    border-bottom-color: var(--balloon-color); }
  [aria-label][data-balloon-pos][data-balloon-pos="down"]:after, [aria-label][data-balloon-pos][data-balloon-pos="down"]:before {
    left: 50%;
    transform: translate(-50%, calc(4px * -1));
    transform: translate(-50%, calc(var(--balloon-move) * -1)); }
  [aria-label][data-balloon-pos][data-balloon-pos="left"]:hover:after, [aria-label][data-balloon-pos][data-balloon-pos="left"][data-balloon-visible]:after, [aria-label][data-balloon-pos][data-balloon-pos="right"]:hover:after, [aria-label][data-balloon-pos][data-balloon-pos="right"][data-balloon-visible]:after {
    transform: translate(0, -50%); }
  [aria-label][data-balloon-pos][data-balloon-pos="left"]:hover:before, [aria-label][data-balloon-pos][data-balloon-pos="left"][data-balloon-visible]:before, [aria-label][data-balloon-pos][data-balloon-pos="right"]:hover:before, [aria-label][data-balloon-pos][data-balloon-pos="right"][data-balloon-visible]:before {
    transform: translate(0, -50%); }
  [aria-label][data-balloon-pos][data-balloon-pos="left"]:after, [aria-label][data-balloon-pos][data-balloon-pos="left"]:before {
    right: 100%;
    top: 50%;
    transform: translate(4px, -50%);
    transform: translate(var(--balloon-move), -50%); }
  [aria-label][data-balloon-pos][data-balloon-pos="left"]:after {
    margin-right: 10px; }
  [aria-label][data-balloon-pos][data-balloon-pos="left"]:before {
    width: 0;
    height: 0;
    border: 5px solid transparent;
    border-left-color: rgba(16, 16, 16, 0.95);
    border-left-color: var(--balloon-color); }
  [aria-label][data-balloon-pos][data-balloon-pos="right"]:after, [aria-label][data-balloon-pos][data-balloon-pos="right"]:before {
    left: 100%;
    top: 50%;
    transform: translate(calc(4px * -1), -50%);
    transform: translate(calc(var(--balloon-move) * -1), -50%); }
  [aria-label][data-balloon-pos][data-balloon-pos="right"]:after {
    margin-left: 10px; }
  [aria-label][data-balloon-pos][data-balloon-pos="right"]:before {
    width: 0;
    height: 0;
    border: 5px solid transparent;
    border-right-color: rgba(16, 16, 16, 0.95);
    border-right-color: var(--balloon-color); }
  [aria-label][data-balloon-pos][data-balloon-length]:after {
    white-space: normal; }
  [aria-label][data-balloon-pos][data-balloon-length="small"]:after {
    width: 80px; }
  [aria-label][data-balloon-pos][data-balloon-length="medium"]:after {
    width: 150px; }
  [aria-label][data-balloon-pos][data-balloon-length="large"]:after {
    width: 260px; }
  [aria-label][data-balloon-pos][data-balloon-length="xlarge"]:after {
    width: 380px; }
    @media screen and (max-width: 768px) {
      [aria-label][data-balloon-pos][data-balloon-length="xlarge"]:after {
        width: 90vw; } }
  [aria-label][data-balloon-pos][data-balloon-length="fit"]:after {
    width: 100%; }
`,"",{version:3,sources:["webpack://./node_modules/.pnpm/balloon-css@1.2.0/node_modules/balloon-css/balloon.css"],names:[],mappings:"AAAA;EACE,4BAA4B;EAC5B,uCAAuC;EACvC,0BAA0B;EAC1B,yBAAyB;EACzB,mBAAmB,EAAE;;AAEvB;EACE,iBAAiB,EAAE;;AAErB;EACE,kBAAkB;EAClB,eAAe,EAAE;EACjB;IACE,UAAU;IACV,oBAAoB;IACpB,oCAAoC;IACpC,cAAc;IACd,wIAAwI;IACxI,mBAAmB;IACnB,kBAAkB;IAClB,iBAAiB;IACjB,eAAmC;IAAnC,mCAAmC;IACnC,kCAAgC;IAAhC,gCAAgC;IAChC,kBAAkB;IAClB,WAAgC;IAAhC,gCAAgC;IAChC,2CAA2C;IAC3C,yBAAyB;IACzB,iBAAiB;IACjB,kBAAkB;IAClB,mBAAmB;IACnB,WAAW,EAAE;EACf;IACE,QAAQ;IACR,SAAS;IACT,6BAA6B;IAC7B,wCAAsC;IAAtC,sCAAsC;IACtC,UAAU;IACV,oBAAoB;IACpB,oCAAoC;IACpC,WAAW;IACX,kBAAkB;IAClB,WAAW,EAAE;EACf;IACE,UAAU;IACV,oBAAoB,EAAE;EACxB;IACE,qJAAqJ,EAAE;EACzJ;IACE,gBAAgB,EAAE;EACpB;IACE,qBAAqB;IACrB,sBAAsB,EAAE;EAC1B;IACE,gBAAgB,EAAE;EACpB;IACE,6BAA6B,EAAE;EACjC;IACE,6BAA6B,EAAE;EACjC;IACE,OAAO,EAAE;EACX;IACE,SAAS,EAAE;EACb;IACE,QAAQ,EAAE;EACZ;IACE,UAAU,EAAE;EACd;IACE,0BAA0B,EAAE;EAC9B;IACE,0BAA0B,EAAE;EAC9B;IACE,YAAY;IACZ,qBAAqB;IACrB,4BAA4C;IAA5C,4CAA4C,EAAE;EAChD;IACE,mBAAmB,EAAE;EACvB;IACE,SAAS;IACT,+BAA+C;IAA/C,+CAA+C,EAAE;EACnD;IACE,SAAS;IACT,uCAAuD;IAAvD,uDAAuD,EAAE;EAC3D;IACE,gBAAgB,EAAE;EACpB;IACE,QAAQ;IACR,SAAS;IACT,6BAA6B;IAC7B,2CAAyC;IAAzC,yCAAyC,EAAE;EAC7C;IACE,SAAS;IACT,0CAA0D;IAA1D,0DAA0D,EAAE;EAC9D;IACE,6BAA6B,EAAE;EACjC;IACE,6BAA6B,EAAE;EACjC;IACE,WAAW;IACX,QAAQ;IACR,+BAA+C;IAA/C,+CAA+C,EAAE;EACnD;IACE,kBAAkB,EAAE;EACtB;IACE,QAAQ;IACR,SAAS;IACT,6BAA6B;IAC7B,yCAAuC;IAAvC,uCAAuC,EAAE;EAC3C;IACE,UAAU;IACV,QAAQ;IACR,0CAA0D;IAA1D,0DAA0D,EAAE;EAC9D;IACE,iBAAiB,EAAE;EACrB;IACE,QAAQ;IACR,SAAS;IACT,6BAA6B;IAC7B,0CAAwC;IAAxC,wCAAwC,EAAE;EAC5C;IACE,mBAAmB,EAAE;EACvB;IACE,WAAW,EAAE;EACf;IACE,YAAY,EAAE;EAChB;IACE,YAAY,EAAE;EAChB;IACE,YAAY,EAAE;IACd;MACE;QACE,WAAW,EAAE,EAAE;EACrB;IACE,WAAW,EAAE",sourcesContent:[`:root {
  --balloon-border-radius: 2px;
  --balloon-color: rgba(16, 16, 16, 0.95);
  --balloon-text-color: #fff;
  --balloon-font-size: 12px;
  --balloon-move: 4px; }

button[aria-label][data-balloon-pos] {
  overflow: visible; }

[aria-label][data-balloon-pos] {
  position: relative;
  cursor: pointer; }
  [aria-label][data-balloon-pos]:after {
    opacity: 0;
    pointer-events: none;
    transition: all 0.18s ease-out 0.18s;
    text-indent: 0;
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
    font-weight: normal;
    font-style: normal;
    text-shadow: none;
    font-size: var(--balloon-font-size);
    background: var(--balloon-color);
    border-radius: 2px;
    color: var(--balloon-text-color);
    border-radius: var(--balloon-border-radius);
    content: attr(aria-label);
    padding: .5em 1em;
    position: absolute;
    white-space: nowrap;
    z-index: 10; }
  [aria-label][data-balloon-pos]:before {
    width: 0;
    height: 0;
    border: 5px solid transparent;
    border-top-color: var(--balloon-color);
    opacity: 0;
    pointer-events: none;
    transition: all 0.18s ease-out 0.18s;
    content: "";
    position: absolute;
    z-index: 10; }
  [aria-label][data-balloon-pos]:hover:before, [aria-label][data-balloon-pos]:hover:after, [aria-label][data-balloon-pos][data-balloon-visible]:before, [aria-label][data-balloon-pos][data-balloon-visible]:after, [aria-label][data-balloon-pos]:not([data-balloon-nofocus]):focus:before, [aria-label][data-balloon-pos]:not([data-balloon-nofocus]):focus:after {
    opacity: 1;
    pointer-events: none; }
  [aria-label][data-balloon-pos].font-awesome:after {
    font-family: FontAwesome, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif; }
  [aria-label][data-balloon-pos][data-balloon-break]:after {
    white-space: pre; }
  [aria-label][data-balloon-pos][data-balloon-break][data-balloon-length]:after {
    white-space: pre-line;
    word-break: break-word; }
  [aria-label][data-balloon-pos][data-balloon-blunt]:before, [aria-label][data-balloon-pos][data-balloon-blunt]:after {
    transition: none; }
  [aria-label][data-balloon-pos][data-balloon-pos="up"]:hover:after, [aria-label][data-balloon-pos][data-balloon-pos="up"][data-balloon-visible]:after, [aria-label][data-balloon-pos][data-balloon-pos="down"]:hover:after, [aria-label][data-balloon-pos][data-balloon-pos="down"][data-balloon-visible]:after {
    transform: translate(-50%, 0); }
  [aria-label][data-balloon-pos][data-balloon-pos="up"]:hover:before, [aria-label][data-balloon-pos][data-balloon-pos="up"][data-balloon-visible]:before, [aria-label][data-balloon-pos][data-balloon-pos="down"]:hover:before, [aria-label][data-balloon-pos][data-balloon-pos="down"][data-balloon-visible]:before {
    transform: translate(-50%, 0); }
  [aria-label][data-balloon-pos][data-balloon-pos*="-left"]:after {
    left: 0; }
  [aria-label][data-balloon-pos][data-balloon-pos*="-left"]:before {
    left: 5px; }
  [aria-label][data-balloon-pos][data-balloon-pos*="-right"]:after {
    right: 0; }
  [aria-label][data-balloon-pos][data-balloon-pos*="-right"]:before {
    right: 5px; }
  [aria-label][data-balloon-pos][data-balloon-po*="-left"]:hover:after, [aria-label][data-balloon-pos][data-balloon-po*="-left"][data-balloon-visible]:after, [aria-label][data-balloon-pos][data-balloon-pos*="-right"]:hover:after, [aria-label][data-balloon-pos][data-balloon-pos*="-right"][data-balloon-visible]:after {
    transform: translate(0, 0); }
  [aria-label][data-balloon-pos][data-balloon-po*="-left"]:hover:before, [aria-label][data-balloon-pos][data-balloon-po*="-left"][data-balloon-visible]:before, [aria-label][data-balloon-pos][data-balloon-pos*="-right"]:hover:before, [aria-label][data-balloon-pos][data-balloon-pos*="-right"][data-balloon-visible]:before {
    transform: translate(0, 0); }
  [aria-label][data-balloon-pos][data-balloon-pos^="up"]:before, [aria-label][data-balloon-pos][data-balloon-pos^="up"]:after {
    bottom: 100%;
    transform-origin: top;
    transform: translate(0, var(--balloon-move)); }
  [aria-label][data-balloon-pos][data-balloon-pos^="up"]:after {
    margin-bottom: 10px; }
  [aria-label][data-balloon-pos][data-balloon-pos="up"]:before, [aria-label][data-balloon-pos][data-balloon-pos="up"]:after {
    left: 50%;
    transform: translate(-50%, var(--balloon-move)); }
  [aria-label][data-balloon-pos][data-balloon-pos^="down"]:before, [aria-label][data-balloon-pos][data-balloon-pos^="down"]:after {
    top: 100%;
    transform: translate(0, calc(var(--balloon-move) * -1)); }
  [aria-label][data-balloon-pos][data-balloon-pos^="down"]:after {
    margin-top: 10px; }
  [aria-label][data-balloon-pos][data-balloon-pos^="down"]:before {
    width: 0;
    height: 0;
    border: 5px solid transparent;
    border-bottom-color: var(--balloon-color); }
  [aria-label][data-balloon-pos][data-balloon-pos="down"]:after, [aria-label][data-balloon-pos][data-balloon-pos="down"]:before {
    left: 50%;
    transform: translate(-50%, calc(var(--balloon-move) * -1)); }
  [aria-label][data-balloon-pos][data-balloon-pos="left"]:hover:after, [aria-label][data-balloon-pos][data-balloon-pos="left"][data-balloon-visible]:after, [aria-label][data-balloon-pos][data-balloon-pos="right"]:hover:after, [aria-label][data-balloon-pos][data-balloon-pos="right"][data-balloon-visible]:after {
    transform: translate(0, -50%); }
  [aria-label][data-balloon-pos][data-balloon-pos="left"]:hover:before, [aria-label][data-balloon-pos][data-balloon-pos="left"][data-balloon-visible]:before, [aria-label][data-balloon-pos][data-balloon-pos="right"]:hover:before, [aria-label][data-balloon-pos][data-balloon-pos="right"][data-balloon-visible]:before {
    transform: translate(0, -50%); }
  [aria-label][data-balloon-pos][data-balloon-pos="left"]:after, [aria-label][data-balloon-pos][data-balloon-pos="left"]:before {
    right: 100%;
    top: 50%;
    transform: translate(var(--balloon-move), -50%); }
  [aria-label][data-balloon-pos][data-balloon-pos="left"]:after {
    margin-right: 10px; }
  [aria-label][data-balloon-pos][data-balloon-pos="left"]:before {
    width: 0;
    height: 0;
    border: 5px solid transparent;
    border-left-color: var(--balloon-color); }
  [aria-label][data-balloon-pos][data-balloon-pos="right"]:after, [aria-label][data-balloon-pos][data-balloon-pos="right"]:before {
    left: 100%;
    top: 50%;
    transform: translate(calc(var(--balloon-move) * -1), -50%); }
  [aria-label][data-balloon-pos][data-balloon-pos="right"]:after {
    margin-left: 10px; }
  [aria-label][data-balloon-pos][data-balloon-pos="right"]:before {
    width: 0;
    height: 0;
    border: 5px solid transparent;
    border-right-color: var(--balloon-color); }
  [aria-label][data-balloon-pos][data-balloon-length]:after {
    white-space: normal; }
  [aria-label][data-balloon-pos][data-balloon-length="small"]:after {
    width: 80px; }
  [aria-label][data-balloon-pos][data-balloon-length="medium"]:after {
    width: 150px; }
  [aria-label][data-balloon-pos][data-balloon-length="large"]:after {
    width: 260px; }
  [aria-label][data-balloon-pos][data-balloon-length="xlarge"]:after {
    width: 380px; }
    @media screen and (max-width: 768px) {
      [aria-label][data-balloon-pos][data-balloon-length="xlarge"]:after {
        width: 90vw; } }
  [aria-label][data-balloon-pos][data-balloon-length="fit"]:after {
    width: 100%; }
`],sourceRoot:""}]);const ie=R},685:(q,j,Z)=>{"use strict";Z.d(j,{Z:()=>Se});var le=Z(415),ce=Z.n(le),ee=Z(352),R=Z.n(ee),ie=Z(49),se=Z(80),V=Z.n(se),ue=new URL(Z(831),Z.b),ve=R()(ce());ve.i(ie.Z);var de=V()(ue);ve.push([q.id,`@keyframes my-face {
  2% {
    transform: translate(0, 1.5px) rotate(1.5deg);
  }
  4% {
    transform: translate(0, -1.5px) rotate(-0.5deg);
  }
  6% {
    transform: translate(0, 1.5px) rotate(-1.5deg);
  }
  8% {
    transform: translate(0, -1.5px) rotate(-1.5deg);
  }
  10% {
    transform: translate(0, 2.5px) rotate(1.5deg);
  }
  12% {
    transform: translate(0, -0.5px) rotate(1.5deg);
  }
  14% {
    transform: translate(0, -1.5px) rotate(1.5deg);
  }
  16% {
    transform: translate(0, -0.5px) rotate(-1.5deg);
  }
  18% {
    transform: translate(0, 0.5px) rotate(-1.5deg);
  }
  20% {
    transform: translate(0, -1.5px) rotate(2.5deg);
  }
  22% {
    transform: translate(0, 0.5px) rotate(-1.5deg);
  }
  24% {
    transform: translate(0, 1.5px) rotate(1.5deg);
  }
  26% {
    transform: translate(0, 0.5px) rotate(0.5deg);
  }
  28% {
    transform: translate(0, 0.5px) rotate(1.5deg);
  }
  30% {
    transform: translate(0, -0.5px) rotate(2.5deg);
  }
  32% {
    transform: translate(0, 1.5px) rotate(-0.5deg);
  }
  34% {
    transform: translate(0, 1.5px) rotate(-0.5deg);
  }
  36% {
    transform: translate(0, -1.5px) rotate(2.5deg);
  }
  38% {
    transform: translate(0, 1.5px) rotate(-1.5deg);
  }
  40% {
    transform: translate(0, -0.5px) rotate(2.5deg);
  }
  42% {
    transform: translate(0, 2.5px) rotate(-1.5deg);
  }
  44% {
    transform: translate(0, 1.5px) rotate(0.5deg);
  }
  46% {
    transform: translate(0, -1.5px) rotate(2.5deg);
  }
  48% {
    transform: translate(0, -0.5px) rotate(0.5deg);
  }
  50% {
    transform: translate(0, 0.5px) rotate(0.5deg);
  }
  52% {
    transform: translate(0, 2.5px) rotate(2.5deg);
  }
  54% {
    transform: translate(0, -1.5px) rotate(1.5deg);
  }
  56% {
    transform: translate(0, 2.5px) rotate(2.5deg);
  }
  58% {
    transform: translate(0, 0.5px) rotate(2.5deg);
  }
  60% {
    transform: translate(0, 2.5px) rotate(2.5deg);
  }
  62% {
    transform: translate(0, -0.5px) rotate(2.5deg);
  }
  64% {
    transform: translate(0, -0.5px) rotate(1.5deg);
  }
  66% {
    transform: translate(0, 1.5px) rotate(-0.5deg);
  }
  68% {
    transform: translate(0, -1.5px) rotate(-0.5deg);
  }
  70% {
    transform: translate(0, 1.5px) rotate(0.5deg);
  }
  72% {
    transform: translate(0, 2.5px) rotate(1.5deg);
  }
  74% {
    transform: translate(0, -0.5px) rotate(0.5deg);
  }
  76% {
    transform: translate(0, -0.5px) rotate(2.5deg);
  }
  78% {
    transform: translate(0, -0.5px) rotate(1.5deg);
  }
  80% {
    transform: translate(0, 1.5px) rotate(1.5deg);
  }
  82% {
    transform: translate(0, -0.5px) rotate(0.5deg);
  }
  84% {
    transform: translate(0, 1.5px) rotate(2.5deg);
  }
  86% {
    transform: translate(0, -1.5px) rotate(-1.5deg);
  }
  88% {
    transform: translate(0, -0.5px) rotate(2.5deg);
  }
  90% {
    transform: translate(0, 2.5px) rotate(-0.5deg);
  }
  92% {
    transform: translate(0, 0.5px) rotate(-0.5deg);
  }
  94% {
    transform: translate(0, 2.5px) rotate(0.5deg);
  }
  96% {
    transform: translate(0, -0.5px) rotate(1.5deg);
  }
  98% {
    transform: translate(0, -1.5px) rotate(-0.5deg);
  }
  0%,
  100% {
    transform: translate(0, 0) rotate(0deg);
  }
}
.dplayer {
  position: relative;
  overflow: hidden;
  -webkit-user-select: none;
     -moz-user-select: none;
          user-select: none;
  line-height: 1;
}
.dplayer * {
  box-sizing: content-box;
}
.dplayer svg {
  width: 100%;
  height: 100%;
}
.dplayer svg path,
.dplayer svg circle {
  fill: #fff;
}
.dplayer:-webkit-full-screen {
  width: 100%;
  height: 100%;
  background: #000;
  position: fixed;
  z-index: 100000;
  left: 0;
  top: 0;
  margin: 0;
  padding: 0;
  transform: translate(0, 0);
}
.dplayer.dplayer-no-danmaku .dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box .dplayer-setting-showdan,
.dplayer.dplayer-no-danmaku .dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box .dplayer-setting-danmaku,
.dplayer.dplayer-no-danmaku .dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box .dplayer-setting-danunlimit {
  display: none;
}
.dplayer.dplayer-no-danmaku .dplayer-controller .dplayer-icons .dplayer-comment {
  display: none;
}
.dplayer.dplayer-no-danmaku .dplayer-danmaku {
  display: none;
}
.dplayer.dplayer-live .dplayer-time {
  display: none;
}
.dplayer.dplayer-live .dplayer-bar-wrap {
  display: none;
}
.dplayer.dplayer-live .dplayer-setting-speed {
  display: none;
}
.dplayer.dplayer-live .dplayer-setting-loop {
  display: none;
}
.dplayer.dplayer-live.dplayer-no-danmaku .dplayer-setting {
  display: none;
}
.dplayer.dplayer-arrow .dplayer-danmaku {
  font-size: 18px;
}
.dplayer.dplayer-arrow .dplayer-icon {
  margin: 0 -3px;
}
.dplayer.dplayer-playing .dplayer-danmaku .dplayer-danmaku-move {
  animation-play-state: running;
}
@media (min-width: 900px) {
  .dplayer.dplayer-playing .dplayer-controller-mask {
    opacity: 0;
  }
  .dplayer.dplayer-playing .dplayer-controller {
    opacity: 0;
  }
  .dplayer.dplayer-playing:hover .dplayer-controller-mask {
    opacity: 1;
  }
  .dplayer.dplayer-playing:hover .dplayer-controller {
    opacity: 1;
  }
}
.dplayer.dplayer-loading .dplayer-bezel .diplayer-loading-icon {
  display: block;
}
.dplayer.dplayer-loading .dplayer-danmaku,
.dplayer.dplayer-paused .dplayer-danmaku,
.dplayer.dplayer-loading .dplayer-danmaku-move,
.dplayer.dplayer-paused .dplayer-danmaku-move {
  animation-play-state: paused;
}
.dplayer.dplayer-hide-controller {
  cursor: none;
}
.dplayer.dplayer-hide-controller .dplayer-controller-mask {
  opacity: 0;
  transform: translateY(100%);
}
.dplayer.dplayer-hide-controller .dplayer-controller {
  opacity: 0;
  transform: translateY(100%);
}
.dplayer.dplayer-show-controller .dplayer-controller-mask {
  opacity: 1;
}
.dplayer.dplayer-show-controller .dplayer-controller {
  opacity: 1;
}
.dplayer.dplayer-fulled {
  width: 100% !important;
  height: 100% !important;
}
.dplayer.dplayer-fulled {
  position: fixed;
  z-index: 100000;
  left: 0;
  top: 0;
}
.dplayer.dplayer-mobile .dplayer-controller .dplayer-icons .dplayer-volume,
.dplayer.dplayer-mobile .dplayer-controller .dplayer-icons .dplayer-camera-icon,
.dplayer.dplayer-mobile .dplayer-controller .dplayer-icons .dplayer-airplay-icon,
.dplayer.dplayer-mobile .dplayer-controller .dplayer-icons .dplayer-chromecast-icon,
.dplayer.dplayer-mobile .dplayer-controller .dplayer-icons .dplayer-play-icon {
  display: none;
}
.dplayer.dplayer-mobile .dplayer-controller .dplayer-icons .dplayer-full .dplayer-full-in-icon {
  position: static;
  display: inline-block;
}
.dplayer.dplayer-mobile .dplayer-bar-time {
  display: none;
}
.dplayer.dplayer-mobile.dplayer-hide-controller .dplayer-mobile-play {
  display: none;
}
.dplayer.dplayer-mobile .dplayer-mobile-play {
  display: block;
}
.dplayer-web-fullscreen-fix {
  position: fixed;
  top: 0;
  left: 0;
  margin: 0;
  padding: 0;
}
[data-balloon]:before {
  display: none;
}
[data-balloon]:after {
  padding: 0.3em 0.7em;
  background: rgba(17, 17, 17, 0.7);
}
[data-balloon][data-balloon-pos="up"]:after {
  margin-bottom: 0;
}
.dplayer-bezel {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  font-size: 22px;
  color: #fff;
  pointer-events: none;
}
.dplayer-bezel .dplayer-bezel-icon {
  position: absolute;
  top: 50%;
  left: 50%;
  margin: -26px 0 0 -26px;
  height: 52px;
  width: 52px;
  padding: 12px;
  box-sizing: border-box;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  opacity: 0;
  pointer-events: none;
}
.dplayer-bezel .dplayer-bezel-icon.dplayer-bezel-transition {
  animation: bezel-hide 0.5s linear;
}
@keyframes bezel-hide {
  from {
    opacity: 1;
    transform: scale(1);
  }
  to {
    opacity: 0;
    transform: scale(2);
  }
}
.dplayer-bezel .dplayer-danloading {
  position: absolute;
  top: 50%;
  margin-top: -7px;
  width: 100%;
  text-align: center;
  font-size: 14px;
  line-height: 14px;
  animation: my-face 5s infinite ease-in-out;
}
.dplayer-bezel .diplayer-loading-icon {
  display: none;
  position: absolute;
  top: 50%;
  left: 50%;
  margin: -18px 0 0 -18px;
  height: 36px;
  width: 36px;
  pointer-events: none;
}
.dplayer-bezel .diplayer-loading-icon .diplayer-loading-hide {
  display: none;
}
.dplayer-bezel .diplayer-loading-icon .diplayer-loading-dot {
  animation: diplayer-loading-dot-fade 0.8s ease infinite;
  opacity: 0;
  transform-origin: 4px 4px;
}
.dplayer-bezel .diplayer-loading-icon .diplayer-loading-dot.diplayer-loading-dot-1 {
  animation-delay: 0.1s;
}
.dplayer-bezel .diplayer-loading-icon .diplayer-loading-dot.diplayer-loading-dot-2 {
  animation-delay: 0.2s;
}
.dplayer-bezel .diplayer-loading-icon .diplayer-loading-dot.diplayer-loading-dot-3 {
  animation-delay: 0.3s;
}
.dplayer-bezel .diplayer-loading-icon .diplayer-loading-dot.diplayer-loading-dot-4 {
  animation-delay: 0.4s;
}
.dplayer-bezel .diplayer-loading-icon .diplayer-loading-dot.diplayer-loading-dot-5 {
  animation-delay: 0.5s;
}
.dplayer-bezel .diplayer-loading-icon .diplayer-loading-dot.diplayer-loading-dot-6 {
  animation-delay: 0.6s;
}
.dplayer-bezel .diplayer-loading-icon .diplayer-loading-dot.diplayer-loading-dot-7 {
  animation-delay: 0.7s;
}
@keyframes diplayer-loading-dot-fade {
  0% {
    opacity: 0.7;
    transform: scale(1.2, 1.2);
  }
  50% {
    opacity: 0.25;
    transform: scale(0.9, 0.9);
  }
  to {
    opacity: 0.25;
    transform: scale(0.85, 0.85);
  }
}
.dplayer-controller-mask {
  background: url(`+de+`) repeat-x bottom;
  height: 98px;
  width: 100%;
  position: absolute;
  bottom: 0;
  transition: all 0.3s ease;
}
.dplayer-controller {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 41px;
  padding: 0 20px;
  -webkit-user-select: none;
     -moz-user-select: none;
          user-select: none;
  transition: all 0.3s ease;
}
.dplayer-controller.dplayer-controller-comment .dplayer-icons {
  display: none;
}
.dplayer-controller.dplayer-controller-comment .dplayer-icons.dplayer-comment-box {
  display: block;
}
.dplayer-controller .dplayer-bar-wrap {
  padding: 5px 0;
  cursor: pointer;
  position: absolute;
  bottom: 33px;
  width: calc(100% - 40px);
  height: 3px;
}
.dplayer-controller .dplayer-bar-wrap:hover .dplayer-bar .dplayer-played .dplayer-thumb {
  transform: scale(1);
}
.dplayer-controller .dplayer-bar-wrap:hover .dplayer-highlight {
  display: block;
  width: 8px;
  transform: translateX(-4px);
  top: 4px;
  height: 40%;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-highlight {
  z-index: 12;
  position: absolute;
  top: 5px;
  width: 6px;
  height: 20%;
  border-radius: 6px;
  background-color: #fff;
  text-align: center;
  transform: translateX(-3px);
  transition: all 0.2s ease-in-out;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-highlight:hover .dplayer-highlight-text {
  display: block;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-highlight:hover ~ .dplayer-bar-preview {
  opacity: 0;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-highlight:hover ~ .dplayer-bar-time {
  opacity: 0;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-highlight .dplayer-highlight-text {
  display: none;
  position: absolute;
  left: 50%;
  top: -24px;
  padding: 5px 8px;
  background-color: rgba(0, 0, 0, 0.62);
  color: #fff;
  border-radius: 4px;
  font-size: 12px;
  white-space: nowrap;
  transform: translateX(-50%);
}
.dplayer-controller .dplayer-bar-wrap .dplayer-bar-preview {
  position: absolute;
  background: #fff;
  pointer-events: none;
  display: none;
  background-size: 16000px 100%;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-bar-preview-canvas {
  position: absolute;
  width: 100%;
  height: 100%;
  z-index: 1;
  pointer-events: none;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-bar-time {
  position: absolute;
  left: 0px;
  top: -20px;
  border-radius: 4px;
  padding: 5px 7px;
  background-color: rgba(0, 0, 0, 0.62);
  color: #fff;
  font-size: 12px;
  text-align: center;
  opacity: 1;
  transition: opacity 0.1s ease-in-out;
  word-wrap: normal;
  word-break: normal;
  z-index: 2;
  pointer-events: none;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-bar-time.hidden {
  opacity: 0;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-bar {
  position: relative;
  height: 3px;
  width: 100%;
  background: rgba(255, 255, 255, 0.2);
  cursor: pointer;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-bar .dplayer-loaded {
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.4);
  height: 3px;
  transition: all 0.5s ease;
  will-change: width;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-bar .dplayer-played {
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  height: 3px;
  will-change: width;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-bar .dplayer-played .dplayer-thumb {
  position: absolute;
  top: 0;
  right: 5px;
  margin-top: -4px;
  margin-right: -10px;
  height: 11px;
  width: 11px;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s ease-in-out;
  transform: scale(0);
}
.dplayer-controller .dplayer-icons {
  height: 38px;
  position: absolute;
  bottom: 0;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box {
  display: none;
  position: absolute;
  transition: all 0.3s ease-in-out;
  z-index: 2;
  height: 38px;
  bottom: 0;
  left: 20px;
  right: 20px;
  color: #fff;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-icon {
  padding: 7px;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-icon {
  position: absolute;
  left: 0;
  top: 0;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-send-icon {
  position: absolute;
  right: 0;
  top: 0;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box {
  position: absolute;
  background: rgba(28, 28, 28, 0.9);
  bottom: 41px;
  left: 0;
  box-shadow: 0 0 25px rgba(0, 0, 0, 0.3);
  border-radius: 4px;
  padding: 10px 10px 16px;
  font-size: 14px;
  width: 204px;
  transition: all 0.3s ease-in-out;
  transform: scale(0);
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box.dplayer-comment-setting-open {
  transform: scale(1);
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box input[type=radio] {
  display: none;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box label {
  cursor: pointer;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-title {
  font-size: 13px;
  color: #fff;
  line-height: 30px;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-type {
  font-size: 0;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-type .dplayer-comment-setting-title {
  margin-bottom: 6px;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-type label:nth-child(2) span {
  border-radius: 4px 0 0 4px;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-type label:nth-child(4) span {
  border-radius: 0 4px 4px 0;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-type span {
  width: 33%;
  padding: 4px 6px;
  line-height: 16px;
  display: inline-block;
  font-size: 12px;
  color: #fff;
  border: 1px solid #fff;
  margin-right: -1px;
  box-sizing: border-box;
  text-align: center;
  cursor: pointer;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-type input:checked + span {
  background: #E4E4E6;
  color: #1c1c1c;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-color {
  font-size: 0;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-color label {
  font-size: 0;
  padding: 6px;
  display: inline-block;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-color span {
  width: 22px;
  height: 22px;
  display: inline-block;
  border-radius: 50%;
  box-sizing: border-box;
  cursor: pointer;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-color span:hover {
  animation: my-face 5s infinite ease-in-out;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-input {
  outline: none;
  border: none;
  padding: 8px 31px;
  font-size: 14px;
  line-height: 18px;
  text-align: center;
  border-radius: 4px;
  background: none;
  margin: 0;
  height: 100%;
  box-sizing: border-box;
  width: 100%;
  color: #fff;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-input::-moz-placeholder {
  color: #fff;
  opacity: 0.8;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-input::placeholder {
  color: #fff;
  opacity: 0.8;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-input::-ms-clear {
  display: none;
}
.dplayer-controller .dplayer-icons.dplayer-icons-left .dplayer-icon {
  padding: 7px;
}
.dplayer-controller .dplayer-icons.dplayer-icons-right {
  right: 20px;
}
.dplayer-controller .dplayer-icons.dplayer-icons-right .dplayer-icon {
  padding: 8px;
}
.dplayer-controller .dplayer-icons .dplayer-time,
.dplayer-controller .dplayer-icons .dplayer-live-badge {
  line-height: 38px;
  color: #eee;
  text-shadow: 0 0 2px rgba(0, 0, 0, 0.5);
  vertical-align: middle;
  font-size: 13px;
  cursor: default;
}
.dplayer-controller .dplayer-icons .dplayer-live-dot {
  display: inline-block;
  width: 6px;
  height: 6px;
  vertical-align: 4%;
  margin-right: 5px;
  content: '';
  border-radius: 6px;
}
.dplayer-controller .dplayer-icons .dplayer-icon {
  width: 40px;
  height: 100%;
  border: none;
  background-color: transparent;
  outline: none;
  cursor: pointer;
  vertical-align: middle;
  box-sizing: border-box;
  display: inline-block;
}
.dplayer-controller .dplayer-icons .dplayer-icon .dplayer-icon-content {
  transition: all 0.2s ease-in-out;
  opacity: 0.8;
}
.dplayer-controller .dplayer-icons .dplayer-icon:hover .dplayer-icon-content {
  opacity: 1;
}
.dplayer-controller .dplayer-icons .dplayer-icon.dplayer-quality-icon {
  color: #fff;
  width: auto;
  line-height: 22px;
  font-size: 14px;
}
.dplayer-controller .dplayer-icons .dplayer-icon.dplayer-comment-icon {
  padding: 10px 9px 9px;
}
.dplayer-controller .dplayer-icons .dplayer-icon.dplayer-setting-icon {
  padding-top: 8.5px;
}
.dplayer-controller .dplayer-icons .dplayer-icon.dplayer-volume-icon {
  width: 43px;
}
.dplayer-controller .dplayer-icons .dplayer-volume {
  position: relative;
  display: inline-block;
  cursor: pointer;
  height: 100%;
}
.dplayer-controller .dplayer-icons .dplayer-volume:hover .dplayer-volume-bar-wrap .dplayer-volume-bar {
  width: 45px;
}
.dplayer-controller .dplayer-icons .dplayer-volume:hover .dplayer-volume-bar-wrap .dplayer-volume-bar .dplayer-volume-bar-inner .dplayer-thumb {
  transform: scale(1);
}
.dplayer-controller .dplayer-icons .dplayer-volume.dplayer-volume-active .dplayer-volume-bar-wrap .dplayer-volume-bar {
  width: 45px;
}
.dplayer-controller .dplayer-icons .dplayer-volume.dplayer-volume-active .dplayer-volume-bar-wrap .dplayer-volume-bar .dplayer-volume-bar-inner .dplayer-thumb {
  transform: scale(1);
}
.dplayer-controller .dplayer-icons .dplayer-volume .dplayer-volume-bar-wrap {
  display: inline-block;
  margin: 0 10px 0 -5px;
  vertical-align: middle;
  height: 100%;
}
.dplayer-controller .dplayer-icons .dplayer-volume .dplayer-volume-bar-wrap .dplayer-volume-bar {
  position: relative;
  top: 17px;
  width: 0;
  height: 3px;
  background: #aaa;
  transition: all 0.3s ease-in-out;
}
.dplayer-controller .dplayer-icons .dplayer-volume .dplayer-volume-bar-wrap .dplayer-volume-bar .dplayer-volume-bar-inner {
  position: absolute;
  bottom: 0;
  left: 0;
  height: 100%;
  transition: all 0.1s ease;
  will-change: width;
}
.dplayer-controller .dplayer-icons .dplayer-volume .dplayer-volume-bar-wrap .dplayer-volume-bar .dplayer-volume-bar-inner .dplayer-thumb {
  position: absolute;
  top: 0;
  right: 5px;
  margin-top: -4px;
  margin-right: -10px;
  height: 11px;
  width: 11px;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s ease-in-out;
  transform: scale(0);
}
.dplayer-controller .dplayer-icons .dplayer-subtitle-btn {
  display: inline-block;
  height: 100%;
}
.dplayer-controller .dplayer-icons .dplayer-subtitles {
  display: inline-block;
  height: 100%;
}
.dplayer-controller .dplayer-icons .dplayer-subtitles .dplayer-subtitles-box {
  position: absolute;
  right: 0;
  bottom: 50px;
  transform: scale(0);
  width: -moz-fit-content;
  width: fit-content;
  max-width: 240px;
  min-width: 120px;
  border-radius: 2px;
  background: rgba(28, 28, 28, 0.9);
  padding: 7px 0;
  transition: all 0.3s ease-in-out;
  overflow: auto;
  z-index: 2;
}
.dplayer-controller .dplayer-icons .dplayer-subtitles .dplayer-subtitles-box.dplayer-subtitles-panel {
  display: block;
}
.dplayer-controller .dplayer-icons .dplayer-subtitles .dplayer-subtitles-box.dplayer-subtitles-box-open {
  transform: scale(1);
}
.dplayer-controller .dplayer-icons .dplayer-subtitles .dplayer-subtitles-item {
  height: 30px;
  padding: 5px 10px;
  box-sizing: border-box;
  cursor: pointer;
  position: relative;
}
.dplayer-controller .dplayer-icons .dplayer-subtitles .dplayer-subtitles-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
}
.dplayer-controller .dplayer-icons .dplayer-setting {
  display: inline-block;
  height: 100%;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box {
  position: absolute;
  right: 0;
  bottom: 50px;
  transform: scale(0);
  width: 150px;
  border-radius: 2px;
  background: rgba(28, 28, 28, 0.9);
  padding: 7px 0;
  transition: all 0.3s ease-in-out;
  overflow: hidden;
  z-index: 2;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box > div {
  display: none;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box > div.dplayer-setting-origin-panel {
  display: block;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box.dplayer-setting-box-open {
  transform: scale(1);
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box.dplayer-setting-box-narrow {
  width: 70px;
  text-align: center;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box.dplayer-setting-box-speed .dplayer-setting-origin-panel {
  display: none;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box.dplayer-setting-box-speed .dplayer-setting-speed-panel {
  display: block;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-item,
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-speed-item {
  height: 30px;
  padding: 5px 10px;
  box-sizing: border-box;
  cursor: pointer;
  position: relative;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-item:hover,
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-speed-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku {
  padding: 5px 0;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku .dplayer-label {
  padding: 0 10px;
  display: inline;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku:hover .dplayer-label {
  display: none;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku:hover .dplayer-danmaku-bar-wrap {
  display: inline-block;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku.dplayer-setting-danmaku-active .dplayer-label {
  display: none;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku.dplayer-setting-danmaku-active .dplayer-danmaku-bar-wrap {
  display: inline-block;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku .dplayer-danmaku-bar-wrap {
  padding: 0 10px;
  box-sizing: border-box;
  display: none;
  vertical-align: middle;
  height: 100%;
  width: 100%;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku .dplayer-danmaku-bar-wrap .dplayer-danmaku-bar {
  position: relative;
  top: 8.5px;
  width: 100%;
  height: 3px;
  background: #fff;
  transition: all 0.3s ease-in-out;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku .dplayer-danmaku-bar-wrap .dplayer-danmaku-bar .dplayer-danmaku-bar-inner {
  position: absolute;
  bottom: 0;
  left: 0;
  height: 100%;
  transition: all 0.1s ease;
  background: #aaa;
  will-change: width;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku .dplayer-danmaku-bar-wrap .dplayer-danmaku-bar .dplayer-danmaku-bar-inner .dplayer-thumb {
  position: absolute;
  top: 0;
  right: 5px;
  margin-top: -4px;
  margin-right: -10px;
  height: 11px;
  width: 11px;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s ease-in-out;
  background: #aaa;
}
.dplayer-controller .dplayer-icons .dplayer-full {
  display: inline-block;
  height: 100%;
  position: relative;
}
.dplayer-controller .dplayer-icons .dplayer-full:hover .dplayer-full-in-icon {
  display: block;
}
.dplayer-controller .dplayer-icons .dplayer-full .dplayer-full-in-icon {
  position: absolute;
  top: -30px;
  z-index: 1;
  display: none;
}
.dplayer-controller .dplayer-icons .dplayer-quality {
  position: relative;
  display: inline-block;
  height: 100%;
  z-index: 2;
}
.dplayer-controller .dplayer-icons .dplayer-quality:hover .dplayer-quality-list {
  display: block;
}
.dplayer-controller .dplayer-icons .dplayer-quality:hover .dplayer-quality-mask {
  display: block;
}
.dplayer-controller .dplayer-icons .dplayer-quality .dplayer-quality-mask {
  display: none;
  position: absolute;
  bottom: 38px;
  left: -18px;
  width: 80px;
  padding-bottom: 12px;
}
.dplayer-controller .dplayer-icons .dplayer-quality .dplayer-quality-list {
  display: none;
  font-size: 12px;
  width: 80px;
  border-radius: 2px;
  background: rgba(28, 28, 28, 0.9);
  padding: 5px 0;
  transition: all 0.3s ease-in-out;
  overflow: hidden;
  color: #fff;
  text-align: center;
}
.dplayer-controller .dplayer-icons .dplayer-quality .dplayer-quality-item {
  height: 25px;
  box-sizing: border-box;
  cursor: pointer;
  line-height: 25px;
}
.dplayer-controller .dplayer-icons .dplayer-quality .dplayer-quality-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
}
.dplayer-controller .dplayer-icons .dplayer-comment {
  display: inline-block;
  height: 100%;
}
.dplayer-controller .dplayer-icons .dplayer-label {
  color: #eee;
  font-size: 13px;
  display: inline-block;
  vertical-align: middle;
  white-space: nowrap;
}
.dplayer-controller .dplayer-icons .dplayer-toggle {
  width: 32px;
  height: 20px;
  text-align: center;
  font-size: 0;
  vertical-align: middle;
  position: absolute;
  top: 5px;
  right: 10px;
}
.dplayer-controller .dplayer-icons .dplayer-toggle input {
  max-height: 0;
  max-width: 0;
  display: none;
}
.dplayer-controller .dplayer-icons .dplayer-toggle input + label {
  display: inline-block;
  position: relative;
  box-shadow: #dfdfdf 0 0 0 0 inset;
  border: 1px solid #dfdfdf;
  height: 20px;
  width: 32px;
  border-radius: 10px;
  box-sizing: border-box;
  cursor: pointer;
  transition: 0.2s ease-in-out;
}
.dplayer-controller .dplayer-icons .dplayer-toggle input + label:before {
  content: "";
  position: absolute;
  display: block;
  height: 18px;
  width: 18px;
  top: 0;
  left: 0;
  border-radius: 15px;
  transition: 0.2s ease-in-out;
}
.dplayer-controller .dplayer-icons .dplayer-toggle input + label:after {
  content: "";
  position: absolute;
  display: block;
  left: 0;
  top: 0;
  border-radius: 15px;
  background: #fff;
  transition: 0.2s ease-in-out;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.4);
  height: 18px;
  width: 18px;
}
.dplayer-controller .dplayer-icons .dplayer-toggle input:checked + label {
  border-color: rgba(255, 255, 255, 0.5);
}
.dplayer-controller .dplayer-icons .dplayer-toggle input:checked + label:before {
  width: 30px;
  background: rgba(255, 255, 255, 0.5);
}
.dplayer-controller .dplayer-icons .dplayer-toggle input:checked + label:after {
  left: 12px;
}
.dplayer-mobile-play {
  display: none;
  width: 50px;
  height: 50px;
  border: none;
  background-color: transparent;
  outline: none;
  cursor: pointer;
  box-sizing: border-box;
  bottom: 0;
  opacity: 0.8;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}
.dplayer-danmaku {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  font-size: 22px;
  color: #fff;
}
.dplayer-danmaku .dplayer-danmaku-item {
  display: inline-block;
  pointer-events: none;
  -webkit-user-select: none;
     -moz-user-select: none;
          user-select: none;
  cursor: default;
  white-space: nowrap;
  text-shadow: 0.5px 0.5px 0.5px rgba(0, 0, 0, 0.5);
}
.dplayer-danmaku .dplayer-danmaku-item--demo {
  position: absolute;
  visibility: hidden;
}
.dplayer-danmaku .dplayer-danmaku-right {
  position: absolute;
  right: 0;
  transform: translateX(100%);
}
.dplayer-danmaku .dplayer-danmaku-right.dplayer-danmaku-move {
  will-change: transform;
  animation-name: 'danmaku';
  animation-timing-function: linear;
  animation-play-state: paused;
}
@keyframes danmaku {
  from {
    transform: translateX(100%);
  }
}
.dplayer-danmaku .dplayer-danmaku-top,
.dplayer-danmaku .dplayer-danmaku-bottom {
  position: absolute;
  width: 100%;
  text-align: center;
  visibility: hidden;
}
.dplayer-danmaku .dplayer-danmaku-top.dplayer-danmaku-move,
.dplayer-danmaku .dplayer-danmaku-bottom.dplayer-danmaku-move {
  will-change: visibility;
  animation-name: 'danmaku-center';
  animation-timing-function: linear;
  animation-play-state: paused;
}
@keyframes danmaku-center {
  from {
    visibility: visible;
  }
  to {
    visibility: visible;
  }
}
.dplayer-logo {
  pointer-events: none;
  position: absolute;
  left: 20px;
  top: 20px;
  max-width: 50px;
  max-height: 50px;
}
.dplayer-logo img {
  max-width: 100%;
  max-height: 100%;
  background: none;
}
.dplayer-menu {
  position: absolute;
  width: 170px;
  border-radius: 2px;
  background: rgba(28, 28, 28, 0.85);
  padding: 5px 0;
  overflow: hidden;
  z-index: 3;
  display: none;
}
.dplayer-menu.dplayer-menu-show {
  display: block;
}
.dplayer-menu .dplayer-menu-item {
  height: 30px;
  box-sizing: border-box;
  cursor: pointer;
}
.dplayer-menu .dplayer-menu-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
}
.dplayer-menu .dplayer-menu-item a {
  padding: 0 10px;
  line-height: 30px;
  color: #eee;
  font-size: 13px;
  display: inline-block;
  vertical-align: middle;
  width: 100%;
  box-sizing: border-box;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}
.dplayer-menu .dplayer-menu-item a:hover {
  text-decoration: none;
}
.dplayer-notice-list {
  position: absolute;
  bottom: 60px;
  left: 20px;
}
.dplayer-notice-list .dplayer-notice {
  border-radius: 2px;
  background: rgba(28, 28, 28, 0.9);
  transition: all 0.3s ease-in-out;
  overflow: hidden;
  color: #fff;
  display: table;
  pointer-events: none;
  animation: showNotice 0.3s ease 1 forwards;
}
.dplayer-notice-list .remove-notice {
  animation: removeNotice 0.3s ease 1 forwards;
}
@keyframes showNotice {
  from {
    padding: 0;
    font-size: 0;
    margin-top: 0;
  }
  to {
    padding: 7px 20px;
    font-size: 14px;
    margin-top: 5px;
  }
}
@keyframes removeNotice {
  0% {
    padding: 7px 20px;
    font-size: 14px;
    margin-top: 5px;
  }
  20% {
    font-size: 12px;
  }
  21% {
    font-size: 0;
    padding: 7px 10px;
  }
  100% {
    padding: 0;
    margin-top: 0;
    font-size: 0;
  }
}
.dplayer-subtitle {
  position: absolute;
  bottom: 40px;
  width: 90%;
  left: 5%;
  text-align: center;
  color: #fff;
  text-shadow: 0.5px 0.5px 0.5px rgba(0, 0, 0, 0.5);
  font-size: 20px;
}
.dplayer-subtitle.dplayer-subtitle-hide {
  display: none;
}
.dplayer-mask {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 1;
  display: none;
}
.dplayer-mask.dplayer-mask-show {
  display: block;
}
.dplayer-video-wrap {
  position: relative;
  background: #000;
  font-size: 0;
  width: 100%;
  height: 100%;
}
.dplayer-video-wrap .dplayer-video {
  width: 100%;
  height: 100%;
  display: none;
}
.dplayer-video-wrap .dplayer-video-current {
  display: block;
}
.dplayer-video-wrap .dplayer-video-prepare {
  display: none;
}
.dplayer-info-panel {
  position: absolute;
  top: 10px;
  left: 10px;
  width: 400px;
  background: rgba(28, 28, 28, 0.8);
  padding: 10px;
  color: #fff;
  font-size: 12px;
  border-radius: 2px;
}
.dplayer-info-panel-hide {
  display: none;
}
.dplayer-info-panel .dplayer-info-panel-close {
  cursor: pointer;
  position: absolute;
  right: 10px;
  top: 10px;
}
.dplayer-info-panel .dplayer-info-panel-item > span {
  display: inline-block;
  vertical-align: middle;
  line-height: 15px;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}
.dplayer-info-panel .dplayer-info-panel-item-title {
  width: 100px;
  text-align: right;
  margin-right: 10px;
}
.dplayer-info-panel .dplayer-info-panel-item-data {
  width: 260px;
}
`,"",{version:3,sources:["webpack://./src/css/global.less","webpack://./src/css/index.less","webpack://./src/css/player.less","webpack://./src/css/balloon.less","webpack://./src/css/bezel.less","webpack://./src/css/notice.less","webpack://./src/css/controller.less","webpack://./src/css/danmaku.less","webpack://./src/css/logo.less","webpack://./src/css/menu.less","webpack://./src/css/subtitle.less","webpack://./src/css/video.less","webpack://./src/css/info-panel.less"],names:[],mappings:"AAAA;EACI;IACI,6CAAA;ECEN;EDAE;IACI,+CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,+CAAA;ECEN;EDAE;IACI,6CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,+CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,6CAAA;ECEN;EDAE;IACI,6CAAA;ECEN;EDAE;IACI,6CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,6CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,6CAAA;ECEN;EDAE;IACI,6CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,6CAAA;ECEN;EDAE;IACI,6CAAA;ECEN;EDAE;IACI,6CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,+CAAA;ECEN;EDAE;IACI,6CAAA;ECEN;EDAE;IACI,6CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,6CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,6CAAA;ECEN;EDAE;IACI,+CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,6CAAA;ECEN;EDAE;IACI,8CAAA;ECEN;EDAE;IACI,+CAAA;ECEN;EDAE;;IAEI,uCAAA;ECEN;AACF;ACzJA;EACI,kBAAA;EACA,gBAAA;EACA,yBAAA;KAAA,sBAAA;UAAA,iBAAA;EACA,cAAA;AD2JJ;AC/JA;EAOQ,uBAAA;AD2JR;AClKA;EAWQ,WAAA;EACA,YAAA;AD0JR;ACtKA;;EAgBY,UAAA;AD0JZ;ACtJI;EACI,WAAA;EACA,YAAA;EACA,gBAAA;EACA,eAAA;EACA,eAAA;EACA,OAAA;EACA,MAAA;EACA,SAAA;EACA,UAAA;EACA,0BAAA;ADwJR;ACpJI;;;EAKY,aAAA;ADoJhB;ACzJI;EAUQ,aAAA;ADkJZ;AC5JI;EAcQ,aAAA;ADiJZ;AC7II;EAEQ,aAAA;AD8IZ;AChJI;EAKQ,aAAA;AD8IZ;ACnJI;EAQQ,aAAA;AD8IZ;ACtJI;EAWQ,aAAA;AD8IZ;AC3IQ;EAEQ,aAAA;AD4IhB;ACvII;EAEQ,eAAA;ADwIZ;AC1II;EAKQ,cAAA;ADwIZ;ACpII;EAEQ,6BAAA;ADqIZ;AClIQ;EAAA;IAEQ,UAAA;EDoId;ECtIM;IAKQ,UAAA;EDoId;ECjIU;IAEQ,UAAA;EDkIlB;ECpIU;IAKQ,UAAA;EDkIlB;AACF;AC7HI;EAEQ,cAAA;AD8HZ;AC1HI;;;;EAIQ,4BAAA;AD4HZ;ACxHI;EACI,YAAA;AD0HR;AC3HI;EAIQ,UAAA;EACA,2BAAA;AD0HZ;AC/HI;EAQQ,UAAA;EACA,2BAAA;AD0HZ;ACvHI;EAEQ,UAAA;ADwHZ;AC1HI;EAKQ,UAAA;ADwHZ;ACrHI;EAKI,sBAAA;EACA,uBAAA;ADuHR;AC7HI;EACI,eAAA;EACA,eAAA;EACA,OAAA;EACA,MAAA;ADyHR;ACrHI;;;;;EAOY,aAAA;ADqHhB;AC5HI;EAUY,gBAAA;EACA,qBAAA;ADqHhB;AChII;EAgBQ,aAAA;ADmHZ;AChHQ;EAEQ,aAAA;ADiHhB;ACtII;EA0BQ,cAAA;AD+GZ;ACzGA;EACI,eAAA;EACA,MAAA;EACA,OAAA;EACA,SAAA;EACA,UAAA;AD2GJ;AElSA;EACI,aAAA;AFoSJ;AEjSA;EACI,oBAAA;EACA,iCAAA;AFmSJ;AEhSA;EACI,gBAAA;AFkSJ;AG9SA;EACI,kBAAA;EACA,OAAA;EACA,QAAA;EACA,MAAA;EACA,SAAA;EACA,eAAA;EACA,WAAA;EACA,oBAAA;AHgTJ;AGxTA;EAUQ,kBAAA;EACA,QAAA;EACA,SAAA;EACA,uBAAA;EACA,YAAA;EACA,WAAA;EACA,aAAA;EACA,sBAAA;EACA,8BAAA;EACA,kBAAA;EACA,UAAA;EACA,oBAAA;AHiTR;AGhTQ;EACI,iCAAA;AHkTZ;AGhTQ;EACI;IACI,UAAA;IACA,mBAAA;EHkTd;EGhTU;IACI,UAAA;IACA,mBAAA;EHkTd;AACF;AGnVA;EAqCQ,kBAAA;EACA,QAAA;EACA,gBAAA;EACA,WAAA;EACA,kBAAA;EACA,eAAA;EACA,iBAAA;EACA,0CAAA;AHiTR;AG7VA;EA+CQ,aAAA;EACA,kBAAA;EACA,QAAA;EACA,SAAA;EACA,uBAAA;EACA,YAAA;EACA,WAAA;EACA,oBAAA;AHiTR;AGvWA;EAwDY,aAAA;AHkTZ;AG1WA;EA2DY,uDAAA;EACA,UAAA;EACA,yBAAA;AHkTZ;AI/WC;EDgEmB,qBAAA;AHkTpB;AIlXC;EDgEmB,qBAAA;AHqTpB;AIrXC;EDgEmB,qBAAA;AHwTpB;AIxXC;EDgEmB,qBAAA;AH2TpB;AI3XC;EDgEmB,qBAAA;AH8TpB;AI9XC;EDgEmB,qBAAA;AHiUpB;AIjYC;EDgEmB,qBAAA;AHoUpB;AGhUQ;EACI;IACI,YAAA;IACA,0BAAA;EHkUd;EGhUU;IACI,aAAA;IACA,0BAAA;EHkUd;EGhUU;IACI,aAAA;IACA,4BAAA;EHkUd;AACF;AKlZA;EACI,mEAAA;EACA,YAAA;EACA,WAAA;EACA,kBAAA;EACA,SAAA;EACA,yBAAA;ALoZJ;AKjZA;EACI,kBAAA;EACA,SAAA;EACA,OAAA;EACA,QAAA;EACA,YAAA;EACA,eAAA;EACA,yBAAA;KAAA,sBAAA;UAAA,iBAAA;EACA,yBAAA;ALmZJ;AKlZI;EAEQ,aAAA;ALmZZ;AKrZI;EAKQ,cAAA;ALmZZ;AKjaA;EAkBQ,cAAA;EACA,eAAA;EACA,kBAAA;EACA,YAAA;EACA,wBAAA;EACA,WAAA;ALkZR;AKjZQ;EAEQ,mBAAA;ALkZhB;AKpZQ;EAKQ,cAAA;EACA,UAAA;EACA,2BAAA;EACA,QAAA;EACA,WAAA;ALkZhB;AKnbA;EAqCY,WAAA;EACA,kBAAA;EACA,QAAA;EACA,UAAA;EACA,WAAA;EACA,kBAAA;EACA,sBAAA;EACA,kBAAA;EACA,2BAAA;EACA,gCAAA;ALiZZ;AKhZY;EAEQ,cAAA;ALiZpB;AK/YgB;EACI,UAAA;ALiZpB;AK/YgB;EACI,UAAA;ALiZpB;AKxcA;EA2DgB,aAAA;EACA,kBAAA;EACA,SAAA;EACA,UAAA;EACA,gBAAA;EACA,qCAAA;EACA,WAAA;EACA,kBAAA;EACA,eAAA;EACA,mBAAA;EACA,2BAAA;ALgZhB;AKrdA;EAyEY,kBAAA;EACA,gBAAA;EACA,oBAAA;EACA,aAAA;EACA,6BAAA;AL+YZ;AK5dA;EAgFY,kBAAA;EACA,WAAA;EACA,YAAA;EACA,UAAA;EACA,oBAAA;AL+YZ;AKneA;EA0FY,kBAAA;EACA,SAAA;EACA,UAAA;EACA,kBAAA;EACA,gBAAA;EACA,qCAAA;EACA,WAAA;EACA,eAAA;EACA,kBAAA;EACA,UAAA;EACA,oCAAA;EACA,iBAAA;EACA,kBAAA;EACA,UAAA;EACA,oBAAA;AL4YZ;AK7ZY;EACI,UAAA;AL+ZhB;AKvfA;EA2GY,kBAAA;EACA,WAAA;EACA,WAAA;EACA,oCAAA;EACA,eAAA;AL+YZ;AK9fA;EAiHgB,kBAAA;EACA,OAAA;EACA,MAAA;EACA,SAAA;EACA,oCAAA;EACA,WAAA;EACA,yBAAA;EACA,kBAAA;ALgZhB;AKxgBA;EA2HgB,kBAAA;EACA,OAAA;EACA,MAAA;EACA,SAAA;EACA,WAAA;EACA,kBAAA;ALgZhB;AKhhBA;EAkIoB,kBAAA;EACA,MAAA;EACA,UAAA;EACA,gBAAA;EACA,mBAAA;EACA,YAAA;EACA,WAAA;EACA,kBAAA;EACA,eAAA;EACA,gCAAA;EACA,mBAAA;ALiZpB;AK7hBA;EAkJQ,YAAA;EACA,kBAAA;EACA,SAAA;AL8YR;AK7YQ;EACI,aAAA;EACA,kBAAA;EACA,gCAAA;EACA,UAAA;EACA,YAAA;EACA,SAAA;EACA,UAAA;EACA,WAAA;EACA,WAAA;AL+YZ;AKxZQ;EAWQ,YAAA;ALgZhB;AK3ZQ;EAcQ,kBAAA;EACA,OAAA;EACA,MAAA;ALgZhB;AKhaQ;EAmBQ,kBAAA;EACA,QAAA;EACA,MAAA;ALgZhB;AKraQ;EAwBQ,kBAAA;EACA,iCAAA;EACA,YAAA;EACA,OAAA;EACA,uCAAA;EACA,kBAAA;EACA,uBAAA;EACA,eAAA;EACA,YAAA;EACA,gCAAA;EACA,mBAAA;ALgZhB;AK/YgB;EACI,mBAAA;ALiZpB;AKrbQ;EAuCY,aAAA;ALiZpB;AKxbQ;EA0CY,eAAA;ALiZpB;AK3bQ;EA6CY,eAAA;EACA,WAAA;EACA,iBAAA;ALiZpB;AKhcQ;EAkDY,YAAA;ALiZpB;AKncQ;EAoDgB,kBAAA;ALkZxB;AK/YwB;EAEQ,0BAAA;ALgZhC;AK7YwB;EAEQ,0BAAA;AL8YhC;AK5cQ;EAmEgB,UAAA;EACA,gBAAA;EACA,iBAAA;EACA,qBAAA;EACA,eAAA;EACA,WAAA;EACA,sBAAA;EACA,kBAAA;EACA,sBAAA;EACA,kBAAA;EACA,eAAA;AL4YxB;AKzdQ;EAgFgB,mBAAA;EACA,cAAA;AL4YxB;AK7dQ;EAqFY,YAAA;AL2YpB;AKheQ;EAuFgB,YAAA;EACA,YAAA;EACA,qBAAA;AL4YxB;AKreQ;EA4FgB,WAAA;EACA,YAAA;EACA,qBAAA;EACA,kBAAA;EACA,sBAAA;EACA,eAAA;AL4YxB;AK3YwB;EACI,0CAAA;AL6Y5B;AKhfQ;EAyGQ,aAAA;EACA,YAAA;EACA,iBAAA;EACA,eAAA;EACA,iBAAA;EACA,kBAAA;EACA,kBAAA;EACA,gBAAA;EACA,SAAA;EACA,YAAA;EACA,sBAAA;EACA,WAAA;EACA,WAAA;AL0YhB;AKzYgB;EACI,WAAA;EACA,YAAA;AL2YpB;AK7YgB;EACI,WAAA;EACA,YAAA;AL2YpB;AKzYgB;EACI,aAAA;AL2YpB;AKvYQ;EAEQ,YAAA;ALwYhB;AKrYQ;EACI,WAAA;ALuYZ;AKxYQ;EAGQ,YAAA;ALwYhB;AKpqBA;;EAiSY,iBAAA;EACA,WAAA;EACA,uCAAA;EACA,sBAAA;EACA,eAAA;EACA,eAAA;ALuYZ;AK7qBA;EAySY,qBAAA;EACA,UAAA;EACA,WAAA;EACA,kBAAA;EACA,iBAAA;EACA,WAAA;EACA,kBAAA;ALuYZ;AKtrBA;EAkTY,WAAA;EACA,YAAA;EACA,YAAA;EACA,6BAAA;EACA,aAAA;EACA,eAAA;EACA,sBAAA;EACA,sBAAA;EACA,qBAAA;ALuYZ;AKjsBA;EA4TgB,gCAAA;EACA,YAAA;ALwYhB;AKtYY;EAEQ,UAAA;ALuYpB;AKpYY;EACI,WAAA;EACA,WAAA;EACA,iBAAA;EACA,eAAA;ALsYhB;AKpYY;EACI,qBAAA;ALsYhB;AKpYY;EACI,kBAAA;ALsYhB;AKpYY;EACI,WAAA;ALsYhB;AKvtBA;EAqVY,kBAAA;EACA,qBAAA;EACA,eAAA;EACA,YAAA;ALqYZ;AKpYY;EAEQ,WAAA;ALqYpB;AKvYY;EAKQ,mBAAA;ALqYpB;AKlYY;EAEQ,WAAA;ALmYpB;AKrYY;EAKQ,mBAAA;ALmYpB;AKzuBA;EA0WgB,qBAAA;EACA,qBAAA;EACA,sBAAA;EACA,YAAA;ALkYhB;AK/uBA;EA+WoB,kBAAA;EACA,SAAA;EACA,QAAA;EACA,WAAA;EACA,gBAAA;EACA,gCAAA;ALmYpB;AKvvBA;EAsXwB,kBAAA;EACA,SAAA;EACA,OAAA;EACA,YAAA;EACA,yBAAA;EACA,kBAAA;ALoYxB;AK/vBA;EA6X4B,kBAAA;EACA,MAAA;EACA,UAAA;EACA,gBAAA;EACA,mBAAA;EACA,YAAA;EACA,WAAA;EACA,kBAAA;EACA,eAAA;EACA,gCAAA;EACA,mBAAA;ALqY5B;AK5wBA;EA8YY,qBAAA;EACA,YAAA;ALiYZ;AKhxBA;EAkZY,qBAAA;EACA,YAAA;ALiYZ;AKpxBA;EAqZgB,kBAAA;EACA,QAAA;EACA,YAAA;EACA,mBAAA;EACA,uBAAA;EAAA,kBAAA;EACA,gBAAA;EACA,gBAAA;EACA,kBAAA;EACA,iCAAA;EACA,cAAA;EACA,gCAAA;EACA,cAAA;EACA,UAAA;ALkYhB;AKjYgB;EACI,cAAA;ALmYpB;AKjYgB;EACI,mBAAA;ALmYpB;AKzyBA;EA0agB,YAAA;EACA,iBAAA;EACA,sBAAA;EACA,eAAA;EACA,kBAAA;ALkYhB;AKjYgB;EACI,0CAAA;ALmYpB;AKnzBA;EAqbY,qBAAA;EACA,YAAA;ALiYZ;AKvzBA;EAwbgB,kBAAA;EACA,QAAA;EACA,YAAA;EACA,mBAAA;EACA,YAAA;EACA,kBAAA;EACA,iCAAA;EACA,cAAA;EACA,gCAAA;EACA,gBAAA;EACA,UAAA;ALkYhB;AKjYgB;EACI,aAAA;ALmYpB;AKlYoB;EACI,cAAA;ALoYxB;AKjYgB;EACI,mBAAA;ALmYpB;AKjYgB;EACI,WAAA;EACA,kBAAA;ALmYpB;AKjYgB;EAEQ,aAAA;ALkYxB;AKpYgB;EAKQ,cAAA;ALkYxB;AKv1BA;;EA2dgB,YAAA;EACA,iBAAA;EACA,sBAAA;EACA,eAAA;EACA,kBAAA;ALgYhB;AK/XgB;;EACI,0CAAA;ALkYpB;AKn2BA;EAqegB,cAAA;ALiYhB;AKt2BA;EAueoB,eAAA;EACA,eAAA;ALkYpB;AKhYgB;EAEQ,aAAA;ALiYxB;AKnYgB;EAKQ,qBAAA;ALiYxB;AK9XgB;EAEQ,aAAA;AL+XxB;AKjYgB;EAKQ,qBAAA;AL+XxB;AKt3BA;EA2foB,eAAA;EACA,sBAAA;EACA,aAAA;EACA,sBAAA;EACA,YAAA;EACA,WAAA;AL8XpB;AK93BA;EAkgBwB,kBAAA;EACA,UAAA;EACA,WAAA;EACA,WAAA;EACA,gBAAA;EACA,gCAAA;AL+XxB;AKt4BA;EAygB4B,kBAAA;EACA,SAAA;EACA,OAAA;EACA,YAAA;EACA,yBAAA;EACA,gBAAA;EACA,kBAAA;ALgY5B;AK/4BA;EAihBgC,kBAAA;EACA,MAAA;EACA,UAAA;EACA,gBAAA;EACA,mBAAA;EACA,YAAA;EACA,WAAA;EACA,kBAAA;EACA,eAAA;EACA,gCAAA;EACA,gBAAA;ALiYhC;AK55BA;EAmiBY,qBAAA;EACA,YAAA;EACA,kBAAA;AL4XZ;AK3XY;EAEQ,cAAA;AL4XpB;AKp6BA;EA4iBgB,kBAAA;EACA,UAAA;EACA,UAAA;EACA,aAAA;AL2XhB;AK16BA;EAmjBY,kBAAA;EACA,qBAAA;EACA,YAAA;EACA,UAAA;AL0XZ;AKzXY;EAEQ,cAAA;AL0XpB;AK5XY;EAKQ,cAAA;AL0XpB;AKt7BA;EAgkBgB,aAAA;EACA,kBAAA;EACA,YAAA;EACA,WAAA;EACA,WAAA;EACA,oBAAA;ALyXhB;AK97BA;EAwkBgB,aAAA;EACA,eAAA;EACA,WAAA;EACA,kBAAA;EACA,iCAAA;EACA,cAAA;EACA,gCAAA;EACA,gBAAA;EACA,WAAA;EACA,kBAAA;ALyXhB;AK18BA;EAolBgB,YAAA;EACA,sBAAA;EACA,eAAA;EACA,iBAAA;ALyXhB;AKxXgB;EACI,0CAAA;AL0XpB;AKn9BA;EA8lBY,qBAAA;EACA,YAAA;ALwXZ;AKv9BA;EAkmBY,WAAA;EACA,eAAA;EACA,qBAAA;EACA,sBAAA;EACA,mBAAA;ALwXZ;AK99BA;EAymBY,WAAA;EACA,YAAA;EACA,kBAAA;EACA,YAAA;EACA,sBAAA;EACA,kBAAA;EACA,QAAA;EACA,WAAA;ALwXZ;AKx+BA;EAknBgB,aAAA;EACA,YAAA;EACA,aAAA;ALyXhB;AK7+BA;EAunBgB,qBAAA;EACA,kBAAA;EACA,iCAAA;EACA,yBAAA;EACA,YAAA;EACA,WAAA;EACA,mBAAA;EACA,sBAAA;EACA,eAAA;EACA,4BAAA;ALyXhB;AKz/BA;EAmoBgB,WAAA;EACA,kBAAA;EACA,cAAA;EACA,YAAA;EACA,WAAA;EACA,MAAA;EACA,OAAA;EACA,mBAAA;EACA,4BAAA;ALyXhB;AKpgCA;EA8oBgB,WAAA;EACA,kBAAA;EACA,cAAA;EACA,OAAA;EACA,MAAA;EACA,mBAAA;EACA,gBAAA;EACA,4BAAA;EACA,wCAAA;EACA,YAAA;EACA,WAAA;ALyXhB;AKjhCA;EA2pBgB,sCAAA;ALyXhB;AKphCA;EA8pBgB,WAAA;EACA,oCAAA;ALyXhB;AKxhCA;EAkqBgB,UAAA;ALyXhB;AKnXA;EACI,aAAA;EACA,WAAA;EACA,YAAA;EACA,YAAA;EACA,6BAAA;EACA,aAAA;EACA,eAAA;EACA,sBAAA;EAEA,SAAA;EACA,YAAA;EACA,kBAAA;EACA,SAAA;EACA,QAAA;EACA,gCAAA;ALoXJ;AMpjCA;EACI,kBAAA;EACA,OAAA;EACA,QAAA;EACA,MAAA;EACA,SAAA;EACA,eAAA;EACA,WAAA;ANsjCJ;AM7jCA;EASQ,qBAAA;EACA,oBAAA;EACA,yBAAA;KAAA,sBAAA;UAAA,iBAAA;EACA,eAAA;EACA,mBAAA;EACA,iDAAA;ANujCR;AMtjCQ;EACI,kBAAA;EACA,kBAAA;ANwjCZ;AMzkCA;EAqBQ,kBAAA;EACA,QAAA;EACA,2BAAA;ANujCR;AMtjCQ;EACI,sBAAA;EACA,yBAAA;EACA,iCAAA;EACA,4BAAA;ANwjCZ;AMrjCI;EACI;IACI,2BAAA;ENujCV;AACF;AMzlCA;;EAsCQ,kBAAA;EACA,WAAA;EACA,kBAAA;EACA,kBAAA;ANujCR;AMtjCQ;;EACI,uBAAA;EACA,gCAAA;EACA,iCAAA;EACA,4BAAA;ANyjCZ;AMtjCI;EACI;IACI,mBAAA;ENwjCV;EMtjCM;IACI,mBAAA;ENwjCV;AACF;AO/mCA;EACI,oBAAA;EACA,kBAAA;EACA,UAAA;EACA,SAAA;EACA,eAAA;EACA,gBAAA;APinCJ;AOvnCA;EAQQ,eAAA;EACA,gBAAA;EACA,gBAAA;APknCR;AQ5nCA;EACI,kBAAA;EACA,YAAA;EACA,kBAAA;EACA,kCAAA;EACA,cAAA;EACA,gBAAA;EACA,UAAA;EACA,aAAA;AR8nCJ;AQ7nCI;EACI,cAAA;AR+nCR;AQzoCA;EAaQ,YAAA;EACA,sBAAA;EACA,eAAA;AR+nCR;AQ9nCQ;EACI,0CAAA;ARgoCZ;AQjpCA;EAqBY,eAAA;EACA,iBAAA;EACA,WAAA;EACA,eAAA;EACA,qBAAA;EACA,sBAAA;EACA,WAAA;EACA,sBAAA;EACA,mBAAA;EACA,uBAAA;EACA,gBAAA;AR+nCZ;AQ9nCY;EACI,qBAAA;ARgoChB;AIjqCA;EACI,kBAAA;EACA,YAAA;EACA,UAAA;AJmqCJ;AItqCA;EAMQ,kBAAA;EACA,iCAAA;EACA,gCAAA;EACA,gBAAA;EACA,WAAA;EACA,cAAA;EACA,oBAAA;EACA,0CAAA;AJmqCR;AIhrCA;EAiBQ,4CAAA;AJkqCR;AI9pCA;EACI;IACI,UAAA;IACA,YAAA;IACA,aAAA;EJgqCN;EI9pCE;IACI,iBAAA;IACA,eAAA;IACA,eAAA;EJgqCN;AACF;AI7pCA;EACI;IACI,iBAAA;IACA,eAAA;IACA,eAAA;EJ+pCN;EI7pCE;IACI,eAAA;EJ+pCN;EI7pCE;IACI,YAAA;IACA,iBAAA;EJ+pCN;EI7pCE;IACI,UAAA;IACA,aAAA;IACA,YAAA;EJ+pCN;AACF;ASltCA;EACI,kBAAA;EACA,YAAA;EACA,UAAA;EACA,QAAA;EACA,kBAAA;EACA,WAAA;EACA,iDAAA;EACA,eAAA;ATotCJ;ASntCI;EACI,aAAA;ATqtCR;AU/tCA;EACI,kBAAA;EACA,MAAA;EACA,SAAA;EACA,OAAA;EACA,QAAA;EACA,UAAA;EACA,aAAA;AViuCJ;AUhuCI;EACI,cAAA;AVkuCR;AU9tCA;EACI,kBAAA;EACA,gBAAA;EACA,YAAA;EACA,WAAA;EACA,YAAA;AVguCJ;AUruCA;EAOQ,WAAA;EACA,YAAA;EACA,aAAA;AViuCR;AU1uCA;EAYQ,cAAA;AViuCR;AU7uCA;EAeQ,aAAA;AViuCR;AW7vCA;EACI,kBAAA;EACA,SAAA;EACA,UAAA;EACA,YAAA;EACA,iCAAA;EACA,aAAA;EACA,WAAA;EACA,eAAA;EACA,kBAAA;AX+vCJ;AW7vCI;EACI,aAAA;AX+vCR;AW3wCA;EAgBQ,eAAA;EACA,kBAAA;EACA,WAAA;EACA,SAAA;AX8vCR;AW1vCQ;EACI,qBAAA;EACA,sBAAA;EACA,iBAAA;EACA,mBAAA;EACA,uBAAA;EACA,gBAAA;AX4vCZ;AWzxCA;EAkCQ,YAAA;EACA,iBAAA;EACA,kBAAA;AX0vCR;AW9xCA;EAwCQ,YAAA;AXyvCR",sourcesContent:[`@keyframes my-face {
    2% {
        transform: translate(0, 1.5px) rotate(1.5deg);
    }
    4% {
        transform: translate(0, -1.5px) rotate(-0.5deg);
    }
    6% {
        transform: translate(0, 1.5px) rotate(-1.5deg);
    }
    8% {
        transform: translate(0, -1.5px) rotate(-1.5deg);
    }
    10% {
        transform: translate(0, 2.5px) rotate(1.5deg);
    }
    12% {
        transform: translate(0, -0.5px) rotate(1.5deg);
    }
    14% {
        transform: translate(0, -1.5px) rotate(1.5deg);
    }
    16% {
        transform: translate(0, -0.5px) rotate(-1.5deg);
    }
    18% {
        transform: translate(0, 0.5px) rotate(-1.5deg);
    }
    20% {
        transform: translate(0, -1.5px) rotate(2.5deg);
    }
    22% {
        transform: translate(0, 0.5px) rotate(-1.5deg);
    }
    24% {
        transform: translate(0, 1.5px) rotate(1.5deg);
    }
    26% {
        transform: translate(0, 0.5px) rotate(0.5deg);
    }
    28% {
        transform: translate(0, 0.5px) rotate(1.5deg);
    }
    30% {
        transform: translate(0, -0.5px) rotate(2.5deg);
    }
    32% {
        transform: translate(0, 1.5px) rotate(-0.5deg);
    }
    34% {
        transform: translate(0, 1.5px) rotate(-0.5deg);
    }
    36% {
        transform: translate(0, -1.5px) rotate(2.5deg);
    }
    38% {
        transform: translate(0, 1.5px) rotate(-1.5deg);
    }
    40% {
        transform: translate(0, -0.5px) rotate(2.5deg);
    }
    42% {
        transform: translate(0, 2.5px) rotate(-1.5deg);
    }
    44% {
        transform: translate(0, 1.5px) rotate(0.5deg);
    }
    46% {
        transform: translate(0, -1.5px) rotate(2.5deg);
    }
    48% {
        transform: translate(0, -0.5px) rotate(0.5deg);
    }
    50% {
        transform: translate(0, 0.5px) rotate(0.5deg);
    }
    52% {
        transform: translate(0, 2.5px) rotate(2.5deg);
    }
    54% {
        transform: translate(0, -1.5px) rotate(1.5deg);
    }
    56% {
        transform: translate(0, 2.5px) rotate(2.5deg);
    }
    58% {
        transform: translate(0, 0.5px) rotate(2.5deg);
    }
    60% {
        transform: translate(0, 2.5px) rotate(2.5deg);
    }
    62% {
        transform: translate(0, -0.5px) rotate(2.5deg);
    }
    64% {
        transform: translate(0, -0.5px) rotate(1.5deg);
    }
    66% {
        transform: translate(0, 1.5px) rotate(-0.5deg);
    }
    68% {
        transform: translate(0, -1.5px) rotate(-0.5deg);
    }
    70% {
        transform: translate(0, 1.5px) rotate(0.5deg);
    }
    72% {
        transform: translate(0, 2.5px) rotate(1.5deg);
    }
    74% {
        transform: translate(0, -0.5px) rotate(0.5deg);
    }
    76% {
        transform: translate(0, -0.5px) rotate(2.5deg);
    }
    78% {
        transform: translate(0, -0.5px) rotate(1.5deg);
    }
    80% {
        transform: translate(0, 1.5px) rotate(1.5deg);
    }
    82% {
        transform: translate(0, -0.5px) rotate(0.5deg);
    }
    84% {
        transform: translate(0, 1.5px) rotate(2.5deg);
    }
    86% {
        transform: translate(0, -1.5px) rotate(-1.5deg);
    }
    88% {
        transform: translate(0, -0.5px) rotate(2.5deg);
    }
    90% {
        transform: translate(0, 2.5px) rotate(-0.5deg);
    }
    92% {
        transform: translate(0, 0.5px) rotate(-0.5deg);
    }
    94% {
        transform: translate(0, 2.5px) rotate(0.5deg);
    }
    96% {
        transform: translate(0, -0.5px) rotate(1.5deg);
    }
    98% {
        transform: translate(0, -1.5px) rotate(-0.5deg);
    }
    0%,
    100% {
        transform: translate(0, 0) rotate(0deg);
    }
}`,`@import '../../node_modules/balloon-css/balloon.css';
@keyframes my-face {
  2% {
    transform: translate(0, 1.5px) rotate(1.5deg);
  }
  4% {
    transform: translate(0, -1.5px) rotate(-0.5deg);
  }
  6% {
    transform: translate(0, 1.5px) rotate(-1.5deg);
  }
  8% {
    transform: translate(0, -1.5px) rotate(-1.5deg);
  }
  10% {
    transform: translate(0, 2.5px) rotate(1.5deg);
  }
  12% {
    transform: translate(0, -0.5px) rotate(1.5deg);
  }
  14% {
    transform: translate(0, -1.5px) rotate(1.5deg);
  }
  16% {
    transform: translate(0, -0.5px) rotate(-1.5deg);
  }
  18% {
    transform: translate(0, 0.5px) rotate(-1.5deg);
  }
  20% {
    transform: translate(0, -1.5px) rotate(2.5deg);
  }
  22% {
    transform: translate(0, 0.5px) rotate(-1.5deg);
  }
  24% {
    transform: translate(0, 1.5px) rotate(1.5deg);
  }
  26% {
    transform: translate(0, 0.5px) rotate(0.5deg);
  }
  28% {
    transform: translate(0, 0.5px) rotate(1.5deg);
  }
  30% {
    transform: translate(0, -0.5px) rotate(2.5deg);
  }
  32% {
    transform: translate(0, 1.5px) rotate(-0.5deg);
  }
  34% {
    transform: translate(0, 1.5px) rotate(-0.5deg);
  }
  36% {
    transform: translate(0, -1.5px) rotate(2.5deg);
  }
  38% {
    transform: translate(0, 1.5px) rotate(-1.5deg);
  }
  40% {
    transform: translate(0, -0.5px) rotate(2.5deg);
  }
  42% {
    transform: translate(0, 2.5px) rotate(-1.5deg);
  }
  44% {
    transform: translate(0, 1.5px) rotate(0.5deg);
  }
  46% {
    transform: translate(0, -1.5px) rotate(2.5deg);
  }
  48% {
    transform: translate(0, -0.5px) rotate(0.5deg);
  }
  50% {
    transform: translate(0, 0.5px) rotate(0.5deg);
  }
  52% {
    transform: translate(0, 2.5px) rotate(2.5deg);
  }
  54% {
    transform: translate(0, -1.5px) rotate(1.5deg);
  }
  56% {
    transform: translate(0, 2.5px) rotate(2.5deg);
  }
  58% {
    transform: translate(0, 0.5px) rotate(2.5deg);
  }
  60% {
    transform: translate(0, 2.5px) rotate(2.5deg);
  }
  62% {
    transform: translate(0, -0.5px) rotate(2.5deg);
  }
  64% {
    transform: translate(0, -0.5px) rotate(1.5deg);
  }
  66% {
    transform: translate(0, 1.5px) rotate(-0.5deg);
  }
  68% {
    transform: translate(0, -1.5px) rotate(-0.5deg);
  }
  70% {
    transform: translate(0, 1.5px) rotate(0.5deg);
  }
  72% {
    transform: translate(0, 2.5px) rotate(1.5deg);
  }
  74% {
    transform: translate(0, -0.5px) rotate(0.5deg);
  }
  76% {
    transform: translate(0, -0.5px) rotate(2.5deg);
  }
  78% {
    transform: translate(0, -0.5px) rotate(1.5deg);
  }
  80% {
    transform: translate(0, 1.5px) rotate(1.5deg);
  }
  82% {
    transform: translate(0, -0.5px) rotate(0.5deg);
  }
  84% {
    transform: translate(0, 1.5px) rotate(2.5deg);
  }
  86% {
    transform: translate(0, -1.5px) rotate(-1.5deg);
  }
  88% {
    transform: translate(0, -0.5px) rotate(2.5deg);
  }
  90% {
    transform: translate(0, 2.5px) rotate(-0.5deg);
  }
  92% {
    transform: translate(0, 0.5px) rotate(-0.5deg);
  }
  94% {
    transform: translate(0, 2.5px) rotate(0.5deg);
  }
  96% {
    transform: translate(0, -0.5px) rotate(1.5deg);
  }
  98% {
    transform: translate(0, -1.5px) rotate(-0.5deg);
  }
  0%,
  100% {
    transform: translate(0, 0) rotate(0deg);
  }
}
.dplayer {
  position: relative;
  overflow: hidden;
  user-select: none;
  line-height: 1;
}
.dplayer * {
  box-sizing: content-box;
}
.dplayer svg {
  width: 100%;
  height: 100%;
}
.dplayer svg path,
.dplayer svg circle {
  fill: #fff;
}
.dplayer:-webkit-full-screen {
  width: 100%;
  height: 100%;
  background: #000;
  position: fixed;
  z-index: 100000;
  left: 0;
  top: 0;
  margin: 0;
  padding: 0;
  transform: translate(0, 0);
}
.dplayer.dplayer-no-danmaku .dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box .dplayer-setting-showdan,
.dplayer.dplayer-no-danmaku .dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box .dplayer-setting-danmaku,
.dplayer.dplayer-no-danmaku .dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box .dplayer-setting-danunlimit {
  display: none;
}
.dplayer.dplayer-no-danmaku .dplayer-controller .dplayer-icons .dplayer-comment {
  display: none;
}
.dplayer.dplayer-no-danmaku .dplayer-danmaku {
  display: none;
}
.dplayer.dplayer-live .dplayer-time {
  display: none;
}
.dplayer.dplayer-live .dplayer-bar-wrap {
  display: none;
}
.dplayer.dplayer-live .dplayer-setting-speed {
  display: none;
}
.dplayer.dplayer-live .dplayer-setting-loop {
  display: none;
}
.dplayer.dplayer-live.dplayer-no-danmaku .dplayer-setting {
  display: none;
}
.dplayer.dplayer-arrow .dplayer-danmaku {
  font-size: 18px;
}
.dplayer.dplayer-arrow .dplayer-icon {
  margin: 0 -3px;
}
.dplayer.dplayer-playing .dplayer-danmaku .dplayer-danmaku-move {
  animation-play-state: running;
}
@media (min-width: 900px) {
  .dplayer.dplayer-playing .dplayer-controller-mask {
    opacity: 0;
  }
  .dplayer.dplayer-playing .dplayer-controller {
    opacity: 0;
  }
  .dplayer.dplayer-playing:hover .dplayer-controller-mask {
    opacity: 1;
  }
  .dplayer.dplayer-playing:hover .dplayer-controller {
    opacity: 1;
  }
}
.dplayer.dplayer-loading .dplayer-bezel .diplayer-loading-icon {
  display: block;
}
.dplayer.dplayer-loading .dplayer-danmaku,
.dplayer.dplayer-paused .dplayer-danmaku,
.dplayer.dplayer-loading .dplayer-danmaku-move,
.dplayer.dplayer-paused .dplayer-danmaku-move {
  animation-play-state: paused;
}
.dplayer.dplayer-hide-controller {
  cursor: none;
}
.dplayer.dplayer-hide-controller .dplayer-controller-mask {
  opacity: 0;
  transform: translateY(100%);
}
.dplayer.dplayer-hide-controller .dplayer-controller {
  opacity: 0;
  transform: translateY(100%);
}
.dplayer.dplayer-show-controller .dplayer-controller-mask {
  opacity: 1;
}
.dplayer.dplayer-show-controller .dplayer-controller {
  opacity: 1;
}
.dplayer.dplayer-fulled {
  position: fixed;
  z-index: 100000;
  left: 0;
  top: 0;
  width: 100% !important;
  height: 100% !important;
}
.dplayer.dplayer-mobile .dplayer-controller .dplayer-icons .dplayer-volume,
.dplayer.dplayer-mobile .dplayer-controller .dplayer-icons .dplayer-camera-icon,
.dplayer.dplayer-mobile .dplayer-controller .dplayer-icons .dplayer-airplay-icon,
.dplayer.dplayer-mobile .dplayer-controller .dplayer-icons .dplayer-chromecast-icon,
.dplayer.dplayer-mobile .dplayer-controller .dplayer-icons .dplayer-play-icon {
  display: none;
}
.dplayer.dplayer-mobile .dplayer-controller .dplayer-icons .dplayer-full .dplayer-full-in-icon {
  position: static;
  display: inline-block;
}
.dplayer.dplayer-mobile .dplayer-bar-time {
  display: none;
}
.dplayer.dplayer-mobile.dplayer-hide-controller .dplayer-mobile-play {
  display: none;
}
.dplayer.dplayer-mobile .dplayer-mobile-play {
  display: block;
}
.dplayer-web-fullscreen-fix {
  position: fixed;
  top: 0;
  left: 0;
  margin: 0;
  padding: 0;
}
[data-balloon]:before {
  display: none;
}
[data-balloon]:after {
  padding: 0.3em 0.7em;
  background: rgba(17, 17, 17, 0.7);
}
[data-balloon][data-balloon-pos="up"]:after {
  margin-bottom: 0;
}
.dplayer-bezel {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  font-size: 22px;
  color: #fff;
  pointer-events: none;
}
.dplayer-bezel .dplayer-bezel-icon {
  position: absolute;
  top: 50%;
  left: 50%;
  margin: -26px 0 0 -26px;
  height: 52px;
  width: 52px;
  padding: 12px;
  box-sizing: border-box;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  opacity: 0;
  pointer-events: none;
}
.dplayer-bezel .dplayer-bezel-icon.dplayer-bezel-transition {
  animation: bezel-hide 0.5s linear;
}
@keyframes bezel-hide {
  from {
    opacity: 1;
    transform: scale(1);
  }
  to {
    opacity: 0;
    transform: scale(2);
  }
}
.dplayer-bezel .dplayer-danloading {
  position: absolute;
  top: 50%;
  margin-top: -7px;
  width: 100%;
  text-align: center;
  font-size: 14px;
  line-height: 14px;
  animation: my-face 5s infinite ease-in-out;
}
.dplayer-bezel .diplayer-loading-icon {
  display: none;
  position: absolute;
  top: 50%;
  left: 50%;
  margin: -18px 0 0 -18px;
  height: 36px;
  width: 36px;
  pointer-events: none;
}
.dplayer-bezel .diplayer-loading-icon .diplayer-loading-hide {
  display: none;
}
.dplayer-bezel .diplayer-loading-icon .diplayer-loading-dot {
  animation: diplayer-loading-dot-fade 0.8s ease infinite;
  opacity: 0;
  transform-origin: 4px 4px;
}
.dplayer-bezel .diplayer-loading-icon .diplayer-loading-dot.diplayer-loading-dot-1 {
  animation-delay: 0.1s;
}
.dplayer-bezel .diplayer-loading-icon .diplayer-loading-dot.diplayer-loading-dot-2 {
  animation-delay: 0.2s;
}
.dplayer-bezel .diplayer-loading-icon .diplayer-loading-dot.diplayer-loading-dot-3 {
  animation-delay: 0.3s;
}
.dplayer-bezel .diplayer-loading-icon .diplayer-loading-dot.diplayer-loading-dot-4 {
  animation-delay: 0.4s;
}
.dplayer-bezel .diplayer-loading-icon .diplayer-loading-dot.diplayer-loading-dot-5 {
  animation-delay: 0.5s;
}
.dplayer-bezel .diplayer-loading-icon .diplayer-loading-dot.diplayer-loading-dot-6 {
  animation-delay: 0.6s;
}
.dplayer-bezel .diplayer-loading-icon .diplayer-loading-dot.diplayer-loading-dot-7 {
  animation-delay: 0.7s;
}
@keyframes diplayer-loading-dot-fade {
  0% {
    opacity: 0.7;
    transform: scale(1.2, 1.2);
  }
  50% {
    opacity: 0.25;
    transform: scale(0.9, 0.9);
  }
  to {
    opacity: 0.25;
    transform: scale(0.85, 0.85);
  }
}
.dplayer-controller-mask {
  background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAADGCAYAAAAT+OqFAAAAdklEQVQoz42QQQ7AIAgEF/T/D+kbq/RWAlnQyyazA4aoAB4FsBSA/bFjuF1EOL7VbrIrBuusmrt4ZZORfb6ehbWdnRHEIiITaEUKa5EJqUakRSaEYBJSCY2dEstQY7AuxahwXFrvZmWl2rh4JZ07z9dLtesfNj5q0FU3A5ObbwAAAABJRU5ErkJggg==) repeat-x bottom;
  height: 98px;
  width: 100%;
  position: absolute;
  bottom: 0;
  transition: all 0.3s ease;
}
.dplayer-controller {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 41px;
  padding: 0 20px;
  user-select: none;
  transition: all 0.3s ease;
}
.dplayer-controller.dplayer-controller-comment .dplayer-icons {
  display: none;
}
.dplayer-controller.dplayer-controller-comment .dplayer-icons.dplayer-comment-box {
  display: block;
}
.dplayer-controller .dplayer-bar-wrap {
  padding: 5px 0;
  cursor: pointer;
  position: absolute;
  bottom: 33px;
  width: calc(100% - 40px);
  height: 3px;
}
.dplayer-controller .dplayer-bar-wrap:hover .dplayer-bar .dplayer-played .dplayer-thumb {
  transform: scale(1);
}
.dplayer-controller .dplayer-bar-wrap:hover .dplayer-highlight {
  display: block;
  width: 8px;
  transform: translateX(-4px);
  top: 4px;
  height: 40%;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-highlight {
  z-index: 12;
  position: absolute;
  top: 5px;
  width: 6px;
  height: 20%;
  border-radius: 6px;
  background-color: #fff;
  text-align: center;
  transform: translateX(-3px);
  transition: all 0.2s ease-in-out;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-highlight:hover .dplayer-highlight-text {
  display: block;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-highlight:hover ~ .dplayer-bar-preview {
  opacity: 0;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-highlight:hover ~ .dplayer-bar-time {
  opacity: 0;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-highlight .dplayer-highlight-text {
  display: none;
  position: absolute;
  left: 50%;
  top: -24px;
  padding: 5px 8px;
  background-color: rgba(0, 0, 0, 0.62);
  color: #fff;
  border-radius: 4px;
  font-size: 12px;
  white-space: nowrap;
  transform: translateX(-50%);
}
.dplayer-controller .dplayer-bar-wrap .dplayer-bar-preview {
  position: absolute;
  background: #fff;
  pointer-events: none;
  display: none;
  background-size: 16000px 100%;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-bar-preview-canvas {
  position: absolute;
  width: 100%;
  height: 100%;
  z-index: 1;
  pointer-events: none;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-bar-time {
  position: absolute;
  left: 0px;
  top: -20px;
  border-radius: 4px;
  padding: 5px 7px;
  background-color: rgba(0, 0, 0, 0.62);
  color: #fff;
  font-size: 12px;
  text-align: center;
  opacity: 1;
  transition: opacity 0.1s ease-in-out;
  word-wrap: normal;
  word-break: normal;
  z-index: 2;
  pointer-events: none;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-bar-time.hidden {
  opacity: 0;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-bar {
  position: relative;
  height: 3px;
  width: 100%;
  background: rgba(255, 255, 255, 0.2);
  cursor: pointer;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-bar .dplayer-loaded {
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.4);
  height: 3px;
  transition: all 0.5s ease;
  will-change: width;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-bar .dplayer-played {
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  height: 3px;
  will-change: width;
}
.dplayer-controller .dplayer-bar-wrap .dplayer-bar .dplayer-played .dplayer-thumb {
  position: absolute;
  top: 0;
  right: 5px;
  margin-top: -4px;
  margin-right: -10px;
  height: 11px;
  width: 11px;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s ease-in-out;
  transform: scale(0);
}
.dplayer-controller .dplayer-icons {
  height: 38px;
  position: absolute;
  bottom: 0;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box {
  display: none;
  position: absolute;
  transition: all 0.3s ease-in-out;
  z-index: 2;
  height: 38px;
  bottom: 0;
  left: 20px;
  right: 20px;
  color: #fff;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-icon {
  padding: 7px;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-icon {
  position: absolute;
  left: 0;
  top: 0;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-send-icon {
  position: absolute;
  right: 0;
  top: 0;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box {
  position: absolute;
  background: rgba(28, 28, 28, 0.9);
  bottom: 41px;
  left: 0;
  box-shadow: 0 0 25px rgba(0, 0, 0, 0.3);
  border-radius: 4px;
  padding: 10px 10px 16px;
  font-size: 14px;
  width: 204px;
  transition: all 0.3s ease-in-out;
  transform: scale(0);
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box.dplayer-comment-setting-open {
  transform: scale(1);
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box input[type=radio] {
  display: none;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box label {
  cursor: pointer;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-title {
  font-size: 13px;
  color: #fff;
  line-height: 30px;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-type {
  font-size: 0;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-type .dplayer-comment-setting-title {
  margin-bottom: 6px;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-type label:nth-child(2) span {
  border-radius: 4px 0 0 4px;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-type label:nth-child(4) span {
  border-radius: 0 4px 4px 0;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-type span {
  width: 33%;
  padding: 4px 6px;
  line-height: 16px;
  display: inline-block;
  font-size: 12px;
  color: #fff;
  border: 1px solid #fff;
  margin-right: -1px;
  box-sizing: border-box;
  text-align: center;
  cursor: pointer;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-type input:checked + span {
  background: #E4E4E6;
  color: #1c1c1c;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-color {
  font-size: 0;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-color label {
  font-size: 0;
  padding: 6px;
  display: inline-block;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-color span {
  width: 22px;
  height: 22px;
  display: inline-block;
  border-radius: 50%;
  box-sizing: border-box;
  cursor: pointer;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-setting-box .dplayer-comment-setting-color span:hover {
  animation: my-face 5s infinite ease-in-out;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-input {
  outline: none;
  border: none;
  padding: 8px 31px;
  font-size: 14px;
  line-height: 18px;
  text-align: center;
  border-radius: 4px;
  background: none;
  margin: 0;
  height: 100%;
  box-sizing: border-box;
  width: 100%;
  color: #fff;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-input::placeholder {
  color: #fff;
  opacity: 0.8;
}
.dplayer-controller .dplayer-icons.dplayer-comment-box .dplayer-comment-input::-ms-clear {
  display: none;
}
.dplayer-controller .dplayer-icons.dplayer-icons-left .dplayer-icon {
  padding: 7px;
}
.dplayer-controller .dplayer-icons.dplayer-icons-right {
  right: 20px;
}
.dplayer-controller .dplayer-icons.dplayer-icons-right .dplayer-icon {
  padding: 8px;
}
.dplayer-controller .dplayer-icons .dplayer-time,
.dplayer-controller .dplayer-icons .dplayer-live-badge {
  line-height: 38px;
  color: #eee;
  text-shadow: 0 0 2px rgba(0, 0, 0, 0.5);
  vertical-align: middle;
  font-size: 13px;
  cursor: default;
}
.dplayer-controller .dplayer-icons .dplayer-live-dot {
  display: inline-block;
  width: 6px;
  height: 6px;
  vertical-align: 4%;
  margin-right: 5px;
  content: '';
  border-radius: 6px;
}
.dplayer-controller .dplayer-icons .dplayer-icon {
  width: 40px;
  height: 100%;
  border: none;
  background-color: transparent;
  outline: none;
  cursor: pointer;
  vertical-align: middle;
  box-sizing: border-box;
  display: inline-block;
}
.dplayer-controller .dplayer-icons .dplayer-icon .dplayer-icon-content {
  transition: all 0.2s ease-in-out;
  opacity: 0.8;
}
.dplayer-controller .dplayer-icons .dplayer-icon:hover .dplayer-icon-content {
  opacity: 1;
}
.dplayer-controller .dplayer-icons .dplayer-icon.dplayer-quality-icon {
  color: #fff;
  width: auto;
  line-height: 22px;
  font-size: 14px;
}
.dplayer-controller .dplayer-icons .dplayer-icon.dplayer-comment-icon {
  padding: 10px 9px 9px;
}
.dplayer-controller .dplayer-icons .dplayer-icon.dplayer-setting-icon {
  padding-top: 8.5px;
}
.dplayer-controller .dplayer-icons .dplayer-icon.dplayer-volume-icon {
  width: 43px;
}
.dplayer-controller .dplayer-icons .dplayer-volume {
  position: relative;
  display: inline-block;
  cursor: pointer;
  height: 100%;
}
.dplayer-controller .dplayer-icons .dplayer-volume:hover .dplayer-volume-bar-wrap .dplayer-volume-bar {
  width: 45px;
}
.dplayer-controller .dplayer-icons .dplayer-volume:hover .dplayer-volume-bar-wrap .dplayer-volume-bar .dplayer-volume-bar-inner .dplayer-thumb {
  transform: scale(1);
}
.dplayer-controller .dplayer-icons .dplayer-volume.dplayer-volume-active .dplayer-volume-bar-wrap .dplayer-volume-bar {
  width: 45px;
}
.dplayer-controller .dplayer-icons .dplayer-volume.dplayer-volume-active .dplayer-volume-bar-wrap .dplayer-volume-bar .dplayer-volume-bar-inner .dplayer-thumb {
  transform: scale(1);
}
.dplayer-controller .dplayer-icons .dplayer-volume .dplayer-volume-bar-wrap {
  display: inline-block;
  margin: 0 10px 0 -5px;
  vertical-align: middle;
  height: 100%;
}
.dplayer-controller .dplayer-icons .dplayer-volume .dplayer-volume-bar-wrap .dplayer-volume-bar {
  position: relative;
  top: 17px;
  width: 0;
  height: 3px;
  background: #aaa;
  transition: all 0.3s ease-in-out;
}
.dplayer-controller .dplayer-icons .dplayer-volume .dplayer-volume-bar-wrap .dplayer-volume-bar .dplayer-volume-bar-inner {
  position: absolute;
  bottom: 0;
  left: 0;
  height: 100%;
  transition: all 0.1s ease;
  will-change: width;
}
.dplayer-controller .dplayer-icons .dplayer-volume .dplayer-volume-bar-wrap .dplayer-volume-bar .dplayer-volume-bar-inner .dplayer-thumb {
  position: absolute;
  top: 0;
  right: 5px;
  margin-top: -4px;
  margin-right: -10px;
  height: 11px;
  width: 11px;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s ease-in-out;
  transform: scale(0);
}
.dplayer-controller .dplayer-icons .dplayer-subtitle-btn {
  display: inline-block;
  height: 100%;
}
.dplayer-controller .dplayer-icons .dplayer-subtitles {
  display: inline-block;
  height: 100%;
}
.dplayer-controller .dplayer-icons .dplayer-subtitles .dplayer-subtitles-box {
  position: absolute;
  right: 0;
  bottom: 50px;
  transform: scale(0);
  width: fit-content;
  max-width: 240px;
  min-width: 120px;
  border-radius: 2px;
  background: rgba(28, 28, 28, 0.9);
  padding: 7px 0;
  transition: all 0.3s ease-in-out;
  overflow: auto;
  z-index: 2;
}
.dplayer-controller .dplayer-icons .dplayer-subtitles .dplayer-subtitles-box.dplayer-subtitles-panel {
  display: block;
}
.dplayer-controller .dplayer-icons .dplayer-subtitles .dplayer-subtitles-box.dplayer-subtitles-box-open {
  transform: scale(1);
}
.dplayer-controller .dplayer-icons .dplayer-subtitles .dplayer-subtitles-item {
  height: 30px;
  padding: 5px 10px;
  box-sizing: border-box;
  cursor: pointer;
  position: relative;
}
.dplayer-controller .dplayer-icons .dplayer-subtitles .dplayer-subtitles-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
}
.dplayer-controller .dplayer-icons .dplayer-setting {
  display: inline-block;
  height: 100%;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box {
  position: absolute;
  right: 0;
  bottom: 50px;
  transform: scale(0);
  width: 150px;
  border-radius: 2px;
  background: rgba(28, 28, 28, 0.9);
  padding: 7px 0;
  transition: all 0.3s ease-in-out;
  overflow: hidden;
  z-index: 2;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box > div {
  display: none;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box > div.dplayer-setting-origin-panel {
  display: block;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box.dplayer-setting-box-open {
  transform: scale(1);
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box.dplayer-setting-box-narrow {
  width: 70px;
  text-align: center;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box.dplayer-setting-box-speed .dplayer-setting-origin-panel {
  display: none;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box.dplayer-setting-box-speed .dplayer-setting-speed-panel {
  display: block;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-item,
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-speed-item {
  height: 30px;
  padding: 5px 10px;
  box-sizing: border-box;
  cursor: pointer;
  position: relative;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-item:hover,
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-speed-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku {
  padding: 5px 0;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku .dplayer-label {
  padding: 0 10px;
  display: inline;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku:hover .dplayer-label {
  display: none;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku:hover .dplayer-danmaku-bar-wrap {
  display: inline-block;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku.dplayer-setting-danmaku-active .dplayer-label {
  display: none;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku.dplayer-setting-danmaku-active .dplayer-danmaku-bar-wrap {
  display: inline-block;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku .dplayer-danmaku-bar-wrap {
  padding: 0 10px;
  box-sizing: border-box;
  display: none;
  vertical-align: middle;
  height: 100%;
  width: 100%;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku .dplayer-danmaku-bar-wrap .dplayer-danmaku-bar {
  position: relative;
  top: 8.5px;
  width: 100%;
  height: 3px;
  background: #fff;
  transition: all 0.3s ease-in-out;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku .dplayer-danmaku-bar-wrap .dplayer-danmaku-bar .dplayer-danmaku-bar-inner {
  position: absolute;
  bottom: 0;
  left: 0;
  height: 100%;
  transition: all 0.1s ease;
  background: #aaa;
  will-change: width;
}
.dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-danmaku .dplayer-danmaku-bar-wrap .dplayer-danmaku-bar .dplayer-danmaku-bar-inner .dplayer-thumb {
  position: absolute;
  top: 0;
  right: 5px;
  margin-top: -4px;
  margin-right: -10px;
  height: 11px;
  width: 11px;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s ease-in-out;
  background: #aaa;
}
.dplayer-controller .dplayer-icons .dplayer-full {
  display: inline-block;
  height: 100%;
  position: relative;
}
.dplayer-controller .dplayer-icons .dplayer-full:hover .dplayer-full-in-icon {
  display: block;
}
.dplayer-controller .dplayer-icons .dplayer-full .dplayer-full-in-icon {
  position: absolute;
  top: -30px;
  z-index: 1;
  display: none;
}
.dplayer-controller .dplayer-icons .dplayer-quality {
  position: relative;
  display: inline-block;
  height: 100%;
  z-index: 2;
}
.dplayer-controller .dplayer-icons .dplayer-quality:hover .dplayer-quality-list {
  display: block;
}
.dplayer-controller .dplayer-icons .dplayer-quality:hover .dplayer-quality-mask {
  display: block;
}
.dplayer-controller .dplayer-icons .dplayer-quality .dplayer-quality-mask {
  display: none;
  position: absolute;
  bottom: 38px;
  left: -18px;
  width: 80px;
  padding-bottom: 12px;
}
.dplayer-controller .dplayer-icons .dplayer-quality .dplayer-quality-list {
  display: none;
  font-size: 12px;
  width: 80px;
  border-radius: 2px;
  background: rgba(28, 28, 28, 0.9);
  padding: 5px 0;
  transition: all 0.3s ease-in-out;
  overflow: hidden;
  color: #fff;
  text-align: center;
}
.dplayer-controller .dplayer-icons .dplayer-quality .dplayer-quality-item {
  height: 25px;
  box-sizing: border-box;
  cursor: pointer;
  line-height: 25px;
}
.dplayer-controller .dplayer-icons .dplayer-quality .dplayer-quality-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
}
.dplayer-controller .dplayer-icons .dplayer-comment {
  display: inline-block;
  height: 100%;
}
.dplayer-controller .dplayer-icons .dplayer-label {
  color: #eee;
  font-size: 13px;
  display: inline-block;
  vertical-align: middle;
  white-space: nowrap;
}
.dplayer-controller .dplayer-icons .dplayer-toggle {
  width: 32px;
  height: 20px;
  text-align: center;
  font-size: 0;
  vertical-align: middle;
  position: absolute;
  top: 5px;
  right: 10px;
}
.dplayer-controller .dplayer-icons .dplayer-toggle input {
  max-height: 0;
  max-width: 0;
  display: none;
}
.dplayer-controller .dplayer-icons .dplayer-toggle input + label {
  display: inline-block;
  position: relative;
  box-shadow: #dfdfdf 0 0 0 0 inset;
  border: 1px solid #dfdfdf;
  height: 20px;
  width: 32px;
  border-radius: 10px;
  box-sizing: border-box;
  cursor: pointer;
  transition: 0.2s ease-in-out;
}
.dplayer-controller .dplayer-icons .dplayer-toggle input + label:before {
  content: "";
  position: absolute;
  display: block;
  height: 18px;
  width: 18px;
  top: 0;
  left: 0;
  border-radius: 15px;
  transition: 0.2s ease-in-out;
}
.dplayer-controller .dplayer-icons .dplayer-toggle input + label:after {
  content: "";
  position: absolute;
  display: block;
  left: 0;
  top: 0;
  border-radius: 15px;
  background: #fff;
  transition: 0.2s ease-in-out;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.4);
  height: 18px;
  width: 18px;
}
.dplayer-controller .dplayer-icons .dplayer-toggle input:checked + label {
  border-color: rgba(255, 255, 255, 0.5);
}
.dplayer-controller .dplayer-icons .dplayer-toggle input:checked + label:before {
  width: 30px;
  background: rgba(255, 255, 255, 0.5);
}
.dplayer-controller .dplayer-icons .dplayer-toggle input:checked + label:after {
  left: 12px;
}
.dplayer-mobile-play {
  display: none;
  width: 50px;
  height: 50px;
  border: none;
  background-color: transparent;
  outline: none;
  cursor: pointer;
  box-sizing: border-box;
  bottom: 0;
  opacity: 0.8;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}
.dplayer-danmaku {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  font-size: 22px;
  color: #fff;
}
.dplayer-danmaku .dplayer-danmaku-item {
  display: inline-block;
  pointer-events: none;
  user-select: none;
  cursor: default;
  white-space: nowrap;
  text-shadow: 0.5px 0.5px 0.5px rgba(0, 0, 0, 0.5);
}
.dplayer-danmaku .dplayer-danmaku-item--demo {
  position: absolute;
  visibility: hidden;
}
.dplayer-danmaku .dplayer-danmaku-right {
  position: absolute;
  right: 0;
  transform: translateX(100%);
}
.dplayer-danmaku .dplayer-danmaku-right.dplayer-danmaku-move {
  will-change: transform;
  animation-name: 'danmaku';
  animation-timing-function: linear;
  animation-play-state: paused;
}
@keyframes danmaku {
  from {
    transform: translateX(100%);
  }
}
.dplayer-danmaku .dplayer-danmaku-top,
.dplayer-danmaku .dplayer-danmaku-bottom {
  position: absolute;
  width: 100%;
  text-align: center;
  visibility: hidden;
}
.dplayer-danmaku .dplayer-danmaku-top.dplayer-danmaku-move,
.dplayer-danmaku .dplayer-danmaku-bottom.dplayer-danmaku-move {
  will-change: visibility;
  animation-name: 'danmaku-center';
  animation-timing-function: linear;
  animation-play-state: paused;
}
@keyframes danmaku-center {
  from {
    visibility: visible;
  }
  to {
    visibility: visible;
  }
}
.dplayer-logo {
  pointer-events: none;
  position: absolute;
  left: 20px;
  top: 20px;
  max-width: 50px;
  max-height: 50px;
}
.dplayer-logo img {
  max-width: 100%;
  max-height: 100%;
  background: none;
}
.dplayer-menu {
  position: absolute;
  width: 170px;
  border-radius: 2px;
  background: rgba(28, 28, 28, 0.85);
  padding: 5px 0;
  overflow: hidden;
  z-index: 3;
  display: none;
}
.dplayer-menu.dplayer-menu-show {
  display: block;
}
.dplayer-menu .dplayer-menu-item {
  height: 30px;
  box-sizing: border-box;
  cursor: pointer;
}
.dplayer-menu .dplayer-menu-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
}
.dplayer-menu .dplayer-menu-item a {
  padding: 0 10px;
  line-height: 30px;
  color: #eee;
  font-size: 13px;
  display: inline-block;
  vertical-align: middle;
  width: 100%;
  box-sizing: border-box;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}
.dplayer-menu .dplayer-menu-item a:hover {
  text-decoration: none;
}
.dplayer-notice-list {
  position: absolute;
  bottom: 60px;
  left: 20px;
}
.dplayer-notice-list .dplayer-notice {
  border-radius: 2px;
  background: rgba(28, 28, 28, 0.9);
  transition: all 0.3s ease-in-out;
  overflow: hidden;
  color: #fff;
  display: table;
  pointer-events: none;
  animation: showNotice 0.3s ease 1 forwards;
}
.dplayer-notice-list .remove-notice {
  animation: removeNotice 0.3s ease 1 forwards;
}
@keyframes showNotice {
  from {
    padding: 0;
    font-size: 0;
    margin-top: 0;
  }
  to {
    padding: 7px 20px;
    font-size: 14px;
    margin-top: 5px;
  }
}
@keyframes removeNotice {
  0% {
    padding: 7px 20px;
    font-size: 14px;
    margin-top: 5px;
  }
  20% {
    font-size: 12px;
  }
  21% {
    font-size: 0;
    padding: 7px 10px;
  }
  100% {
    padding: 0;
    margin-top: 0;
    font-size: 0;
  }
}
.dplayer-subtitle {
  position: absolute;
  bottom: 40px;
  width: 90%;
  left: 5%;
  text-align: center;
  color: #fff;
  text-shadow: 0.5px 0.5px 0.5px rgba(0, 0, 0, 0.5);
  font-size: 20px;
}
.dplayer-subtitle.dplayer-subtitle-hide {
  display: none;
}
.dplayer-mask {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 1;
  display: none;
}
.dplayer-mask.dplayer-mask-show {
  display: block;
}
.dplayer-video-wrap {
  position: relative;
  background: #000;
  font-size: 0;
  width: 100%;
  height: 100%;
}
.dplayer-video-wrap .dplayer-video {
  width: 100%;
  height: 100%;
  display: none;
}
.dplayer-video-wrap .dplayer-video-current {
  display: block;
}
.dplayer-video-wrap .dplayer-video-prepare {
  display: none;
}
.dplayer-info-panel {
  position: absolute;
  top: 10px;
  left: 10px;
  width: 400px;
  background: rgba(28, 28, 28, 0.8);
  padding: 10px;
  color: #fff;
  font-size: 12px;
  border-radius: 2px;
}
.dplayer-info-panel-hide {
  display: none;
}
.dplayer-info-panel .dplayer-info-panel-close {
  cursor: pointer;
  position: absolute;
  right: 10px;
  top: 10px;
}
.dplayer-info-panel .dplayer-info-panel-item > span {
  display: inline-block;
  vertical-align: middle;
  line-height: 15px;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}
.dplayer-info-panel .dplayer-info-panel-item-title {
  width: 100px;
  text-align: right;
  margin-right: 10px;
}
.dplayer-info-panel .dplayer-info-panel-item-data {
  width: 260px;
}
`,`.dplayer {
    position: relative;
    overflow: hidden;
    user-select: none;
    line-height: 1;

    * {
        box-sizing: content-box;
    }

    svg {
        width: 100%;
        height: 100%;

        path,
        circle {
            fill: #fff;
        }
    }

    &:-webkit-full-screen {
        width: 100%;
        height: 100%;
        background: #000;
        position: fixed;
        z-index: 100000;
        left: 0;
        top: 0;
        margin: 0;
        padding: 0;
        transform: translate(0, 0);
        
    }

    &.dplayer-no-danmaku {
        .dplayer-controller .dplayer-icons .dplayer-setting .dplayer-setting-box {
            .dplayer-setting-showdan,
            .dplayer-setting-danmaku,
            .dplayer-setting-danunlimit {
                display: none;
            }
        }

        .dplayer-controller .dplayer-icons .dplayer-comment {
            display: none;
        }

        .dplayer-danmaku {
            display: none;
        }
    }

    &.dplayer-live {
        .dplayer-time {
            display: none;
        }
        .dplayer-bar-wrap {
            display: none;
        }
        .dplayer-setting-speed {
            display: none;
        }
        .dplayer-setting-loop {
            display: none;
        }

        &.dplayer-no-danmaku {
            .dplayer-setting {
                display: none;
            }
        }
    }

    &.dplayer-arrow {
        .dplayer-danmaku {
            font-size: 18px;
        }
        .dplayer-icon {
            margin: 0 -3px;
        }
    }

    &.dplayer-playing {
        .dplayer-danmaku .dplayer-danmaku-move {
            animation-play-state: running;
        }

        @media (min-width: 900px) {
            .dplayer-controller-mask {
                opacity: 0;
            }
            .dplayer-controller {
                opacity: 0;
            }

            &:hover {
                .dplayer-controller-mask {
                    opacity: 1;
                }
                .dplayer-controller {
                    opacity: 1;
                }
            }
        }
    }

    &.dplayer-loading {
        .dplayer-bezel .diplayer-loading-icon {
            display: block;
        }
    }

    &.dplayer-loading,
    &.dplayer-paused {
        .dplayer-danmaku,
        .dplayer-danmaku-move {
            animation-play-state: paused;
        }
    }

    &.dplayer-hide-controller {
        cursor: none;

        .dplayer-controller-mask {
            opacity: 0;
            transform: translateY(100%);
        }
        .dplayer-controller {
            opacity: 0;
            transform: translateY(100%);
        }
    }
    &.dplayer-show-controller {
        .dplayer-controller-mask {
            opacity: 1;
        }
        .dplayer-controller {
            opacity: 1;
        }
    }
    &.dplayer-fulled {
        position: fixed;
        z-index: 100000;
        left: 0;
        top: 0;
        width: 100% !important;
        height: 100% !important;
    }
    &.dplayer-mobile {
        .dplayer-controller .dplayer-icons {
            .dplayer-volume,
            .dplayer-camera-icon,
            .dplayer-airplay-icon,
            .dplayer-chromecast-icon,
            .dplayer-play-icon {
                display: none;
            }
            .dplayer-full .dplayer-full-in-icon {
                position: static;
                display: inline-block;
            }
        }

        .dplayer-bar-time {
            display: none;
        }

        &.dplayer-hide-controller {
            .dplayer-mobile-play {
                display: none;
            }
        }

        .dplayer-mobile-play {
            display: block;
        }
    }
}

// To hide scroll bar, apply this class to <body>
.dplayer-web-fullscreen-fix {
    position: fixed;
    top: 0;
    left: 0;
    margin: 0;
    padding: 0;
}
`,`@import '../../node_modules/balloon-css/balloon.css';

[data-balloon]:before {
    display: none;
}

[data-balloon]:after {
    padding: 0.3em 0.7em;
    background: rgba(17, 17, 17, 0.7);
}

[data-balloon][data-balloon-pos="up"]:after {
    margin-bottom: 0;
}`,`.dplayer-bezel {
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    font-size: 22px;
    color: #fff;
    pointer-events: none;
    .dplayer-bezel-icon {
        position: absolute;
        top: 50%;
        left: 50%;
        margin: -26px 0 0 -26px;
        height: 52px;
        width: 52px;
        padding: 12px;
        box-sizing: border-box;
        background: rgba(0, 0, 0, .5);
        border-radius: 50%;
        opacity: 0;
        pointer-events: none;
        &.dplayer-bezel-transition {
            animation: bezel-hide .5s linear;
        }
        @keyframes bezel-hide {
            from {
                opacity: 1;
                transform: scale(1);
            }
            to {
                opacity: 0;
                transform: scale(2);
            }
        }
    }
    .dplayer-danloading {
        position: absolute;
        top: 50%;
        margin-top: -7px;
        width: 100%;
        text-align: center;
        font-size: 14px;
        line-height: 14px;
        animation: my-face 5s infinite ease-in-out;
    }
    .diplayer-loading-icon {
        display: none;
        position: absolute;
        top: 50%;
        left: 50%;
        margin: -18px 0 0 -18px;
        height: 36px;
        width: 36px;
        pointer-events: none;
        .diplayer-loading-hide {
            display: none;
        }
        .diplayer-loading-dot {
            animation: diplayer-loading-dot-fade .8s ease infinite;
            opacity: 0;
            transform-origin: 4px 4px;
            each(range(7), {
                &.diplayer-loading-dot-@{value} {
                    animation-delay: (@value * 0.1s);
                }
            });
        }
        @keyframes diplayer-loading-dot-fade {
            0% {
                opacity: .7;
                transform: scale(1.2, 1.2)
            }
            50% {
                opacity: .25;
                transform: scale(.9, .9)
            }
            to {
                opacity: .25;
                transform: scale(.85, .85)
            }
        }
    }
}`,`.dplayer-notice-list{
    position: absolute;
    bottom: 60px;
    left: 20px;

    .dplayer-notice {
        border-radius: 2px;
        background: rgba(28, 28, 28, 0.9);
        transition: all .3s ease-in-out;
        overflow: hidden;
        color: #fff;
        display: table;
        pointer-events: none;
        animation: showNotice .3s ease 1 forwards;
    }

    .remove-notice{
        animation: removeNotice .3s ease 1 forwards;
    }
}

@keyframes showNotice {
    from {
        padding: 0;
        font-size: 0;
        margin-top: 0;
    }
    to {
        padding: 7px 20px;
        font-size: 14px;
        margin-top: 5px;
    }
}

@keyframes removeNotice {
    0%{
        padding: 7px 20px;
        font-size: 14px;
        margin-top: 5px;
    }
    20%{
        font-size: 12px;
    }
    21%{
        font-size: 0;
        padding: 7px 10px;
    }
    100%{
        padding: 0;
        margin-top: 0;
        font-size: 0;
    }
}
`,`.dplayer-controller-mask {
    background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAADGCAYAAAAT+OqFAAAAdklEQVQoz42QQQ7AIAgEF/T/D+kbq/RWAlnQyyazA4aoAB4FsBSA/bFjuF1EOL7VbrIrBuusmrt4ZZORfb6ehbWdnRHEIiITaEUKa5EJqUakRSaEYBJSCY2dEstQY7AuxahwXFrvZmWl2rh4JZ07z9dLtesfNj5q0FU3A5ObbwAAAABJRU5ErkJggg==) repeat-x bottom;
    height: 98px;
    width: 100%;
    position: absolute;
    bottom: 0;
    transition: all 0.3s ease;
}

.dplayer-controller {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 41px;
    padding: 0 20px;
    user-select: none;
    transition: all 0.3s ease;
    &.dplayer-controller-comment {
        .dplayer-icons {
            display: none;
        }
        .dplayer-icons.dplayer-comment-box {
            display: block;
        }
    }
    .dplayer-bar-wrap {
        padding: 5px 0;
        cursor: pointer;
        position: absolute;
        bottom: 33px;
        width: calc(100% - 40px);
        height: 3px;
        &:hover {
            .dplayer-bar .dplayer-played .dplayer-thumb {
                transform: scale(1);
            }
            .dplayer-highlight {
                display: block;
                width: 8px;
                transform: translateX(-4px);
                top: 4px;
                height: 40%;
            }
        }
        .dplayer-highlight {
            z-index: 12;
            position: absolute;
            top: 5px;
            width: 6px;
            height: 20%;
            border-radius: 6px;
            background-color: #fff;
            text-align: center;
            transform: translateX(-3px);
            transition: all .2s ease-in-out;
            &:hover {
                .dplayer-highlight-text {
                    display: block;
                }
                &~.dplayer-bar-preview {
                    opacity: 0;
                }
                &~.dplayer-bar-time {
                    opacity: 0;
                }
            }
            .dplayer-highlight-text {
                display: none;
                position: absolute;
                left: 50%;
                top: -24px;
                padding: 5px 8px;
                background-color: rgba(0, 0, 0, .62);
                color: #fff;
                border-radius: 4px;
                font-size: 12px;
                white-space: nowrap;
                transform: translateX(-50%);
            }
        }
        .dplayer-bar-preview {
            position: absolute;
            background: #fff;
            pointer-events: none;
            display: none;
            background-size: 16000px 100%;
        }
        .dplayer-bar-preview-canvas {
            position: absolute;
            width: 100%;
            height: 100%;
            z-index: 1;
            pointer-events: none;
        }
        .dplayer-bar-time {
            &.hidden {
                opacity: 0;
            }
            position: absolute;
            left: 0px;
            top: -20px;
            border-radius: 4px;
            padding: 5px 7px;
            background-color: rgba(0, 0, 0, 0.62);
            color: #fff;
            font-size: 12px;
            text-align: center;
            opacity: 1;
            transition: opacity .1s ease-in-out;
            word-wrap: normal;
            word-break: normal;
            z-index: 2;
            pointer-events: none;
        }
        .dplayer-bar {
            position: relative;
            height: 3px;
            width: 100%;
            background: rgba(255, 255, 255, .2);
            cursor: pointer;
            .dplayer-loaded {
                position: absolute;
                left: 0;
                top: 0;
                bottom: 0;
                background: rgba(255, 255, 255, .4);
                height: 3px;
                transition: all 0.5s ease;
                will-change: width;
            }
            .dplayer-played {
                position: absolute;
                left: 0;
                top: 0;
                bottom: 0;
                height: 3px;
                will-change: width;
                .dplayer-thumb {
                    position: absolute;
                    top: 0;
                    right: 5px;
                    margin-top: -4px;
                    margin-right: -10px;
                    height: 11px;
                    width: 11px;
                    border-radius: 50%;
                    cursor: pointer;
                    transition: all .3s ease-in-out;
                    transform: scale(0);
                }
            }
        }
    }
    .dplayer-icons {
        height: 38px;
        position: absolute;
        bottom: 0;
        &.dplayer-comment-box {
            display: none;
            position: absolute;
            transition: all .3s ease-in-out;
            z-index: 2;
            height: 38px;
            bottom: 0;
            left: 20px;
            right: 20px;
            color: #fff;
            .dplayer-icon {
                padding: 7px;
            }
            .dplayer-comment-setting-icon {
                position: absolute;
                left: 0;
                top: 0;
            }
            .dplayer-send-icon {
                position: absolute;
                right: 0;
                top: 0;
            }
            .dplayer-comment-setting-box {
                position: absolute;
                background: rgba(28, 28, 28, 0.9);
                bottom: 41px;
                left: 0;
                box-shadow: 0 0 25px rgba(0, 0, 0, .3);
                border-radius: 4px;
                padding: 10px 10px 16px;
                font-size: 14px;
                width: 204px;
                transition: all .3s ease-in-out;
                transform: scale(0);
                &.dplayer-comment-setting-open {
                    transform: scale(1);
                }
                input[type=radio] {
                    display: none;
                }
                label {
                    cursor: pointer;
                }
                .dplayer-comment-setting-title {
                    font-size: 13px;
                    color: #fff;
                    line-height: 30px;
                }
                .dplayer-comment-setting-type {
                    font-size: 0;
                    .dplayer-comment-setting-title {
                        margin-bottom: 6px;
                    }
                    label {
                        &:nth-child(2) {
                            span {
                                border-radius: 4px 0 0 4px;
                            }
                        }
                        &:nth-child(4) {
                            span {
                                border-radius: 0 4px 4px 0;
                            }
                        }
                    }
                    span {
                        width: 33%;
                        padding: 4px 6px;
                        line-height: 16px;
                        display: inline-block;
                        font-size: 12px;
                        color: #fff;
                        border: 1px solid #fff;
                        margin-right: -1px;
                        box-sizing: border-box;
                        text-align: center;
                        cursor: pointer;
                    }
                    input:checked+span {
                        background: #E4E4E6;
                        color: #1c1c1c;
                    }
                }
                .dplayer-comment-setting-color {
                    font-size: 0;
                    label {
                        font-size: 0;
                        padding: 6px;
                        display: inline-block;
                    }
                    span {
                        width: 22px;
                        height: 22px;
                        display: inline-block;
                        border-radius: 50%;
                        box-sizing: border-box;
                        cursor: pointer;
                        &:hover {
                            animation: my-face 5s infinite ease-in-out;
                        }
                    }
                }
            }
            .dplayer-comment-input {
                outline: none;
                border: none;
                padding: 8px 31px;
                font-size: 14px;
                line-height: 18px;
                text-align: center;
                border-radius: 4px;
                background: none;
                margin: 0;
                height: 100%;
                box-sizing: border-box;
                width: 100%;
                color: #fff;
                &::placeholder {
                    color: #fff;
                    opacity: 0.8;
                }
                &::-ms-clear {
                    display: none;
                }
            }
        }
        &.dplayer-icons-left {
            .dplayer-icon {
                padding: 7px;
            }
        }
        &.dplayer-icons-right {
            right: 20px;
            .dplayer-icon {
                padding: 8px;
            }
        }
        .dplayer-time,
        .dplayer-live-badge {
            line-height: 38px;
            color: #eee;
            text-shadow: 0 0 2px rgba(0, 0, 0, .5);
            vertical-align: middle;
            font-size: 13px;
            cursor: default;
        }
        .dplayer-live-dot {
            display: inline-block;
            width: 6px;
            height: 6px;
            vertical-align: 4%;
            margin-right: 5px;
            content: '';
            border-radius: 6px;
        }
        .dplayer-icon {
            width: 40px;
            height: 100%;
            border: none;
            background-color: transparent;
            outline: none;
            cursor: pointer;
            vertical-align: middle;
            box-sizing: border-box;
            display: inline-block;
            .dplayer-icon-content {
                transition: all .2s ease-in-out;
                opacity: .8;
            }
            &:hover {
                .dplayer-icon-content {
                    opacity: 1;
                }
            }
            &.dplayer-quality-icon {
                color: #fff;
                width: auto;
                line-height: 22px;
                font-size: 14px;
            }
            &.dplayer-comment-icon {
                padding: 10px 9px 9px;
            }
            &.dplayer-setting-icon {
                padding-top: 8.5px;
            }
            &.dplayer-volume-icon {
                width: 43px;
            }
        }
        .dplayer-volume {
            position: relative;
            display: inline-block;
            cursor: pointer;
            height: 100%;
            &:hover {
                .dplayer-volume-bar-wrap .dplayer-volume-bar {
                    width: 45px;
                }
                .dplayer-volume-bar-wrap .dplayer-volume-bar .dplayer-volume-bar-inner .dplayer-thumb {
                    transform: scale(1);
                }
            }
            &.dplayer-volume-active {
                .dplayer-volume-bar-wrap .dplayer-volume-bar {
                    width: 45px;
                }
                .dplayer-volume-bar-wrap .dplayer-volume-bar .dplayer-volume-bar-inner .dplayer-thumb {
                    transform: scale(1);
                }
            }
            .dplayer-volume-bar-wrap {
                display: inline-block;
                margin: 0 10px 0 -5px;
                vertical-align: middle;
                height: 100%;
                .dplayer-volume-bar {
                    position: relative;
                    top: 17px;
                    width: 0;
                    height: 3px;
                    background: #aaa;
                    transition: all 0.3s ease-in-out;
                    .dplayer-volume-bar-inner {
                        position: absolute;
                        bottom: 0;
                        left: 0;
                        height: 100%;
                        transition: all 0.1s ease;
                        will-change: width;
                        .dplayer-thumb {
                            position: absolute;
                            top: 0;
                            right: 5px;
                            margin-top: -4px;
                            margin-right: -10px;
                            height: 11px;
                            width: 11px;
                            border-radius: 50%;
                            cursor: pointer;
                            transition: all .3s ease-in-out;
                            transform: scale(0);
                        }
                    }
                }
            }
        }
        .dplayer-subtitle-btn {
            display: inline-block;
            height: 100%;
        }
        .dplayer-subtitles {
            display: inline-block;
            height: 100%;
            .dplayer-subtitles-box {
                position: absolute;
                right: 0;
                bottom: 50px;
                transform: scale(0);
                width: fit-content;
                max-width: 240px;
                min-width: 120px;
                border-radius: 2px;
                background: rgba(28, 28, 28, 0.9);
                padding: 7px 0;
                transition: all .3s ease-in-out;
                overflow: auto;
                z-index: 2;
                &.dplayer-subtitles-panel {
                    display: block;
                }
                &.dplayer-subtitles-box-open {
                    transform: scale(1);
                }
            }
            .dplayer-subtitles-item {
                height: 30px;
                padding: 5px 10px;
                box-sizing: border-box;
                cursor: pointer;
                position: relative;
                &:hover {
                    background-color: rgba(255, 255, 255, .1);
                }
            }
        }
        .dplayer-setting {
            display: inline-block;
            height: 100%;
            .dplayer-setting-box {
                position: absolute;
                right: 0;
                bottom: 50px;
                transform: scale(0);
                width: 150px;
                border-radius: 2px;
                background: rgba(28, 28, 28, 0.9);
                padding: 7px 0;
                transition: all .3s ease-in-out;
                overflow: hidden;
                z-index: 2;
                &>div {
                    display: none;
                    &.dplayer-setting-origin-panel {
                        display: block;
                    }
                }
                &.dplayer-setting-box-open {
                    transform: scale(1);
                }
                &.dplayer-setting-box-narrow {
                    width: 70px;
                    text-align: center;
                }
                &.dplayer-setting-box-speed {
                    .dplayer-setting-origin-panel {
                        display: none;
                    }
                    .dplayer-setting-speed-panel {
                        display: block;
                    }
                }
            }
            .dplayer-setting-item,
            .dplayer-setting-speed-item {
                height: 30px;
                padding: 5px 10px;
                box-sizing: border-box;
                cursor: pointer;
                position: relative;
                &:hover {
                    background-color: rgba(255, 255, 255, .1);
                }
            }
            .dplayer-setting-danmaku {
                padding: 5px 0;
                .dplayer-label {
                    padding: 0 10px;
                    display: inline;
                }
                &:hover {
                    .dplayer-label {
                        display: none;
                    }
                    .dplayer-danmaku-bar-wrap {
                        display: inline-block;
                    }
                }
                &.dplayer-setting-danmaku-active {
                    .dplayer-label {
                        display: none;
                    }
                    .dplayer-danmaku-bar-wrap {
                        display: inline-block;
                    }
                }
                .dplayer-danmaku-bar-wrap {
                    padding: 0 10px;
                    box-sizing: border-box;
                    display: none;
                    vertical-align: middle;
                    height: 100%;
                    width: 100%;
                    .dplayer-danmaku-bar {
                        position: relative;
                        top: 8.5px;
                        width: 100%;
                        height: 3px;
                        background: #fff;
                        transition: all 0.3s ease-in-out;
                        .dplayer-danmaku-bar-inner {
                            position: absolute;
                            bottom: 0;
                            left: 0;
                            height: 100%;
                            transition: all 0.1s ease;
                            background: #aaa;
                            will-change: width;
                            .dplayer-thumb {
                                position: absolute;
                                top: 0;
                                right: 5px;
                                margin-top: -4px;
                                margin-right: -10px;
                                height: 11px;
                                width: 11px;
                                border-radius: 50%;
                                cursor: pointer;
                                transition: all .3s ease-in-out;
                                background: #aaa;
                            }
                        }
                    }
                }
            }
        }
        .dplayer-full {
            display: inline-block;
            height: 100%;
            position: relative;
            &:hover {
                .dplayer-full-in-icon {
                    display: block;
                }
            }
            .dplayer-full-in-icon {
                position: absolute;
                top: -30px;
                z-index: 1;
                display: none;
            }
        }
        .dplayer-quality {
            position: relative;
            display: inline-block;
            height: 100%;
            z-index: 2;
            &:hover {
                .dplayer-quality-list {
                    display: block;
                }
                .dplayer-quality-mask {
                    display: block;
                }
            }
            .dplayer-quality-mask {
                display: none;
                position: absolute;
                bottom: 38px;
                left: -18px;
                width: 80px;
                padding-bottom: 12px;
            }
            .dplayer-quality-list {
                display: none;
                font-size: 12px;
                width: 80px;
                border-radius: 2px;
                background: rgba(28, 28, 28, 0.9);
                padding: 5px 0;
                transition: all .3s ease-in-out;
                overflow: hidden;
                color: #fff;
                text-align: center;
            }
            .dplayer-quality-item {
                height: 25px;
                box-sizing: border-box;
                cursor: pointer;
                line-height: 25px;
                &:hover {
                    background-color: rgba(255, 255, 255, .1);
                }
            }
        }
        .dplayer-comment {
            display: inline-block;
            height: 100%;
        }
        .dplayer-label {
            color: #eee;
            font-size: 13px;
            display: inline-block;
            vertical-align: middle;
            white-space: nowrap;
        }
        .dplayer-toggle {
            width: 32px;
            height: 20px;
            text-align: center;
            font-size: 0;
            vertical-align: middle;
            position: absolute;
            top: 5px;
            right: 10px;
            input {
                max-height: 0;
                max-width: 0;
                display: none;
            }
            input+label {
                display: inline-block;
                position: relative;
                box-shadow: rgb(223, 223, 223) 0 0 0 0 inset;
                border: 1px solid rgb(223, 223, 223);
                height: 20px;
                width: 32px;
                border-radius: 10px;
                box-sizing: border-box;
                cursor: pointer;
                transition: .2s ease-in-out;
            }
            input+label:before {
                content: "";
                position: absolute;
                display: block;
                height: 18px;
                width: 18px;
                top: 0;
                left: 0;
                border-radius: 15px;
                transition: .2s ease-in-out;
            }
            input+label:after {
                content: "";
                position: absolute;
                display: block;
                left: 0;
                top: 0;
                border-radius: 15px;
                background: #fff;
                transition: .2s ease-in-out;
                box-shadow: 0 1px 3px rgba(0, 0, 0, 0.4);
                height: 18px;
                width: 18px;
            }
            input:checked+label {
                border-color: rgba(255, 255, 255, 0.5);
            }
            input:checked+label:before {
                width: 30px;
                background: rgba(255, 255, 255, 0.5);
            }
            input:checked+label:after {
                left: 12px;
            }
        }
    }
}

.dplayer-mobile-play {
    display: none;
    width: 50px;
    height: 50px;
    border: none;
    background-color: transparent;
    outline: none;
    cursor: pointer;
    box-sizing: border-box;
    position: absolute;
    bottom: 0;
    opacity: 0.8;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
}`,`.dplayer-danmaku {
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    font-size: 22px;
    color: #fff;
    .dplayer-danmaku-item {
        display: inline-block;
        pointer-events: none;
        user-select: none;
        cursor: default;
        white-space: nowrap;
        text-shadow: .5px .5px .5px rgba(0, 0, 0, .5);
        &--demo {
            position: absolute;
            visibility: hidden;
        }
    }
    .dplayer-danmaku-right {
        position: absolute;
        right: 0;
        transform: translateX(100%);
        &.dplayer-danmaku-move {
            will-change: transform;
            animation-name: 'danmaku';
            animation-timing-function: linear;
            animation-play-state: paused;
        }
    }
    @keyframes danmaku {
        from {
            transform: translateX(100%);
        }
    }
    .dplayer-danmaku-top,
    .dplayer-danmaku-bottom {
        position: absolute;
        width: 100%;
        text-align: center;
        visibility: hidden;
        &.dplayer-danmaku-move {
            will-change: visibility;
            animation-name: 'danmaku-center';
            animation-timing-function: linear;
            animation-play-state: paused;
        }
    }
    @keyframes danmaku-center {
        from {
            visibility: visible;
        }
        to {
            visibility: visible;
        }
    }
}`,`.dplayer-logo {
    pointer-events: none;
    position: absolute;
    left: 20px;
    top: 20px;
    max-width: 50px;
    max-height: 50px;
    img {
        max-width: 100%;
        max-height: 100%;
        background: none;
    }
}`,`.dplayer-menu {
    position: absolute;
    width: 170px;
    border-radius: 2px;
    background: rgba(28, 28, 28, 0.85);
    padding: 5px 0;
    overflow: hidden;
    z-index: 3;
    display: none;
    &.dplayer-menu-show {
        display: block;
    }
    .dplayer-menu-item {
        height: 30px;
        box-sizing: border-box;
        cursor: pointer;
        &:hover {
            background-color: rgba(255, 255, 255, .1);
        }
        a {
            display: inline-block;
            padding: 0 10px;
            line-height: 30px;
            color: #eee;
            font-size: 13px;
            display: inline-block;
            vertical-align: middle;
            width: 100%;
            box-sizing: border-box;
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden;
            &:hover {
                text-decoration: none;
            }
        }
    }
}`,`.dplayer-subtitle {
    position: absolute;
    bottom: 40px;
    width: 90%;
    left: 5%;
    text-align: center;
    color: #fff;
    text-shadow: 0.5px 0.5px 0.5px rgba(0, 0, 0, 0.5);
    font-size: 20px;
    &.dplayer-subtitle-hide {
        display: none;
    }
}`,`.dplayer-mask {
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    z-index: 1;
    display: none;
    &.dplayer-mask-show {
        display: block;
    }
}

.dplayer-video-wrap {
    position: relative;
    background: #000;
    font-size: 0;
    width: 100%;
    height: 100%;
    .dplayer-video {
        width: 100%;
        height: 100%;
        display: none;
    }
    .dplayer-video-current {
        display: block;
    }
    .dplayer-video-prepare {
        display: none;
    }
}`,`.dplayer-info-panel {
    position: absolute;
    top: 10px;
    left: 10px;
    width: 400px;
    background: rgba(28, 28, 28, 0.8);
    padding: 10px;
    color: #fff;
    font-size: 12px;
    border-radius: 2px;

    &-hide {
        display: none;
    }

    .dplayer-info-panel-close {
        cursor: pointer;
        position: absolute;
        right: 10px;
        top: 10px;
    }

    .dplayer-info-panel-item {
        & > span {
            display: inline-block;
            vertical-align: middle;
            line-height: 15px;
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden;
        }
    }

    .dplayer-info-panel-item-title {
        width: 100px;
        text-align: right;
        margin-right: 10px;
    }
    
    .dplayer-info-panel-item-data {
        width: 260px;
    }
}`],sourceRoot:""}]);const Se=ve},856:q=>{"use strict";var j=[];function Z(ee){for(var R=-1,ie=0;ie<j.length;ie++)if(j[ie].identifier===ee){R=ie;break}return R}function le(ee,R){for(var ie={},se=[],V=0;V<ee.length;V++){var ue=ee[V],ve=R.base?ue[0]+R.base:ue[0],de=ie[ve]||0,Se="".concat(ve," ").concat(de);ie[ve]=de+1;var ge=Z(Se),st={css:ue[1],media:ue[2],sourceMap:ue[3],supports:ue[4],layer:ue[5]};if(ge!==-1)j[ge].references++,j[ge].updater(st);else{var oe=ce(st,R);R.byIndex=V,j.splice(V,0,{identifier:Se,updater:oe,references:1})}se.push(Se)}return se}function ce(ee,R){var ie=R.domAPI(R);return ie.update(ee),function(se){if(se){if(se.css===ee.css&&se.media===ee.media&&se.sourceMap===ee.sourceMap&&se.supports===ee.supports&&se.layer===ee.layer)return;ie.update(ee=se)}else ie.remove()}}q.exports=function(ee,R){var ie=le(ee=ee||[],R=R||{});return function(se){se=se||[];for(var V=0;V<ie.length;V++){var ue=Z(ie[V]);j[ue].references--}for(var ve=le(se,R),de=0;de<ie.length;de++){var Se=Z(ie[de]);j[Se].references===0&&(j[Se].updater(),j.splice(Se,1))}ie=ve}}},370:q=>{"use strict";var j={};q.exports=function(Z,le){var ce=function(ee){if(j[ee]===void 0){var R=document.querySelector(ee);if(window.HTMLIFrameElement&&R instanceof window.HTMLIFrameElement)try{R=R.contentDocument.head}catch(ie){R=null}j[ee]=R}return j[ee]}(Z);if(!ce)throw new Error("Couldn't find a style target. This probably means that the value for the 'insert' parameter is invalid.");ce.appendChild(le)}},278:q=>{"use strict";q.exports=function(j){var Z=document.createElement("style");return j.setAttributes(Z,j.attributes),j.insert(Z,j.options),Z}},458:(q,j,Z)=>{"use strict";q.exports=function(le){var ce=Z.nc;ce&&le.setAttribute("nonce",ce)}},470:q=>{"use strict";q.exports=function(j){var Z=j.insertStyleElement(j);return{update:function(le){(function(ce,ee,R){var ie="";R.supports&&(ie+="@supports (".concat(R.supports,") {")),R.media&&(ie+="@media ".concat(R.media," {"));var se=R.layer!==void 0;se&&(ie+="@layer".concat(R.layer.length>0?" ".concat(R.layer):""," {")),ie+=R.css,se&&(ie+="}"),R.media&&(ie+="}"),R.supports&&(ie+="}");var V=R.sourceMap;V&&typeof btoa!="undefined"&&(ie+=`
/*# sourceMappingURL=data:application/json;base64,`.concat(btoa(unescape(encodeURIComponent(JSON.stringify(V))))," */")),ee.styleTagTransform(ie,ce,ee.options)})(Z,j,le)},remove:function(){(function(le){if(le.parentNode===null)return!1;le.parentNode.removeChild(le)})(Z)}}}},488:q=>{"use strict";q.exports=function(j,Z){if(Z.styleSheet)Z.styleSheet.cssText=j;else{for(;Z.firstChild;)Z.removeChild(Z.firstChild);Z.appendChild(document.createTextNode(j))}}},251:q=>{q.exports='<svg viewBox="0 0 288 288" xmlns="http://www.w3.org/2000/svg"><path d="M288 90v96c0 20-16 36-36 36h-10c-16 0-16-24 0-24h10c7 0 12-5 12-12V90c0-7-5-12-12-12H36c-7 0-12 5-12 12v96c0 7 5 12 12 12h10c16 0 16 24 0 24H36c-20 0-36-16-36-36V90c0-20 16-36 36-36h216c20 0 36 16 36 36zm-120 62l48 68c14 20 1 38-20 38H92c-21 0-34-18-20-38l48-68c13-18 35-18 48 0z"></path></svg>'},113:q=>{q.exports='<svg xmlns="http://www.w3.org/2000/svg" version="1.1" viewBox="0 0 32 32"><path d="M16 23c-3.309 0-6-2.691-6-6s2.691-6 6-6 6 2.691 6 6-2.691 6-6 6zM16 13c-2.206 0-4 1.794-4 4s1.794 4 4 4c2.206 0 4-1.794 4-4s-1.794-4-4-4zM27 28h-22c-1.654 0-3-1.346-3-3v-16c0-1.654 1.346-3 3-3h3c0.552 0 1 0.448 1 1s-0.448 1-1 1h-3c-0.551 0-1 0.449-1 1v16c0 0.552 0.449 1 1 1h22c0.552 0 1-0.448 1-1v-16c0-0.551-0.448-1-1-1h-11c-0.552 0-1-0.448-1-1s0.448-1 1-1h11c1.654 0 3 1.346 3 3v16c0 1.654-1.346 3-3 3zM24 10.5c0 0.828 0.672 1.5 1.5 1.5s1.5-0.672 1.5-1.5c0-0.828-0.672-1.5-1.5-1.5s-1.5 0.672-1.5 1.5zM15 4c0 0.552-0.448 1-1 1h-4c-0.552 0-1-0.448-1-1v0c0-0.552 0.448-1 1-1h4c0.552 0 1 0.448 1 1v0z"></path></svg>'},193:q=>{q.exports='<svg aria-hidden="true" focusable="false" data-prefix="fab" data-icon="chromecast" class="svg-inline--fa fa-chromecast fa-w-16" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path fill="currentColor" d="M447.8,64H64c-23.6,0-42.7,19.1-42.7,42.7v63.9H64v-63.9h383.8v298.6H298.6V448H448c23.6,0,42.7-19.1,42.7-42.7V106.7 C490.7,83.1,471.4,64,447.8,64z M21.3,383.6L21.3,383.6l0,63.9h63.9C85.2,412.2,56.6,383.6,21.3,383.6L21.3,383.6z M21.3,298.6V341 c58.9,0,106.6,48.1,106.6,107h42.7C170.7,365.6,103.7,298.7,21.3,298.6z M213.4,448h42.7c-0.5-129.5-105.3-234.3-234.8-234.6l0,42.4 C127.3,255.6,213.3,342,213.4,448z"></path></svg>'},338:q=>{q.exports='<svg xmlns="http://www.w3.org/2000/svg" version="1.1" viewBox="0 0 32 32"><path d="M27.090 0.131h-22.731c-2.354 0-4.262 1.839-4.262 4.109v16.401c0 2.269 1.908 4.109 4.262 4.109h4.262v-2.706h8.469l-8.853 8.135 1.579 1.451 7.487-6.88h9.787c2.353 0 4.262-1.84 4.262-4.109v-16.401c0-2.27-1.909-4.109-4.262-4.109v0zM28.511 19.304c0 1.512-1.272 2.738-2.841 2.738h-8.425l-0.076-0.070-0.076 0.070h-11.311c-1.569 0-2.841-1.226-2.841-2.738v-13.696c0-1.513 1.272-2.739 2.841-2.739h19.889c1.569 0 2.841-0.142 2.841 1.37v15.064z"></path></svg>'},807:q=>{q.exports='<svg xmlns="http://www.w3.org/2000/svg" version="1.1" viewBox="0 0 32 32"><path d="M27.128 0.38h-22.553c-2.336 0-4.229 1.825-4.229 4.076v16.273c0 2.251 1.893 4.076 4.229 4.076h4.229v-2.685h8.403l-8.784 8.072 1.566 1.44 7.429-6.827h9.71c2.335 0 4.229-1.825 4.229-4.076v-16.273c0-2.252-1.894-4.076-4.229-4.076zM28.538 19.403c0 1.5-1.262 2.717-2.819 2.717h-8.36l-0.076-0.070-0.076 0.070h-11.223c-1.557 0-2.819-1.217-2.819-2.717v-13.589c0-1.501 1.262-2.718 2.819-2.718h19.734c1.557 0 2.819-0.141 2.819 1.359v14.947zM9.206 10.557c-1.222 0-2.215 0.911-2.215 2.036s0.992 2.035 2.215 2.035c1.224 0 2.216-0.911 2.216-2.035s-0.992-2.036-2.216-2.036zM22.496 10.557c-1.224 0-2.215 0.911-2.215 2.036s0.991 2.035 2.215 2.035c1.224 0 2.215-0.911 2.215-2.035s-0.991-2.036-2.215-2.036zM15.852 10.557c-1.224 0-2.215 0.911-2.215 2.036s0.991 2.035 2.215 2.035c1.222 0 2.215-0.911 2.215-2.035s-0.992-2.036-2.215-2.036z"></path></svg>'},300:q=>{q.exports='<svg xmlns="http://www.w3.org/2000/svg" version="1.1" viewBox="0 0 32 33"><path d="M24.965 24.38h-18.132c-1.366 0-2.478-1.113-2.478-2.478v-11.806c0-1.364 1.111-2.478 2.478-2.478h18.132c1.366 0 2.478 1.113 2.478 2.478v11.806c0 1.364-1.11 2.478-2.478 2.478zM6.833 10.097v11.806h18.134l-0.002-11.806h-18.132zM2.478 28.928h5.952c0.684 0 1.238-0.554 1.238-1.239 0-0.684-0.554-1.238-1.238-1.238h-5.952v-5.802c0-0.684-0.554-1.239-1.238-1.239s-1.239 0.556-1.239 1.239v5.802c0 1.365 1.111 2.478 2.478 2.478zM30.761 19.412c-0.684 0-1.238 0.554-1.238 1.238v5.801h-5.951c-0.686 0-1.239 0.554-1.239 1.238 0 0.686 0.554 1.239 1.239 1.239h5.951c1.366 0 2.478-1.111 2.478-2.478v-5.801c0-0.683-0.554-1.238-1.239-1.238zM0 5.55v5.802c0 0.683 0.554 1.238 1.238 1.238s1.238-0.555 1.238-1.238v-5.802h5.952c0.684 0 1.238-0.554 1.238-1.238s-0.554-1.238-1.238-1.238h-5.951c-1.366-0.001-2.478 1.111-2.478 2.476zM32 11.35v-5.801c0-1.365-1.11-2.478-2.478-2.478h-5.951c-0.686 0-1.239 0.554-1.239 1.238s0.554 1.238 1.239 1.238h5.951v5.801c0 0.683 0.554 1.237 1.238 1.237 0.686 0.002 1.239-0.553 1.239-1.236z"></path></svg>'},574:q=>{q.exports='<svg xmlns="http://www.w3.org/2000/svg" version="1.1" viewBox="0 0 32 33"><path d="M6.667 28h-5.333c-0.8 0-1.333-0.533-1.333-1.333v-5.333c0-0.8 0.533-1.333 1.333-1.333s1.333 0.533 1.333 1.333v4h4c0.8 0 1.333 0.533 1.333 1.333s-0.533 1.333-1.333 1.333zM30.667 28h-5.333c-0.8 0-1.333-0.533-1.333-1.333s0.533-1.333 1.333-1.333h4v-4c0-0.8 0.533-1.333 1.333-1.333s1.333 0.533 1.333 1.333v5.333c0 0.8-0.533 1.333-1.333 1.333zM30.667 12c-0.8 0-1.333-0.533-1.333-1.333v-4h-4c-0.8 0-1.333-0.533-1.333-1.333s0.533-1.333 1.333-1.333h5.333c0.8 0 1.333 0.533 1.333 1.333v5.333c0 0.8-0.533 1.333-1.333 1.333zM1.333 12c-0.8 0-1.333-0.533-1.333-1.333v-5.333c0-0.8 0.533-1.333 1.333-1.333h5.333c0.8 0 1.333 0.533 1.333 1.333s-0.533 1.333-1.333 1.333h-4v4c0 0.8-0.533 1.333-1.333 1.333z"></path></svg>'},182:q=>{q.exports='<svg version="1.1" viewBox="0 0 22 22"><svg x="7" y="1"><circle class="diplayer-loading-dot diplayer-loading-dot-0" cx="4" cy="4" r="2"></circle></svg><svg x="11" y="3"><circle class="diplayer-loading-dot diplayer-loading-dot-1" cx="4" cy="4" r="2"></circle></svg><svg x="13" y="7"><circle class="diplayer-loading-dot diplayer-loading-dot-2" cx="4" cy="4" r="2"></circle></svg><svg x="11" y="11"><circle class="diplayer-loading-dot diplayer-loading-dot-3" cx="4" cy="4" r="2"></circle></svg><svg x="7" y="13"><circle class="diplayer-loading-dot diplayer-loading-dot-4" cx="4" cy="4" r="2"></circle></svg><svg x="3" y="11"><circle class="diplayer-loading-dot diplayer-loading-dot-5" cx="4" cy="4" r="2"></circle></svg><svg x="1" y="7"><circle class="diplayer-loading-dot diplayer-loading-dot-6" cx="4" cy="4" r="2"></circle></svg><svg x="3" y="3"><circle class="diplayer-loading-dot diplayer-loading-dot-7" cx="4" cy="4" r="2"></circle></svg></svg>'},965:q=>{q.exports='<svg xmlns="http://www.w3.org/2000/svg" version="1.1" viewBox="0 0 32 32"><path d="M19.357 2.88c1.749 0 3.366 0.316 4.851 0.946 1.485 0.632 2.768 1.474 3.845 2.533s1.922 2.279 2.532 3.661c0.611 1.383 0.915 2.829 0.915 4.334 0 1.425-0.304 2.847-0.915 4.271-0.611 1.425-1.587 2.767-2.928 4.028-0.855 0.813-1.811 1.607-2.869 2.38s-2.136 1.465-3.233 2.075c-1.099 0.61-2.198 1.098-3.296 1.465-1.098 0.366-2.115 0.549-3.051 0.549-1.343 0-2.441-0.438-3.296-1.311-0.854-0.876-1.281-2.41-1.281-4.608 0-0.366 0.020-0.773 0.060-1.221s0.062-0.895 0.062-1.343c0-0.773-0.183-1.353-0.55-1.738-0.366-0.387-0.793-0.58-1.281-0.58-0.652 0-1.21 0.295-1.678 0.886s-0.926 1.23-1.373 1.921c-0.447 0.693-0.905 1.334-1.372 1.923s-1.028 0.886-1.679 0.886c-0.529 0-1.048-0.427-1.556-1.282s-0.763-2.259-0.763-4.212c0-2.197 0.529-4.241 1.587-6.133s2.462-3.529 4.21-4.912c1.75-1.383 3.762-2.471 6.041-3.264 2.277-0.796 4.617-1.212 7.018-1.253zM7.334 15.817c0.569 0 1.047-0.204 1.434-0.611s0.579-0.875 0.579-1.404c0-0.569-0.193-1.047-0.579-1.434s-0.864-0.579-1.434-0.579c-0.529 0-0.987 0.193-1.373 0.579s-0.58 0.864-0.58 1.434c0 0.53 0.194 0.998 0.58 1.404 0.388 0.407 0.845 0.611 1.373 0.611zM12.216 11.79c0.691 0 1.292-0.254 1.8-0.763s0.762-1.107 0.762-1.8c0-0.732-0.255-1.343-0.762-1.831-0.509-0.489-1.109-0.732-1.8-0.732-0.732 0-1.342 0.244-1.831 0.732-0.488 0.488-0.732 1.098-0.732 1.831 0 0.693 0.244 1.292 0.732 1.8s1.099 0.763 1.831 0.763zM16.366 25.947c0.692 0 1.282-0.214 1.77-0.64s0.732-0.987 0.732-1.678-0.244-1.261-0.732-1.709c-0.489-0.448-1.078-0.671-1.77-0.671-0.65 0-1.21 0.223-1.678 0.671s-0.702 1.018-0.702 1.709c0 0.692 0.234 1.25 0.702 1.678s1.027 0.64 1.678 0.64zM19.113 9.592c0.651 0 1.129-0.203 1.433-0.611 0.305-0.406 0.459-0.874 0.459-1.404 0-0.488-0.154-0.947-0.459-1.373-0.304-0.427-0.782-0.641-1.433-0.641-0.529 0-1.008 0.193-1.434 0.58s-0.64 0.865-0.64 1.434c0 0.571 0.213 1.049 0.64 1.434 0.427 0.389 0.905 0.581 1.434 0.581zM24.848 12.826c0.57 0 1.067-0.213 1.495-0.64 0.427-0.427 0.64-0.947 0.64-1.556 0-0.57-0.214-1.068-0.64-1.495-0.428-0.427-0.927-0.64-1.495-0.64-0.611 0-1.129 0.213-1.555 0.64-0.428 0.427-0.642 0.926-0.642 1.495 0 0.611 0.213 1.129 0.642 1.556s0.947 0.64 1.555 0.64z"></path></svg>'},74:q=>{q.exports='<svg xmlns="http://www.w3.org/2000/svg" version="1.1" viewBox="0 0 17 32"><path d="M14.080 4.8q2.88 0 2.88 2.048v18.24q0 2.112-2.88 2.112t-2.88-2.112v-18.24q0-2.048 2.88-2.048zM2.88 4.8q2.88 0 2.88 2.048v18.24q0 2.112-2.88 2.112t-2.88-2.112v-18.24q0-2.048 2.88-2.048z"></path></svg>'},730:q=>{q.exports='<svg xmlns="http://www.w3.org/2000/svg" version="1.1" viewBox="0 0 16 32"><path d="M15.552 15.168q0.448 0.32 0.448 0.832 0 0.448-0.448 0.768l-13.696 8.512q-0.768 0.512-1.312 0.192t-0.544-1.28v-16.448q0-0.96 0.544-1.28t1.312 0.192z"></path></svg>'},428:q=>{q.exports='<svg xmlns="http://www.w3.org/2000/svg" version="1.1" viewBox="0 0 32 32"><path d="M22 16l-10.105-10.6-1.895 1.987 8.211 8.613-8.211 8.612 1.895 1.988 8.211-8.613z"></path></svg>'},254:q=>{q.exports='<svg xmlns="http://www.w3.org/2000/svg" version="1.1" viewBox="0 0 32 32"><path d="M13.725 30l3.9-5.325-3.9-1.125v6.45zM0 17.5l11.050 3.35 13.6-11.55-10.55 12.425 11.8 3.65 6.1-23.375-32 15.5z"></path></svg>'},934:q=>{q.exports='<svg xmlns="http://www.w3.org/2000/svg" version="1.1" viewBox="0 0 32 28"><path d="M28.633 17.104c0.035 0.21 0.026 0.463-0.026 0.76s-0.14 0.598-0.262 0.904c-0.122 0.306-0.271 0.581-0.445 0.825s-0.367 0.419-0.576 0.524c-0.209 0.105-0.393 0.157-0.55 0.157s-0.332-0.035-0.524-0.105c-0.175-0.052-0.393-0.1-0.655-0.144s-0.528-0.052-0.799-0.026c-0.271 0.026-0.541 0.083-0.812 0.17s-0.502 0.236-0.694 0.445c-0.419 0.437-0.664 0.934-0.734 1.493s0.009 1.092 0.236 1.598c0.175 0.349 0.148 0.699-0.079 1.048-0.105 0.14-0.271 0.284-0.498 0.432s-0.476 0.284-0.747 0.406-0.555 0.218-0.851 0.288c-0.297 0.070-0.559 0.105-0.786 0.105-0.157 0-0.306-0.061-0.445-0.183s-0.236-0.253-0.288-0.393h-0.026c-0.192-0.541-0.52-1.009-0.982-1.402s-1-0.589-1.611-0.589c-0.594 0-1.131 0.197-1.611 0.589s-0.816 0.851-1.009 1.375c-0.087 0.21-0.218 0.362-0.393 0.458s-0.367 0.144-0.576 0.144c-0.244 0-0.52-0.044-0.825-0.131s-0.611-0.197-0.917-0.327c-0.306-0.131-0.581-0.284-0.825-0.458s-0.428-0.349-0.55-0.524c-0.087-0.122-0.135-0.266-0.144-0.432s0.057-0.397 0.197-0.694c0.192-0.402 0.266-0.86 0.223-1.375s-0.266-0.991-0.668-1.428c-0.244-0.262-0.541-0.432-0.891-0.511s-0.681-0.109-0.995-0.092c-0.367 0.017-0.742 0.087-1.127 0.21-0.244 0.070-0.489 0.052-0.734-0.052-0.192-0.070-0.371-0.231-0.537-0.485s-0.314-0.533-0.445-0.838c-0.131-0.306-0.231-0.62-0.301-0.943s-0.087-0.59-0.052-0.799c0.052-0.384 0.227-0.629 0.524-0.734 0.524-0.21 0.995-0.555 1.415-1.035s0.629-1.017 0.629-1.611c0-0.611-0.21-1.144-0.629-1.598s-0.891-0.786-1.415-0.996c-0.157-0.052-0.288-0.179-0.393-0.38s-0.157-0.406-0.157-0.616c0-0.227 0.035-0.48 0.105-0.76s0.162-0.55 0.275-0.812 0.244-0.502 0.393-0.72c0.148-0.218 0.31-0.38 0.485-0.485 0.14-0.087 0.275-0.122 0.406-0.105s0.275 0.052 0.432 0.105c0.524 0.21 1.070 0.275 1.637 0.197s1.070-0.327 1.506-0.747c0.21-0.209 0.362-0.467 0.458-0.773s0.157-0.607 0.183-0.904c0.026-0.297 0.026-0.568 0-0.812s-0.048-0.419-0.065-0.524c-0.035-0.105-0.066-0.227-0.092-0.367s-0.013-0.262 0.039-0.367c0.105-0.244 0.293-0.458 0.563-0.642s0.563-0.336 0.878-0.458c0.314-0.122 0.62-0.214 0.917-0.275s0.533-0.092 0.707-0.092c0.227 0 0.406 0.074 0.537 0.223s0.223 0.301 0.275 0.458c0.192 0.471 0.507 0.886 0.943 1.244s0.952 0.537 1.546 0.537c0.611 0 1.153-0.17 1.624-0.511s0.803-0.773 0.996-1.297c0.070-0.14 0.179-0.284 0.327-0.432s0.301-0.223 0.458-0.223c0.244 0 0.511 0.035 0.799 0.105s0.572 0.166 0.851 0.288c0.279 0.122 0.537 0.279 0.773 0.472s0.423 0.402 0.563 0.629c0.087 0.14 0.113 0.293 0.079 0.458s-0.070 0.284-0.105 0.354c-0.227 0.506-0.297 1.039-0.21 1.598s0.341 1.048 0.76 1.467c0.419 0.419 0.934 0.651 1.546 0.694s1.179-0.057 1.703-0.301c0.14-0.087 0.31-0.122 0.511-0.105s0.371 0.096 0.511 0.236c0.262 0.244 0.493 0.616 0.694 1.113s0.336 1 0.406 1.506c0.035 0.297-0.013 0.528-0.144 0.694s-0.266 0.275-0.406 0.327c-0.542 0.192-1.004 0.528-1.388 1.009s-0.576 1.026-0.576 1.637c0 0.594 0.162 1.113 0.485 1.559s0.747 0.764 1.27 0.956c0.122 0.070 0.227 0.14 0.314 0.21 0.192 0.157 0.323 0.358 0.393 0.602v0zM16.451 19.462c0.786 0 1.528-0.149 2.227-0.445s1.305-0.707 1.821-1.231c0.515-0.524 0.921-1.131 1.218-1.821s0.445-1.428 0.445-2.214c0-0.786-0.148-1.524-0.445-2.214s-0.703-1.292-1.218-1.808c-0.515-0.515-1.122-0.921-1.821-1.218s-1.441-0.445-2.227-0.445c-0.786 0-1.524 0.148-2.214 0.445s-1.292 0.703-1.808 1.218c-0.515 0.515-0.921 1.118-1.218 1.808s-0.445 1.428-0.445 2.214c0 0.786 0.149 1.524 0.445 2.214s0.703 1.297 1.218 1.821c0.515 0.524 1.118 0.934 1.808 1.231s1.428 0.445 2.214 0.445v0z"></path></svg>'},410:q=>{q.exports='<svg xmlns="http://www.w3.org/2000/svg" version="1.1" viewBox="0 0 32 32"><path d="M26.667 5.333h-21.333c-0 0-0.001 0-0.001 0-1.472 0-2.666 1.194-2.666 2.666 0 0 0 0.001 0 0.001v-0 16c0 0 0 0.001 0 0.001 0 1.472 1.194 2.666 2.666 2.666 0 0 0.001 0 0.001 0h21.333c0 0 0.001 0 0.001 0 1.472 0 2.666-1.194 2.666-2.666 0-0 0-0.001 0-0.001v0-16c0-0 0-0.001 0-0.001 0-1.472-1.194-2.666-2.666-2.666-0 0-0.001 0-0.001 0h0zM5.333 16h5.333v2.667h-5.333v-2.667zM18.667 24h-13.333v-2.667h13.333v2.667zM26.667 24h-5.333v-2.667h5.333v2.667zM26.667 18.667h-13.333v-2.667h13.333v2.667z"></path></svg>'},644:q=>{q.exports='<svg xmlns="http://www.w3.org/2000/svg" version="1.1" viewBox="0 0 21 32"><path d="M13.728 6.272v19.456q0 0.448-0.352 0.8t-0.8 0.32-0.8-0.32l-5.952-5.952h-4.672q-0.48 0-0.8-0.352t-0.352-0.8v-6.848q0-0.48 0.352-0.8t0.8-0.352h4.672l5.952-5.952q0.32-0.32 0.8-0.32t0.8 0.32 0.352 0.8zM20.576 16q0 1.344-0.768 2.528t-2.016 1.664q-0.16 0.096-0.448 0.096-0.448 0-0.8-0.32t-0.32-0.832q0-0.384 0.192-0.64t0.544-0.448 0.608-0.384 0.512-0.64 0.192-1.024-0.192-1.024-0.512-0.64-0.608-0.384-0.544-0.448-0.192-0.64q0-0.48 0.32-0.832t0.8-0.32q0.288 0 0.448 0.096 1.248 0.48 2.016 1.664t0.768 2.528z"></path></svg>'},324:q=>{q.exports='<svg xmlns="http://www.w3.org/2000/svg" version="1.1" viewBox="0 0 21 32"><path d="M13.728 6.272v19.456q0 0.448-0.352 0.8t-0.8 0.32-0.8-0.32l-5.952-5.952h-4.672q-0.48 0-0.8-0.352t-0.352-0.8v-6.848q0-0.48 0.352-0.8t0.8-0.352h4.672l5.952-5.952q0.32-0.32 0.8-0.32t0.8 0.32 0.352 0.8z"></path></svg>'},437:q=>{q.exports='<svg xmlns="http://www.w3.org/2000/svg" version="1.1" viewBox="0 0 21 32"><path d="M13.728 6.272v19.456q0 0.448-0.352 0.8t-0.8 0.32-0.8-0.32l-5.952-5.952h-4.672q-0.48 0-0.8-0.352t-0.352-0.8v-6.848q0-0.48 0.352-0.8t0.8-0.352h4.672l5.952-5.952q0.32-0.32 0.8-0.32t0.8 0.32 0.352 0.8zM20.576 16q0 1.344-0.768 2.528t-2.016 1.664q-0.16 0.096-0.448 0.096-0.448 0-0.8-0.32t-0.32-0.832q0-0.384 0.192-0.64t0.544-0.448 0.608-0.384 0.512-0.64 0.192-1.024-0.192-1.024-0.512-0.64-0.608-0.384-0.544-0.448-0.192-0.64q0-0.48 0.32-0.832t0.8-0.32q0.288 0 0.448 0.096 1.248 0.48 2.016 1.664t0.768 2.528zM25.152 16q0 2.72-1.536 5.056t-4 3.36q-0.256 0.096-0.448 0.096-0.48 0-0.832-0.352t-0.32-0.8q0-0.704 0.672-1.056 1.024-0.512 1.376-0.8 1.312-0.96 2.048-2.4t0.736-3.104-0.736-3.104-2.048-2.4q-0.352-0.288-1.376-0.8-0.672-0.352-0.672-1.056 0-0.448 0.32-0.8t0.8-0.352q0.224 0 0.48 0.096 2.496 1.056 4 3.36t1.536 5.056z"></path></svg>'},897:(q,j,Z)=>{"use strict";var le=typeof self!="undefined"?self:typeof window!="undefined"?window:Z.g!==void 0?Z.g:{},ce=Object.create(le),ee=/["&'<>]/;function R(ie){return typeof ie!="string"&&(ie=ie==null?"":typeof ie=="function"?R(ie.call(ie)):JSON.stringify(ie)),ie}ce.$escape=function(ie){return function(se){var V=""+se,ue=ee.exec(V);if(!ue)return se;var ve="",de=void 0,Se=void 0,ge=void 0;for(de=ue.index,Se=0;de<V.length;de++){switch(V.charCodeAt(de)){case 34:ge="&#34;";break;case 38:ge="&#38;";break;case 39:ge="&#39;";break;case 60:ge="&#60;";break;case 62:ge="&#62;";break;default:continue}Se!==de&&(ve+=V.substring(Se,de)),Se=de+1,ve+=ge}return Se!==de?ve+V.substring(Se,de):ve}(R(ie))},ce.$each=function(ie,se){if(Array.isArray(ie))for(var V=0,ue=ie.length;V<ue;V++)se(ie[V],V);else for(var ve in ie)se(ie[ve],ve)},q.exports=ce},471:(q,j,Z)=>{"use strict";q.exports=Z(897)},352:q=>{"use strict";q.exports=function(j){var Z=[];return Z.toString=function(){return this.map(function(le){var ce="",ee=le[5]!==void 0;return le[4]&&(ce+="@supports (".concat(le[4],") {")),le[2]&&(ce+="@media ".concat(le[2]," {")),ee&&(ce+="@layer".concat(le[5].length>0?" ".concat(le[5]):""," {")),ce+=j(le),ee&&(ce+="}"),le[2]&&(ce+="}"),le[4]&&(ce+="}"),ce}).join("")},Z.i=function(le,ce,ee,R,ie){typeof le=="string"&&(le=[[null,le,void 0]]);var se={};if(ee)for(var V=0;V<this.length;V++){var ue=this[V][0];ue!=null&&(se[ue]=!0)}for(var ve=0;ve<le.length;ve++){var de=[].concat(le[ve]);ee&&se[de[0]]||(ie!==void 0&&(de[5]===void 0||(de[1]="@layer".concat(de[5].length>0?" ".concat(de[5]):""," {").concat(de[1],"}")),de[5]=ie),ce&&(de[2]&&(de[1]="@media ".concat(de[2]," {").concat(de[1],"}")),de[2]=ce),R&&(de[4]?(de[1]="@supports (".concat(de[4],") {").concat(de[1],"}"),de[4]=R):de[4]="".concat(R)),Z.push(de))}},Z}},80:q=>{"use strict";q.exports=function(j,Z){return Z||(Z={}),j&&(j=String(j.__esModule?j.default:j),/^['"].*['"]$/.test(j)&&(j=j.slice(1,-1)),Z.hash&&(j+=Z.hash),/["'() \t\n]|(%20)/.test(j)||Z.needQuotes?'"'.concat(j.replace(/"/g,'\\"').replace(/\n/g,"\\n"),'"'):j)}},415:q=>{"use strict";q.exports=function(j){var Z=j[1],le=j[3];if(!le)return Z;if(typeof btoa=="function"){var ce=btoa(unescape(encodeURIComponent(JSON.stringify(le)))),ee="sourceMappingURL=data:application/json;charset=utf-8;base64,".concat(ce),R="/*# ".concat(ee," */");return[Z].concat([R]).join(`
`)}return[Z].join(`
`)}},937:q=>{function j(Z){return j=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(le){return typeof le}:function(le){return le&&typeof Symbol=="function"&&le.constructor===Symbol&&le!==Symbol.prototype?"symbol":typeof le},j(Z)}q.exports=(typeof self=="undefined"?"undefined":j(self))=="object"?self.FormData:window.FormData},831:q=>{"use strict";q.exports="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAADGCAYAAAAT+OqFAAAAdklEQVQoz42QQQ7AIAgEF/T/D+kbq/RWAlnQyyazA4aoAB4FsBSA/bFjuF1EOL7VbrIrBuusmrt4ZZORfb6ehbWdnRHEIiITaEUKa5EJqUakRSaEYBJSCY2dEstQY7AuxahwXFrvZmWl2rh4JZ07z9dLtesfNj5q0FU3A5ObbwAAAABJRU5ErkJggg=="}},tn={};function Q(q){var j=tn[q];if(j!==void 0)return j.exports;var Z=tn[q]={id:q,exports:{}};return Ke[q](Z,Z.exports,Q),Z.exports}Q.m=Ke,Q.n=q=>{var j=q&&q.__esModule?()=>q.default:()=>q;return Q.d(j,{a:j}),j},Q.d=(q,j)=>{for(var Z in j)Q.o(j,Z)&&!Q.o(q,Z)&&Object.defineProperty(q,Z,{enumerable:!0,get:j[Z]})},Q.g=function(){if(typeof globalThis=="object")return globalThis;try{return this||new Function("return this")()}catch(q){if(typeof window=="object")return window}}(),Q.o=(q,j)=>Object.prototype.hasOwnProperty.call(q,j),Q.b=document.baseURI||self.location.href,Q.nc=void 0;var Nr={};return(()=>{"use strict";Q.d(Nr,{default:()=>Fr});var q=Q(856),j=Q.n(q),Z=Q(470),le=Q.n(Z),ce=Q(370),ee=Q.n(ce),R=Q(458),ie=Q.n(R),se=Q(278),V=Q.n(se),ue=Q(488),ve=Q.n(ue),de=Q(685),Se={};Se.styleTagTransform=ve(),Se.setAttributes=ie(),Se.insert=ee().bind(null,"head"),Se.domAPI=le(),Se.insertStyleElement=V(),j()(de.Z,Se),de.Z&&de.Z.locals&&de.Z.locals;function ge(h){return ge=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},ge(h)}function st(h,v){this.name="AggregateError",this.errors=h,this.message=v||""}st.prototype=Error.prototype;function oe(h){return oe=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},oe(h)}var S=setTimeout;function fe(h){return Boolean(h&&h.length!==void 0)}function U(){}function ke(h){if(!(this instanceof ke))throw new TypeError("Promises must be constructed via new");if(typeof h!="function")throw new TypeError("not a function");this._state=0,this._handled=!1,this._value=void 0,this._deferreds=[],P(h,this)}function In(h,v){for(;h._state===3;)h=h._value;h._state!==0?(h._handled=!0,ke._immediateFn(function(){var x=h._state===1?v.onFulfilled:v.onRejected;if(x!==null){var f;try{f=x(h._value)}catch(m){return void nn(v.promise,m)}Tt(v.promise,f)}else(h._state===1?Tt:nn)(v.promise,h._value)})):h._deferreds.push(v)}function Tt(h,v){try{if(v===h)throw new TypeError("A promise cannot be resolved with itself.");if(v&&(oe(v)==="object"||typeof v=="function")){var x=v.then;if(v instanceof ke)return h._state=3,h._value=v,void ir(h);if(typeof x=="function")return void P((f=x,m=v,function(){f.apply(m,arguments)}),h)}h._state=1,h._value=v,ir(h)}catch(A){nn(h,A)}var f,m}function nn(h,v){h._state=2,h._value=v,ir(h)}function ir(h){h._state===2&&h._deferreds.length===0&&ke._immediateFn(function(){h._handled||ke._unhandledRejectionFn(h._value)});for(var v=0,x=h._deferreds.length;v<x;v++)In(h,h._deferreds[v]);h._deferreds=null}function Ur(h,v,x){this.onFulfilled=typeof h=="function"?h:null,this.onRejected=typeof v=="function"?v:null,this.promise=x}function P(h,v){var x=!1;try{h(function(f){x||(x=!0,Tt(v,f))},function(f){x||(x=!0,nn(v,f))})}catch(f){if(x)return;x=!0,nn(v,f)}}ke.prototype.catch=function(h){return this.then(null,h)},ke.prototype.then=function(h,v){var x=new this.constructor(U);return In(this,new Ur(h,v,x)),x},ke.prototype.finally=function(h){var v=this.constructor;return this.then(function(x){return v.resolve(h()).then(function(){return x})},function(x){return v.resolve(h()).then(function(){return v.reject(x)})})},ke.all=function(h){return new ke(function(v,x){if(!fe(h))return x(new TypeError("Promise.all accepts an array"));var f=Array.prototype.slice.call(h);if(f.length===0)return v([]);var m=f.length;function A(L,I){try{if(I&&(oe(I)==="object"||typeof I=="function")){var B=I.then;if(typeof B=="function")return void B.call(I,function(N){A(L,N)},x)}f[L]=I,--m==0&&v(f)}catch(N){x(N)}}for(var T=0;T<f.length;T++)A(T,f[T])})},ke.any=function(h){var v=this;return new v(function(x,f){if(!h||h.length===void 0)return f(new TypeError("Promise.any accepts an array"));var m=Array.prototype.slice.call(h);if(m.length===0)return f();for(var A=[],T=0;T<m.length;T++)try{v.resolve(m[T]).then(x).catch(function(L){A.push(L),A.length===m.length&&f(new st(A,"All promises were rejected"))})}catch(L){f(L)}})},ke.allSettled=function(h){return new this(function(v,x){if(!h||h.length===void 0)return x(new TypeError(ge(h)+" "+h+" is not iterable(cannot read property Symbol(Symbol.iterator))"));var f=Array.prototype.slice.call(h);if(f.length===0)return v([]);var m=f.length;function A(L,I){if(I&&(ge(I)==="object"||typeof I=="function")){var B=I.then;if(typeof B=="function")return void B.call(I,function(N){A(L,N)},function(N){f[L]={status:"rejected",reason:N},--m==0&&v(f)})}f[L]={status:"fulfilled",value:I},--m==0&&v(f)}for(var T=0;T<f.length;T++)A(T,f[T])})},ke.resolve=function(h){return h&&oe(h)==="object"&&h.constructor===ke?h:new ke(function(v){v(h)})},ke.reject=function(h){return new ke(function(v,x){x(h)})},ke.race=function(h){return new ke(function(v,x){if(!fe(h))return x(new TypeError("Promise.race accepts an array"));for(var f=0,m=h.length;f<m;f++)ke.resolve(h[f]).then(v,x)})},ke._immediateFn=typeof setImmediate=="function"&&function(h){setImmediate(h)}||function(h){S(h,0)},ke._unhandledRejectionFn=function(h){typeof console!="undefined"&&console&&console.warn("Possible Unhandled Promise Rejection:",h)};const Ki=ke;var rn=/mobile/i.test(window.navigator.userAgent);const ne={secondToTime:function(h){if((h=h||0)===0||h===1/0||h.toString()==="NaN")return"00:00";var v=Math.floor(h/3600),x=Math.floor((h-3600*v)/60),f=Math.floor(h-3600*v-60*x);return(v>0?[v,x,f]:[x,f]).map(function(m){return m<10?"0"+m:""+m}).join(":")},getElementViewLeft:function(h){var v=h.offsetLeft,x=h.offsetParent,f=document.body.scrollLeft+document.documentElement.scrollLeft;if(document.fullscreenElement||document.mozFullScreenElement||document.webkitFullscreenElement)for(;x!==null&&x!==h;)v+=x.offsetLeft,x=x.offsetParent;else for(;x!==null;)v+=x.offsetLeft,x=x.offsetParent;return v-f},getBoundingClientRectViewLeft:function(h){var v=window.scrollY||window.pageYOffset||document.body.scrollTop+(document.documentElement&&document.documentElement.scrollTop||0);if(h.getBoundingClientRect){if(typeof this.getBoundingClientRectViewLeft.offset!="number"){var x=document.createElement("div");x.style.cssText="position:absolute;top:0;left:0;",document.body.appendChild(x),this.getBoundingClientRectViewLeft.offset=-x.getBoundingClientRect().top-v,document.body.removeChild(x),x=null}var f=h.getBoundingClientRect(),m=this.getBoundingClientRectViewLeft.offset;return f.left+m}return this.getElementViewLeft(h)},getScrollPosition:function(){return{left:window.pageXOffset||document.documentElement.scrollLeft||document.body.scrollLeft||0,top:window.pageYOffset||document.documentElement.scrollTop||document.body.scrollTop||0}},setScrollPosition:function(h){var v=h.left,x=v===void 0?0:v,f=h.top,m=f===void 0?0:f;this.isFirefox?(document.documentElement.scrollLeft=x,document.documentElement.scrollTop=m):window.scrollTo(x,m)},isMobile:rn,isFirefox:/firefox/i.test(window.navigator.userAgent),isChrome:/chrome/i.test(window.navigator.userAgent),isSafari:/safari/i.test(window.navigator.userAgent),storage:{set:function(h,v){localStorage.setItem(h,v)},get:function(h){return localStorage.getItem(h)}},nameMap:{dragStart:rn?"touchstart":"mousedown",dragMove:rn?"touchmove":"mousemove",dragEnd:rn?"touchend":"mouseup"},color2Number:function(h){return h[0]==="#"&&(h=h.substr(1)),h.length===3&&(h="".concat(h[0]).concat(h[0]).concat(h[1]).concat(h[1]).concat(h[2]).concat(h[2])),parseInt(h,16)+0&16777215},number2Color:function(h){return"#"+("00000"+h.toString(16)).slice(-6)},number2Type:function(h){switch(h){case 0:default:return"right";case 1:return"top";case 2:return"bottom"}}};function Kr(h,v){return function(){return h.apply(v,arguments)}}function an(h){return an=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},an(h)}var Rn,on=Object.prototype.toString,Ce=Object.getPrototypeOf,Dn=(Rn=Object.create(null),function(h){var v=on.call(h);return Rn[v]||(Rn[v]=v.slice(8,-1).toLowerCase())}),nt=function(h){return h=h.toLowerCase(),function(v){return Dn(v)===h}},_n=function(h){return function(v){return an(v)===h}},Ft=Array.isArray,sn=_n("undefined"),Pn=nt("ArrayBuffer"),Gi=_n("string"),St=_n("function"),Gr=_n("number"),Bn=function(h){return h!==null&&an(h)==="object"},De=function(h){if(Dn(h)!=="object")return!1;var v=Ce(h);return!(v!==null&&v!==Object.prototype&&Object.getPrototypeOf(v)!==null||Symbol.toStringTag in h||Symbol.iterator in h)},We=nt("Date"),Hr=nt("File"),qr=nt("Blob"),Hi=nt("FileList"),Yr=nt("URLSearchParams");function Ct(h,v){var x,f,m=arguments.length>2&&arguments[2]!==void 0?arguments[2]:{},A=m.allOwnKeys,T=A!==void 0&&A;if(h!=null)if(an(h)!=="object"&&(h=[h]),Ft(h))for(x=0,f=h.length;x<f;x++)v.call(null,h[x],x,h);else{var L,I=T?Object.getOwnPropertyNames(h):Object.keys(h),B=I.length;for(x=0;x<B;x++)L=I[x],v.call(null,h[L],L,h)}}function ar(h,v){v=v.toLowerCase();for(var x,f=Object.keys(h),m=f.length;m-- >0;)if(v===(x=f[m]).toLowerCase())return x;return null}var or,Wr,mt=typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:typeof window!="undefined"?window:nr.g,On=function(h){return!sn(h)&&h!==mt},zr=(or=typeof Uint8Array!="undefined"&&Ce(Uint8Array),function(h){return or&&h instanceof or}),Mn=nt("HTMLFormElement"),ln=(Wr=Object.prototype.hasOwnProperty,function(h,v){return Wr.call(h,v)}),qi=nt("RegExp"),Vr=function(h,v){var x=Object.getOwnPropertyDescriptors(h),f={};Ct(x,function(m,A){v(m,A,h)!==!1&&(f[A]=m)}),Object.defineProperties(h,f)};const K={isArray:Ft,isArrayBuffer:Pn,isBuffer:function(h){return h!==null&&!sn(h)&&h.constructor!==null&&!sn(h.constructor)&&St(h.constructor.isBuffer)&&h.constructor.isBuffer(h)},isFormData:function(h){var v="[object FormData]";return h&&(typeof FormData=="function"&&h instanceof FormData||on.call(h)===v||St(h.toString)&&h.toString()===v)},isArrayBufferView:function(h){return typeof ArrayBuffer!="undefined"&&ArrayBuffer.isView?ArrayBuffer.isView(h):h&&h.buffer&&Pn(h.buffer)},isString:Gi,isNumber:Gr,isBoolean:function(h){return h===!0||h===!1},isObject:Bn,isPlainObject:De,isUndefined:sn,isDate:We,isFile:Hr,isBlob:qr,isRegExp:qi,isFunction:St,isStream:function(h){return Bn(h)&&St(h.pipe)},isURLSearchParams:Yr,isTypedArray:zr,isFileList:Hi,forEach:Ct,merge:function h(){for(var v=On(this)&&this||{},x=v.caseless,f={},m=function(L,I){var B=x&&ar(f,I)||I;De(f[B])&&De(L)?f[B]=h(f[B],L):De(L)?f[B]=h({},L):Ft(L)?f[B]=L.slice():f[B]=L},A=0,T=arguments.length;A<T;A++)arguments[A]&&Ct(arguments[A],m);return f},extend:function(h,v,x){var f=arguments.length>3&&arguments[3]!==void 0?arguments[3]:{},m=f.allOwnKeys;return Ct(v,function(A,T){x&&St(A)?h[T]=Kr(A,x):h[T]=A},{allOwnKeys:m}),h},trim:function(h){return h.trim?h.trim():h.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g,"")},stripBOM:function(h){return h.charCodeAt(0)===65279&&(h=h.slice(1)),h},inherits:function(h,v,x,f){h.prototype=Object.create(v.prototype,f),h.prototype.constructor=h,Object.defineProperty(h,"super",{value:v.prototype}),x&&Object.assign(h.prototype,x)},toFlatObject:function(h,v,x,f){var m,A,T,L={};if(v=v||{},h==null)return v;do{for(A=(m=Object.getOwnPropertyNames(h)).length;A-- >0;)T=m[A],f&&!f(T,h,v)||L[T]||(v[T]=h[T],L[T]=!0);h=x!==!1&&Ce(h)}while(h&&(!x||x(h,v))&&h!==Object.prototype);return v},kindOf:Dn,kindOfTest:nt,endsWith:function(h,v,x){h=String(h),(x===void 0||x>h.length)&&(x=h.length),x-=v.length;var f=h.indexOf(v,x);return f!==-1&&f===x},toArray:function(h){if(!h)return null;if(Ft(h))return h;var v=h.length;if(!Gr(v))return null;for(var x=new Array(v);v-- >0;)x[v]=h[v];return x},forEachEntry:function(h,v){for(var x,f=(h&&h[Symbol.iterator]).call(h);(x=f.next())&&!x.done;){var m=x.value;v.call(h,m[0],m[1])}},matchAll:function(h,v){for(var x,f=[];(x=h.exec(v))!==null;)f.push(x);return f},isHTMLForm:Mn,hasOwnProperty:ln,hasOwnProp:ln,reduceDescriptors:Vr,freezeMethods:function(h){Vr(h,function(v,x){if(St(h)&&["arguments","caller","callee"].indexOf(x)!==-1)return!1;var f=h[x];St(f)&&(v.enumerable=!1,"writable"in v?v.writable=!1:v.set||(v.set=function(){throw Error("Can not rewrite read-only method '"+x+"'")}))})},toObjectSet:function(h,v){var x={},f=function(m){m.forEach(function(A){x[A]=!0})};return Ft(h)?f(h):f(String(h).split(v)),x},toCamelCase:function(h){return h.toLowerCase().replace(/[_-\s]([a-z\d])(\w*)/g,function(v,x,f){return x.toUpperCase()+f})},noop:function(){},toFiniteNumber:function(h,v){return h=+h,Number.isFinite(h)?h:v},findKey:ar,global:mt,isContextDefined:On,toJSONObject:function(h){var v=new Array(10);return function x(f,m){if(Bn(f)){if(v.indexOf(f)>=0)return;if(!("toJSON"in f)){v[m]=f;var A=Ft(f)?[]:{};return Ct(f,function(T,L){var I=x(T,m+1);!sn(I)&&(A[L]=I)}),v[m]=void 0,A}}return f}(h,0)}};function Nt(h,v,x,f,m){Error.call(this),Error.captureStackTrace?Error.captureStackTrace(this,this.constructor):this.stack=new Error().stack,this.message=h,this.name="AxiosError",v&&(this.code=v),x&&(this.config=x),f&&(this.request=f),m&&(this.response=m)}K.inherits(Nt,Error,{toJSON:function(){return{message:this.message,name:this.name,description:this.description,number:this.number,fileName:this.fileName,lineNumber:this.lineNumber,columnNumber:this.columnNumber,stack:this.stack,config:K.toJSONObject(this.config),code:this.code,status:this.response&&this.response.status?this.response.status:null}}});var sr=Nt.prototype,jr={};["ERR_BAD_OPTION_VALUE","ERR_BAD_OPTION","ECONNABORTED","ETIMEDOUT","ERR_NETWORK","ERR_FR_TOO_MANY_REDIRECTS","ERR_DEPRECATED","ERR_BAD_RESPONSE","ERR_BAD_REQUEST","ERR_CANCELED","ERR_NOT_SUPPORT","ERR_INVALID_URL"].forEach(function(h){jr[h]={value:h}}),Object.defineProperties(Nt,jr),Object.defineProperty(sr,"isAxiosError",{value:!0}),Nt.from=function(h,v,x,f,m,A){var T=Object.create(sr);return K.toFlatObject(h,T,function(L){return L!==Error.prototype},function(L){return L!=="isAxiosError"}),Nt.call(T,h.message,v,x,f,m),T.cause=h,T.name=h.name,A&&Object.assign(T,A),T};const Le=Nt,Yi=Q(937);function lr(h){return lr=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},lr(h)}function dr(h){return K.isPlainObject(h)||K.isArray(h)}function rt(h){return K.endsWith(h,"[]")?h.slice(0,-2):h}function Fn(h,v,x){return h?h.concat(v).map(function(f,m){return f=rt(f),!x&&m?"["+f+"]":f}).join(x?".":""):v}var Wi=K.toFlatObject(K,{},null,function(h){return/^is[A-Z]/.test(h)});const Qe=function(h,v,x){if(!K.isObject(h))throw new TypeError("target must be an object");v=v||new(Yi||FormData);var f,m=(x=K.toFlatObject(x,{metaTokens:!0,dots:!1,indexes:!1},!1,function(re,$){return!K.isUndefined($[re])})).metaTokens,A=x.visitor||N,T=x.dots,L=x.indexes,I=(x.Blob||typeof Blob!="undefined"&&Blob)&&(f=v)&&K.isFunction(f.append)&&f[Symbol.toStringTag]==="FormData"&&f[Symbol.iterator];if(!K.isFunction(A))throw new TypeError("visitor must be a function");function B(re){if(re===null)return"";if(K.isDate(re))return re.toISOString();if(!I&&K.isBlob(re))throw new Le("Blob is not supported. Use a Buffer instead.");return K.isArrayBuffer(re)||K.isTypedArray(re)?I&&typeof Blob=="function"?new Blob([re]):rr.from(re):re}function N(re,$,W){var ae=re;if(re&&!W&&lr(re)==="object"){if(K.endsWith($,"{}"))$=m?$:$.slice(0,-2),re=JSON.stringify(re);else if(K.isArray(re)&&function(pe){return K.isArray(pe)&&!pe.some(dr)}(re)||K.isFileList(re)||K.endsWith($,"[]")&&(ae=K.toArray(re)))return $=rt($),ae.forEach(function(pe,Ue){!K.isUndefined(pe)&&pe!==null&&v.append(L===!0?Fn([$],Ue,T):L===null?$:$+"[]",B(pe))}),!1}return!!dr(re)||(v.append(Fn(W,$,T),B(re)),!1)}var X=[],J=Object.assign(Wi,{defaultVisitor:N,convertValue:B,isVisitable:dr});if(!K.isObject(h))throw new TypeError("data must be an object");return function re($,W){if(!K.isUndefined($)){if(X.indexOf($)!==-1)throw Error("Circular reference detected in "+W.join("."));X.push($),K.forEach($,function(ae,pe){(!(K.isUndefined(ae)||ae===null)&&A.call(v,ae,K.isString(pe)?pe.trim():pe,W,J))===!0&&re(ae,W?W.concat(pe):[pe])}),X.pop()}}(h),v};function dn(h){var v={"!":"%21","'":"%27","(":"%28",")":"%29","~":"%7E","%20":"+","%00":"\0"};return encodeURIComponent(h).replace(/[!'()~]|%20|%00/g,function(x){return v[x]})}function Qr(h,v){this._pairs=[],h&&Qe(h,this,v)}var ur=Qr.prototype;ur.append=function(h,v){this._pairs.push([h,v])},ur.toString=function(h){var v=h?function(x){return h.call(this,x,dn)}:dn;return this._pairs.map(function(x){return v(x[0])+"="+v(x[1])},"").join("&")};const Ge=Qr;function Xr(h){return encodeURIComponent(h).replace(/%3A/gi,":").replace(/%24/g,"$").replace(/%2C/gi,",").replace(/%20/g,"+").replace(/%5B/gi,"[").replace(/%5D/gi,"]")}function me(h,v,x){if(!v)return h;var f,m=x&&x.encode||Xr,A=x&&x.serialize;if(f=A?A(v,x):K.isURLSearchParams(v)?v.toString():new Ge(v,x).toString(m)){var T=h.indexOf("#");T!==-1&&(h=h.slice(0,T)),h+=(h.indexOf("?")===-1?"?":"&")+f}return h}function Ut(h){return Ut=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},Ut(h)}function cr(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(Ut(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(Ut(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),Ut(m)==="symbol"?m:String(m)),f)}var m}var Te=function(){function h(){(function(f,m){if(!(f instanceof m))throw new TypeError("Cannot call a class as a function")})(this,h),this.handlers=[]}var v,x;return v=h,x=[{key:"use",value:function(f,m,A){return this.handlers.push({fulfilled:f,rejected:m,synchronous:!!A&&A.synchronous,runWhen:A?A.runWhen:null}),this.handlers.length-1}},{key:"eject",value:function(f){this.handlers[f]&&(this.handlers[f]=null)}},{key:"clear",value:function(){this.handlers&&(this.handlers=[])}},{key:"forEach",value:function(f){K.forEach(this.handlers,function(m){m!==null&&f(m)})}}],x&&cr(v.prototype,x),Object.defineProperty(v,"prototype",{writable:!1}),h}();const Zr=Te,fr={silentJSONParsing:!0,forcedJSONParsing:!0,clarifyTimeoutError:!1},zi=typeof URLSearchParams!="undefined"?URLSearchParams:Ge,Jr=FormData;var hr,Vi=(typeof navigator=="undefined"||(hr=navigator.product)!=="ReactNative"&&hr!=="NativeScript"&&hr!=="NS")&&typeof window!="undefined"&&typeof document!="undefined",ji=typeof WorkerGlobalScope!="undefined"&&self instanceof WorkerGlobalScope&&typeof self.importScripts=="function";const lt={isBrowser:!0,classes:{URLSearchParams:zi,FormData:Jr,Blob},isStandardBrowserEnv:Vi,isStandardBrowserWebWorkerEnv:ji,protocols:["http","https","file","blob","url","data"]},$r=function(h){function v(f,m,A,T){var L=f[T++],I=Number.isFinite(+L),B=T>=f.length;return L=!L&&K.isArray(A)?A.length:L,B?(K.hasOwnProp(A,L)?A[L]=[A[L],m]:A[L]=m,!I):(A[L]&&K.isObject(A[L])||(A[L]=[]),v(f,m,A[L],T)&&K.isArray(A[L])&&(A[L]=function(N){var X,J,re={},$=Object.keys(N),W=$.length;for(X=0;X<W;X++)re[J=$[X]]=N[J];return re}(A[L])),!I)}if(K.isFormData(h)&&K.isFunction(h.entries)){var x={};return K.forEachEntry(h,function(f,m){v(function(A){return K.matchAll(/\w+|\[(\w*)]/g,A).map(function(T){return T[0]==="[]"?"":T[1]||T[0]})}(f),m,x,0)}),x}return null};var kt={"Content-Type":void 0},un={transitional:fr,adapter:["xhr","http"],transformRequest:[function(h,v){var x,f=v.getContentType()||"",m=f.indexOf("application/json")>-1,A=K.isObject(h);if(A&&K.isHTMLForm(h)&&(h=new FormData(h)),K.isFormData(h))return m&&m?JSON.stringify($r(h)):h;if(K.isArrayBuffer(h)||K.isBuffer(h)||K.isStream(h)||K.isFile(h)||K.isBlob(h))return h;if(K.isArrayBufferView(h))return h.buffer;if(K.isURLSearchParams(h))return v.setContentType("application/x-www-form-urlencoded;charset=utf-8",!1),h.toString();if(A){if(f.indexOf("application/x-www-form-urlencoded")>-1)return function(L,I){return Qe(L,new lt.classes.URLSearchParams,Object.assign({visitor:function(B,N,X,J){return lt.isNode&&K.isBuffer(B)?(this.append(N,B.toString("base64")),!1):J.defaultVisitor.apply(this,arguments)}},I))}(h,this.formSerializer).toString();if((x=K.isFileList(h))||f.indexOf("multipart/form-data")>-1){var T=this.env&&this.env.FormData;return Qe(x?{"files[]":h}:h,T&&new T,this.formSerializer)}}return A||m?(v.setContentType("application/json",!1),function(L,I,B){if(K.isString(L))try{return(0,JSON.parse)(L),K.trim(L)}catch(N){if(N.name!=="SyntaxError")throw N}return(0,JSON.stringify)(L)}(h)):h}],transformResponse:[function(h){var v=this.transitional||un.transitional,x=v&&v.forcedJSONParsing,f=this.responseType==="json";if(h&&K.isString(h)&&(x&&!this.responseType||f)){var m=!(v&&v.silentJSONParsing)&&f;try{return JSON.parse(h)}catch(A){if(m)throw A.name==="SyntaxError"?Le.from(A,Le.ERR_BAD_RESPONSE,this,null,this.response):A}}return h}],timeout:0,xsrfCookieName:"XSRF-TOKEN",xsrfHeaderName:"X-XSRF-TOKEN",maxContentLength:-1,maxBodyLength:-1,env:{FormData:lt.classes.FormData,Blob:lt.classes.Blob},validateStatus:function(h){return h>=200&&h<300},headers:{common:{Accept:"application/json, text/plain, */*"}}};K.forEach(["delete","get","head"],function(h){un.headers[h]={}}),K.forEach(["post","put","patch"],function(h){un.headers[h]=K.merge(kt)});const pr=un;var Qi=K.toObjectSet(["age","authorization","content-length","content-type","etag","expires","from","host","if-modified-since","if-unmodified-since","last-modified","location","max-forwards","proxy-authorization","referer","retry-after","user-agent"]);function Kt(h){return Kt=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},Kt(h)}function vr(h,v){(v==null||v>h.length)&&(v=h.length);for(var x=0,f=new Array(v);x<v;x++)f[x]=h[x];return f}function ei(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(Kt(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(Kt(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),Kt(m)==="symbol"?m:String(m)),f)}var m}var ti=Symbol("internals");function cn(h){return h&&String(h).trim().toLowerCase()}function Nn(h){return h===!1||h==null?h:K.isArray(h)?h.map(Nn):String(h)}function fn(h,v,x,f){return K.isFunction(f)?f.call(this,v,x):K.isString(v)?K.isString(f)?v.indexOf(f)!==-1:K.isRegExp(f)?f.test(v):void 0:void 0}var Gt=function(h,v){function x(T){(function(L,I){if(!(L instanceof I))throw new TypeError("Cannot call a class as a function")})(this,x),T&&this.set(T)}var f,m,A;return f=x,m=[{key:"set",value:function(T,L,I){var B=this;function N(pe,Ue,Me){var Ie=cn(Ue);if(!Ie)throw new Error("header name must be a non-empty string");var Et=K.findKey(B,Ie);(!Et||B[Et]===void 0||Me===!0||Me===void 0&&B[Et]!==!1)&&(B[Et||Ue]=Nn(pe))}var X,J,re,$,W,ae=function(pe,Ue){return K.forEach(pe,function(Me,Ie){return N(Me,Ie,Ue)})};return K.isPlainObject(T)||T instanceof this.constructor?ae(T,L):K.isString(T)&&(T=T.trim())&&!/^[-_a-zA-Z]+$/.test(T.trim())?ae((W={},(X=T)&&X.split(`
`).forEach(function(pe){$=pe.indexOf(":"),J=pe.substring(0,$).trim().toLowerCase(),re=pe.substring($+1).trim(),!J||W[J]&&Qi[J]||(J==="set-cookie"?W[J]?W[J].push(re):W[J]=[re]:W[J]=W[J]?W[J]+", "+re:re)}),W),L):T!=null&&N(L,T,I),this}},{key:"get",value:function(T,L){if(T=cn(T)){var I=K.findKey(this,T);if(I){var B=this[I];if(!L)return B;if(L===!0)return function(N){for(var X,J=Object.create(null),re=/([^\s,;=]+)\s*(?:=\s*([^,;]+))?/g;X=re.exec(N);)J[X[1]]=X[2];return J}(B);if(K.isFunction(L))return L.call(this,B,I);if(K.isRegExp(L))return L.exec(B);throw new TypeError("parser must be boolean|regexp|function")}}}},{key:"has",value:function(T,L){if(T=cn(T)){var I=K.findKey(this,T);return!(!I||L&&!fn(0,this[I],I,L))}return!1}},{key:"delete",value:function(T,L){var I=this,B=!1;function N(X){if(X=cn(X)){var J=K.findKey(I,X);!J||L&&!fn(0,I[J],J,L)||(delete I[J],B=!0)}}return K.isArray(T)?T.forEach(N):N(T),B}},{key:"clear",value:function(){return Object.keys(this).forEach(this.delete.bind(this))}},{key:"normalize",value:function(T){var L=this,I={};return K.forEach(this,function(B,N){var X=K.findKey(I,N);if(X)return L[X]=Nn(B),void delete L[N];var J=T?function(re){return re.trim().toLowerCase().replace(/([a-z\d])(\w*)/g,function($,W,ae){return W.toUpperCase()+ae})}(N):String(N).trim();J!==N&&delete L[N],L[J]=Nn(B),I[J]=!0}),this}},{key:"concat",value:function(){for(var T,L=arguments.length,I=new Array(L),B=0;B<L;B++)I[B]=arguments[B];return(T=this.constructor).concat.apply(T,[this].concat(I))}},{key:"toJSON",value:function(T){var L=Object.create(null);return K.forEach(this,function(I,B){I!=null&&I!==!1&&(L[B]=T&&K.isArray(I)?I.join(", "):I)}),L}},{key:Symbol.iterator,value:function(){return Object.entries(this.toJSON())[Symbol.iterator]()}},{key:"toString",value:function(){return Object.entries(this.toJSON()).map(function(T){var L,I,B=(I=2,function(N){if(Array.isArray(N))return N}(L=T)||function(N,X){var J=N==null?null:typeof Symbol!="undefined"&&N[Symbol.iterator]||N["@@iterator"];if(J!=null){var re,$,W,ae,pe=[],Ue=!0,Me=!1;try{if(W=(J=J.call(N)).next,X===0){if(Object(J)!==J)return;Ue=!1}else for(;!(Ue=(re=W.call(J)).done)&&(pe.push(re.value),pe.length!==X);Ue=!0);}catch(Ie){Me=!0,$=Ie}finally{try{if(!Ue&&J.return!=null&&(ae=J.return(),Object(ae)!==ae))return}finally{if(Me)throw $}}return pe}}(L,I)||function(N,X){if(N){if(typeof N=="string")return vr(N,X);var J=Object.prototype.toString.call(N).slice(8,-1);return J==="Object"&&N.constructor&&(J=N.constructor.name),J==="Map"||J==="Set"?Array.from(N):J==="Arguments"||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(J)?vr(N,X):void 0}}(L,I)||function(){throw new TypeError(`Invalid attempt to destructure non-iterable instance.
In order to be iterable, non-array objects must have a [Symbol.iterator]() method.`)}());return B[0]+": "+B[1]}).join(`
`)}},{key:Symbol.toStringTag,get:function(){return"AxiosHeaders"}}],A=[{key:"from",value:function(T){return T instanceof this?T:new this(T)}},{key:"concat",value:function(T){for(var L=new this(T),I=arguments.length,B=new Array(I>1?I-1:0),N=1;N<I;N++)B[N-1]=arguments[N];return B.forEach(function(X){return L.set(X)}),L}},{key:"accessor",value:function(T){var L=(this[ti]=this[ti]={accessors:{}}).accessors,I=this.prototype;function B(N){var X=cn(N);L[X]||(function(J,re){var $=K.toCamelCase(" "+re);["get","set","has"].forEach(function(W){Object.defineProperty(J,W+$,{value:function(ae,pe,Ue){return this[W].call(this,re,ae,pe,Ue)},configurable:!0})})}(I,N),L[X]=!0)}return K.isArray(T)?T.forEach(B):B(T),this}}],m&&ei(f.prototype,m),A&&ei(f,A),Object.defineProperty(f,"prototype",{writable:!1}),x}();Gt.accessor(["Content-Type","Content-Length","Accept","Accept-Encoding","User-Agent"]),K.freezeMethods(Gt.prototype),K.freezeMethods(Gt);const ct=Gt;function Un(h,v){var x=this||pr,f=v||x,m=ct.from(f.headers),A=f.data;return K.forEach(h,function(T){A=T.call(x,A,m.normalize(),v?v.status:void 0)}),m.normalize(),A}function mr(h){return!(!h||!h.__CANCEL__)}function qe(h,v,x){Le.call(this,h==null?"canceled":h,Le.ERR_CANCELED,v,x),this.name="CanceledError"}K.inherits(qe,Le,{__CANCEL__:!0});const Ht=qe,ni=lt.isStandardBrowserEnv?{write:function(h,v,x,f,m,A){var T=[];T.push(h+"="+encodeURIComponent(v)),K.isNumber(x)&&T.push("expires="+new Date(x).toGMTString()),K.isString(f)&&T.push("path="+f),K.isString(m)&&T.push("domain="+m),A===!0&&T.push("secure"),document.cookie=T.join("; ")},read:function(h){var v=document.cookie.match(new RegExp("(^|;\\s*)("+h+")=([^;]*)"));return v?decodeURIComponent(v[3]):null},remove:function(h){this.write(h,"",Date.now()-864e5)}}:{write:function(){},read:function(){return null},remove:function(){}};function ri(h,v){return h&&!/^([a-z][a-z\d+\-.]*:)?\/\//i.test(v)?function(x,f){return f?x.replace(/\/+$/,"")+"/"+f.replace(/^\/+/,""):x}(h,v):v}const Xi=lt.isStandardBrowserEnv?function(){var h,v=/(msie|trident)/i.test(navigator.userAgent),x=document.createElement("a");function f(m){var A=m;return v&&(x.setAttribute("href",A),A=x.href),x.setAttribute("href",A),{href:x.href,protocol:x.protocol?x.protocol.replace(/:$/,""):"",host:x.host,search:x.search?x.search.replace(/^\?/,""):"",hash:x.hash?x.hash.replace(/^#/,""):"",hostname:x.hostname,port:x.port,pathname:x.pathname.charAt(0)==="/"?x.pathname:"/"+x.pathname}}return h=f(window.location.href),function(m){var A=K.isString(m)?f(m):m;return A.protocol===h.protocol&&A.host===h.host}}():function(){return!0};function ii(h,v){var x=0,f=function(m,A){m=m||10;var T,L=new Array(m),I=new Array(m),B=0,N=0;return A=A!==void 0?A:1e3,function(X){var J=Date.now(),re=I[N];T||(T=J),L[B]=X,I[B]=J;for(var $=N,W=0;$!==B;)W+=L[$++],$%=m;if((B=(B+1)%m)===N&&(N=(N+1)%m),!(J-T<A)){var ae=re&&J-re;return ae?Math.round(1e3*W/ae):void 0}}}(50,250);return function(m){var A=m.loaded,T=m.lengthComputable?m.total:void 0,L=A-x,I=f(L);x=A;var B={loaded:A,total:T,progress:T?A/T:void 0,bytes:L,rate:I||void 0,estimated:I&&T&&A<=T?(T-A)/I:void 0,event:m};B[v?"download":"upload"]=!0,h(B)}}const gr=typeof XMLHttpRequest!="undefined"&&function(h){return new Promise(function(v,x){var f,m=h.data,A=ct.from(h.headers).normalize(),T=h.responseType;function L(){h.cancelToken&&h.cancelToken.unsubscribe(f),h.signal&&h.signal.removeEventListener("abort",f)}K.isFormData(m)&&(lt.isStandardBrowserEnv||lt.isStandardBrowserWebWorkerEnv)&&A.setContentType(!1);var I=new XMLHttpRequest;if(h.auth){var B=h.auth.username||"",N=h.auth.password?unescape(encodeURIComponent(h.auth.password)):"";A.set("Authorization","Basic "+btoa(B+":"+N))}var X=ri(h.baseURL,h.url);function J(){if(I){var ae=ct.from("getAllResponseHeaders"in I&&I.getAllResponseHeaders());(function(pe,Ue,Me){var Ie=Me.config.validateStatus;Me.status&&Ie&&!Ie(Me.status)?Ue(new Le("Request failed with status code "+Me.status,[Le.ERR_BAD_REQUEST,Le.ERR_BAD_RESPONSE][Math.floor(Me.status/100)-4],Me.config,Me.request,Me)):pe(Me)})(function(pe){v(pe),L()},function(pe){x(pe),L()},{data:T&&T!=="text"&&T!=="json"?I.response:I.responseText,status:I.status,statusText:I.statusText,headers:ae,config:h,request:I}),I=null}}if(I.open(h.method.toUpperCase(),me(X,h.params,h.paramsSerializer),!0),I.timeout=h.timeout,"onloadend"in I?I.onloadend=J:I.onreadystatechange=function(){I&&I.readyState===4&&(I.status!==0||I.responseURL&&I.responseURL.indexOf("file:")===0)&&setTimeout(J)},I.onabort=function(){I&&(x(new Le("Request aborted",Le.ECONNABORTED,h,I)),I=null)},I.onerror=function(){x(new Le("Network Error",Le.ERR_NETWORK,h,I)),I=null},I.ontimeout=function(){var ae=h.timeout?"timeout of "+h.timeout+"ms exceeded":"timeout exceeded",pe=h.transitional||fr;h.timeoutErrorMessage&&(ae=h.timeoutErrorMessage),x(new Le(ae,pe.clarifyTimeoutError?Le.ETIMEDOUT:Le.ECONNABORTED,h,I)),I=null},lt.isStandardBrowserEnv){var re=(h.withCredentials||Xi(X))&&h.xsrfCookieName&&ni.read(h.xsrfCookieName);re&&A.set(h.xsrfHeaderName,re)}m===void 0&&A.setContentType(null),"setRequestHeader"in I&&K.forEach(A.toJSON(),function(ae,pe){I.setRequestHeader(pe,ae)}),K.isUndefined(h.withCredentials)||(I.withCredentials=!!h.withCredentials),T&&T!=="json"&&(I.responseType=h.responseType),typeof h.onDownloadProgress=="function"&&I.addEventListener("progress",ii(h.onDownloadProgress,!0)),typeof h.onUploadProgress=="function"&&I.upload&&I.upload.addEventListener("progress",ii(h.onUploadProgress)),(h.cancelToken||h.signal)&&(f=function(ae){I&&(x(!ae||ae.type?new Ht(null,h,I):ae),I.abort(),I=null)},h.cancelToken&&h.cancelToken.subscribe(f),h.signal&&(h.signal.aborted?f():h.signal.addEventListener("abort",f)));var $,W=($=/^([-+\w]{1,25})(:?\/\/|:)/.exec(X))&&$[1]||"";W&&lt.protocols.indexOf(W)===-1?x(new Le("Unsupported protocol "+W+":",Le.ERR_BAD_REQUEST,h)):I.send(m||null)})};var Kn={http:null,xhr:gr};K.forEach(Kn,function(h,v){if(h){try{Object.defineProperty(h,"name",{value:v})}catch(x){}Object.defineProperty(h,"adapterName",{value:v})}});function Gn(h){if(h.cancelToken&&h.cancelToken.throwIfRequested(),h.signal&&h.signal.aborted)throw new Ht(null,h)}function ai(h){return Gn(h),h.headers=ct.from(h.headers),h.data=Un.call(h,h.transformRequest),["post","put","patch"].indexOf(h.method)!==-1&&h.headers.setContentType("application/x-www-form-urlencoded",!1),function(v){for(var x,f,m=(v=K.isArray(v)?v:[v]).length,A=0;A<m&&(x=v[A],!(f=K.isString(x)?Kn[x.toLowerCase()]:x));A++);if(!f)throw f===!1?new Le("Adapter ".concat(x," is not supported by the environment"),"ERR_NOT_SUPPORT"):new Error(K.hasOwnProp(Kn,x)?"Adapter '".concat(x,"' is not available in the build"):"Unknown adapter '".concat(x,"'"));if(!K.isFunction(f))throw new TypeError("adapter is not a function");return f}(h.adapter||pr.adapter)(h).then(function(v){return Gn(h),v.data=Un.call(h,h.transformResponse,v),v.headers=ct.from(v.headers),v},function(v){return mr(v)||(Gn(h),v&&v.response&&(v.response.data=Un.call(h,h.transformResponse,v.response),v.response.headers=ct.from(v.response.headers))),Promise.reject(v)})}var yr=function(h){return h instanceof ct?h.toJSON():h};function qt(h,v){v=v||{};var x={};function f(B,N,X){return K.isPlainObject(B)&&K.isPlainObject(N)?K.merge.call({caseless:X},B,N):K.isPlainObject(N)?K.merge({},N):K.isArray(N)?N.slice():N}function m(B,N,X){return K.isUndefined(N)?K.isUndefined(B)?void 0:f(void 0,B,X):f(B,N,X)}function A(B,N){if(!K.isUndefined(N))return f(void 0,N)}function T(B,N){return K.isUndefined(N)?K.isUndefined(B)?void 0:f(void 0,B):f(void 0,N)}function L(B,N,X){return X in v?f(B,N):X in h?f(void 0,B):void 0}var I={url:A,method:A,data:A,baseURL:T,transformRequest:T,transformResponse:T,paramsSerializer:T,timeout:T,timeoutMessage:T,withCredentials:T,adapter:T,responseType:T,xsrfCookieName:T,xsrfHeaderName:T,onUploadProgress:T,onDownloadProgress:T,decompress:T,maxContentLength:T,maxBodyLength:T,beforeRedirect:T,transport:T,httpAgent:T,httpsAgent:T,cancelToken:T,socketPath:T,responseEncoding:T,validateStatus:L,headers:function(B,N){return m(yr(B),yr(N),!0)}};return K.forEach(Object.keys(h).concat(Object.keys(v)),function(B){var N=I[B]||m,X=N(h[B],v[B],B);K.isUndefined(X)&&N!==L||(x[B]=X)}),x}function gt(h){return gt=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},gt(h)}var Hn={};["object","boolean","number","function","string","symbol"].forEach(function(h,v){Hn[h]=function(x){return gt(x)===h||"a"+(v<1?"n ":" ")+h}});var Ar={};Hn.transitional=function(h,v,x){function f(m,A){return"[Axios v1.2.3] Transitional option '"+m+"'"+A+(x?". "+x:"")}return function(m,A,T){if(h===!1)throw new Le(f(A," has been removed"+(v?" in "+v:"")),Le.ERR_DEPRECATED);return v&&!Ar[A]&&(Ar[A]=!0,console.warn(f(A," has been deprecated since v"+v+" and will be removed in the near future"))),!h||h(m,A,T)}};const br={assertOptions:function(h,v,x){if(gt(h)!=="object")throw new Le("options must be an object",Le.ERR_BAD_OPTION_VALUE);for(var f=Object.keys(h),m=f.length;m-- >0;){var A=f[m],T=v[A];if(T){var L=h[A],I=L===void 0||T(L,A,h);if(I!==!0)throw new Le("option "+A+" must be "+I,Le.ERR_BAD_OPTION_VALUE)}else if(x!==!0)throw new Le("Unknown option "+A,Le.ERR_BAD_OPTION)}},validators:Hn};function Yt(h){return Yt=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},Yt(h)}function Zi(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(Yt(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(Yt(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),Yt(m)==="symbol"?m:String(m)),f)}var m}var ft=br.validators,hn=function(){function h(f){(function(m,A){if(!(m instanceof A))throw new TypeError("Cannot call a class as a function")})(this,h),this.defaults=f,this.interceptors={request:new Zr,response:new Zr}}var v,x;return v=h,(x=[{key:"request",value:function(f,m){typeof f=="string"?(m=m||{}).url=f:m=f||{};var A,T=m=qt(this.defaults,m),L=T.transitional,I=T.paramsSerializer,B=T.headers;L!==void 0&&br.assertOptions(L,{silentJSONParsing:ft.transitional(ft.boolean),forcedJSONParsing:ft.transitional(ft.boolean),clarifyTimeoutError:ft.transitional(ft.boolean)},!1),I!==void 0&&br.assertOptions(I,{encode:ft.function,serialize:ft.function},!0),m.method=(m.method||this.defaults.method||"get").toLowerCase(),(A=B&&K.merge(B.common,B[m.method]))&&K.forEach(["delete","get","head","post","put","patch","common"],function(Ie){delete B[Ie]}),m.headers=ct.concat(A,B);var N=[],X=!0;this.interceptors.request.forEach(function(Ie){typeof Ie.runWhen=="function"&&Ie.runWhen(m)===!1||(X=X&&Ie.synchronous,N.unshift(Ie.fulfilled,Ie.rejected))});var J,re=[];this.interceptors.response.forEach(function(Ie){re.push(Ie.fulfilled,Ie.rejected)});var $,W=0;if(!X){var ae=[ai.bind(this),void 0];for(ae.unshift.apply(ae,N),ae.push.apply(ae,re),$=ae.length,J=Promise.resolve(m);W<$;)J=J.then(ae[W++],ae[W++]);return J}$=N.length;var pe=m;for(W=0;W<$;){var Ue=N[W++],Me=N[W++];try{pe=Ue(pe)}catch(Ie){Me.call(this,Ie);break}}try{J=ai.call(this,pe)}catch(Ie){return Promise.reject(Ie)}for(W=0,$=re.length;W<$;)J=J.then(re[W++],re[W++]);return J}},{key:"getUri",value:function(f){return me(ri((f=qt(this.defaults,f)).baseURL,f.url),f.params,f.paramsSerializer)}}])&&Zi(v.prototype,x),Object.defineProperty(v,"prototype",{writable:!1}),h}();K.forEach(["delete","get","head","options"],function(h){hn.prototype[h]=function(v,x){return this.request(qt(x||{},{method:h,url:v,data:(x||{}).data}))}}),K.forEach(["post","put","patch"],function(h){function v(x){return function(f,m,A){return this.request(qt(A||{},{method:h,headers:x?{"Content-Type":"multipart/form-data"}:{},url:f,data:m}))}}hn.prototype[h]=v(),hn.prototype[h+"Form"]=v(!0)});const pn=hn;function be(h){return be=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},be(h)}function he(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(be(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(be(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),be(m)==="symbol"?m:String(m)),f)}var m}var oi=function(){function h(m){if(function(L,I){if(!(L instanceof I))throw new TypeError("Cannot call a class as a function")}(this,h),typeof m!="function")throw new TypeError("executor must be a function.");var A;this.promise=new Promise(function(L){A=L});var T=this;this.promise.then(function(L){if(T._listeners){for(var I=T._listeners.length;I-- >0;)T._listeners[I](L);T._listeners=null}}),this.promise.then=function(L){var I,B=new Promise(function(N){T.subscribe(N),I=N}).then(L);return B.cancel=function(){T.unsubscribe(I)},B},m(function(L,I,B){T.reason||(T.reason=new Ht(L,I,B),A(T.reason))})}var v,x,f;return v=h,x=[{key:"throwIfRequested",value:function(){if(this.reason)throw this.reason}},{key:"subscribe",value:function(m){this.reason?m(this.reason):this._listeners?this._listeners.push(m):this._listeners=[m]}},{key:"unsubscribe",value:function(m){if(this._listeners){var A=this._listeners.indexOf(m);A!==-1&&this._listeners.splice(A,1)}}}],f=[{key:"source",value:function(){var m;return{token:new h(function(A){m=A}),cancel:m}}}],x&&he(v.prototype,x),f&&he(v,f),Object.defineProperty(v,"prototype",{writable:!1}),h}();const Er=oi;function si(h,v){(v==null||v>h.length)&&(v=h.length);for(var x=0,f=new Array(v);x<v;x++)f[x]=h[x];return f}var qn={Continue:100,SwitchingProtocols:101,Processing:102,EarlyHints:103,Ok:200,Created:201,Accepted:202,NonAuthoritativeInformation:203,NoContent:204,ResetContent:205,PartialContent:206,MultiStatus:207,AlreadyReported:208,ImUsed:226,MultipleChoices:300,MovedPermanently:301,Found:302,SeeOther:303,NotModified:304,UseProxy:305,Unused:306,TemporaryRedirect:307,PermanentRedirect:308,BadRequest:400,Unauthorized:401,PaymentRequired:402,Forbidden:403,NotFound:404,MethodNotAllowed:405,NotAcceptable:406,ProxyAuthenticationRequired:407,RequestTimeout:408,Conflict:409,Gone:410,LengthRequired:411,PreconditionFailed:412,PayloadTooLarge:413,UriTooLong:414,UnsupportedMediaType:415,RangeNotSatisfiable:416,ExpectationFailed:417,ImATeapot:418,MisdirectedRequest:421,UnprocessableEntity:422,Locked:423,FailedDependency:424,TooEarly:425,UpgradeRequired:426,PreconditionRequired:428,TooManyRequests:429,RequestHeaderFieldsTooLarge:431,UnavailableForLegalReasons:451,InternalServerError:500,NotImplemented:501,BadGateway:502,ServiceUnavailable:503,GatewayTimeout:504,HttpVersionNotSupported:505,VariantAlsoNegotiates:506,InsufficientStorage:507,LoopDetected:508,NotExtended:510,NetworkAuthenticationRequired:511};Object.entries(qn).forEach(function(h){var v,x,f=(x=2,function(T){if(Array.isArray(T))return T}(v=h)||function(T,L){var I=T==null?null:typeof Symbol!="undefined"&&T[Symbol.iterator]||T["@@iterator"];if(I!=null){var B,N,X,J,re=[],$=!0,W=!1;try{if(X=(I=I.call(T)).next,L===0){if(Object(I)!==I)return;$=!1}else for(;!($=(B=X.call(I)).done)&&(re.push(B.value),re.length!==L);$=!0);}catch(ae){W=!0,N=ae}finally{try{if(!$&&I.return!=null&&(J=I.return(),Object(J)!==J))return}finally{if(W)throw N}}return re}}(v,x)||function(T,L){if(T){if(typeof T=="string")return si(T,L);var I=Object.prototype.toString.call(T).slice(8,-1);return I==="Object"&&T.constructor&&(I=T.constructor.name),I==="Map"||I==="Set"?Array.from(T):I==="Arguments"||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(I)?si(T,L):void 0}}(v,x)||function(){throw new TypeError(`Invalid attempt to destructure non-iterable instance.
In order to be iterable, non-array objects must have a [Symbol.iterator]() method.`)}()),m=f[0],A=f[1];qn[A]=m});const li=qn;var Pe=function h(v){var x=new pn(v),f=Kr(pn.prototype.request,x);return K.extend(f,pn.prototype,x,{allOwnKeys:!0}),K.extend(f,x,null,{allOwnKeys:!0}),f.create=function(m){return h(qt(v,m))},f}(pr);Pe.Axios=pn,Pe.CanceledError=Ht,Pe.CancelToken=Er,Pe.isCancel=mr,Pe.VERSION="1.2.3",Pe.toFormData=Qe,Pe.AxiosError=Le,Pe.Cancel=Pe.CanceledError,Pe.all=function(h){return Promise.all(h)},Pe.spread=function(h){return function(v){return h.apply(null,v)}},Pe.isAxiosError=function(h){return K.isObject(h)&&h.isAxiosError===!0},Pe.mergeConfig=qt,Pe.AxiosHeaders=ct,Pe.formToJSON=function(h){return $r(K.isHTMLForm(h)?new FormData(h):h)},Pe.HttpStatusCode=li,Pe.default=Pe;const Yn=Pe,Ji={send:function(h){Yn.post(h.url,h.data).then(function(v){var x=v.data;x&&x.code===0?h.success&&h.success(x):h.error&&h.error(x&&x.msg)}).catch(function(v){console.error(v),h.error&&h.error()})},read:function(h){Yn.get(h.url).then(function(v){var x=v.data;x&&x.code===0?h.success&&h.success(x.data.map(function(f){return{time:f[0],type:f[1],color:f[2],author:f[3],text:f[4]}})):h.error&&h.error(x&&x.msg)}).catch(function(v){console.error(v),h.error&&h.error()})}};function xr(h){return xr=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},xr(h)}function it(h){var v=this;this.lang=h,this.fallbackLang=this.lang.includes("-")?this.lang.split("-")[0]:this.lang,this.tran=function(x){return x=x.toLowerCase(),yt[v.lang]&&yt[v.lang][x]?yt[v.lang][x]:yt[v.fallbackLang]&&yt[v.fallbackLang][x]?yt[v.fallbackLang][x]:Tr[x]?Tr[x]:x}}var Tr={"danmaku-loading":"Danmaku is loading",top:"Top",bottom:"Bottom",rolling:"Rolling","input-danmaku-enter":"Input danmaku, hit Enter","about-author":"About author","dplayer-feedback":"DPlayer feedback","about-dplayer":"About DPlayer",loop:"Loop",speed:"Speed","opacity-danmaku":"Opacity for danmaku",normal:"Normal","please-input-danmaku":"Please input danmaku content!","set-danmaku-color":"Set danmaku color","set-danmaku-type":"Set danmaku type","show-danmaku":"Show danmaku","video-failed":"Video load failed","danmaku-failed":"Danmaku load failed","danmaku-send-failed":"Danmaku send failed","switching-quality":"Switching to %q quality","switched-quality":"Switched to %q quality",ff:"FF %s s",rew:"REW %s s","unlimited-danmaku":"Unlimited danmaku","send-danmaku":"Send danmaku",setting:"Setting",fullscreen:"Full screen","web-fullscreen":"Web full screen",send:"Send",screenshot:"Screenshot",airplay:"AirPlay",chromecast:"ChromeCast",subtitle:"Subtitle",off:"Off","show-subs":"Show subtitle","hide-subs":"Hide subtitle",volume:"Volume",live:"Live","video-info":"Video info"},yt={en:Tr,"zh-cn":{"danmaku-loading":"\u5F39\u5E55\u52A0\u8F7D\u4E2D",top:"\u9876\u90E8",bottom:"\u5E95\u90E8",rolling:"\u6EDA\u52A8","input-danmaku-enter":"\u8F93\u5165\u5F39\u5E55\uFF0C\u56DE\u8F66\u53D1\u9001","about-author":"\u5173\u4E8E\u4F5C\u8005","dplayer-feedback":"\u64AD\u653E\u5668\u610F\u89C1\u53CD\u9988","about-dplayer":"\u5173\u4E8E DPlayer \u64AD\u653E\u5668",loop:"\u6D17\u8111\u5FAA\u73AF",speed:"\u901F\u5EA6","opacity-danmaku":"\u5F39\u5E55\u900F\u660E\u5EA6",normal:"\u6B63\u5E38","please-input-danmaku":"\u8981\u8F93\u5165\u5F39\u5E55\u5185\u5BB9\u554A\u5582\uFF01","set-danmaku-color":"\u8BBE\u7F6E\u5F39\u5E55\u989C\u8272","set-danmaku-type":"\u8BBE\u7F6E\u5F39\u5E55\u7C7B\u578B","show-danmaku":"\u663E\u793A\u5F39\u5E55","video-failed":"\u89C6\u9891\u52A0\u8F7D\u5931\u8D25","danmaku-failed":"\u5F39\u5E55\u52A0\u8F7D\u5931\u8D25","danmaku-send-failed":"\u5F39\u5E55\u53D1\u9001\u5931\u8D25","switching-quality":"\u6B63\u5728\u5207\u6362\u81F3 %q \u753B\u8D28","switched-quality":"\u5DF2\u7ECF\u5207\u6362\u81F3 %q \u753B\u8D28",ff:"\u5FEB\u8FDB %s \u79D2",rew:"\u5FEB\u9000 %s \u79D2","unlimited-danmaku":"\u6D77\u91CF\u5F39\u5E55","send-danmaku":"\u53D1\u9001\u5F39\u5E55",setting:"\u8BBE\u7F6E",fullscreen:"\u5168\u5C4F","web-fullscreen":"\u9875\u9762\u5168\u5C4F",send:"\u53D1\u9001",screenshot:"\u622A\u56FE",airplay:"\u65E0\u7EBF\u6295\u5C4F",chromecast:"ChromeCast",subtitle:"\u5B57\u5E55",off:"\u5173\u95ED","show-subs":"\u663E\u793A\u5B57\u5E55","hide-subs":"\u9690\u85CF\u5B57\u5E55",volume:"\u97F3\u91CF",live:"\u76F4\u64AD","video-info":"\u89C6\u9891\u7EDF\u8BA1\u4FE1\u606F"},"zh-tw":{"danmaku-loading":"\u5F48\u5E55\u8F09\u5165\u4E2D",top:"\u9802\u90E8",bottom:"\u5E95\u90E8",rolling:"\u6EFE\u52D5","input-danmaku-enter":"\u8F38\u5165\u5F48\u5E55\uFF0CEnter \u767C\u9001","about-author":"\u95DC\u65BC\u4F5C\u8005","dplayer-feedback":"\u64AD\u653E\u5668\u610F\u898B\u56DE\u994B","about-dplayer":"\u95DC\u65BC DPlayer \u64AD\u653E\u5668",loop:"\u5FAA\u74B0\u64AD\u653E",speed:"\u901F\u5EA6","opacity-danmaku":"\u5F48\u5E55\u900F\u660E\u5EA6",normal:"\u6B63\u5E38","please-input-danmaku":"\u8ACB\u8F38\u5165\u5F48\u5E55\u5167\u5BB9\u554A\uFF01","set-danmaku-color":"\u8A2D\u5B9A\u5F48\u5E55\u984F\u8272","set-danmaku-type":"\u8A2D\u5B9A\u5F48\u5E55\u985E\u578B","show-danmaku":"\u986F\u793A\u5F48\u5E55","video-failed":"\u5F71\u7247\u8F09\u5165\u5931\u6557","danmaku-failed":"\u5F48\u5E55\u8F09\u5165\u5931\u6557","danmaku-send-failed":"\u5F48\u5E55\u767C\u9001\u5931\u6557","switching-quality":"\u6B63\u5728\u5207\u63DB\u81F3 %q \u756B\u8CEA","switched-quality":"\u5DF2\u7D93\u5207\u63DB\u81F3 %q \u756B\u8CEA",ff:"\u5FEB\u9032 %s \u79D2",rew:"\u5FEB\u9000 %s \u79D2","unlimited-danmaku":"\u5DE8\u91CF\u5F48\u5E55","send-danmaku":"\u767C\u9001\u5F48\u5E55",setting:"\u8A2D\u5B9A",fullscreen:"\u5168\u87A2\u5E55","web-fullscreen":"\u9801\u9762\u5168\u87A2\u5E55",send:"\u767C\u9001",screenshot:"\u622A\u5716",airplay:"\u7121\u7DDA\u6295\u5C4F",chromecast:"ChromeCast",subtitle:"\u5B57\u5E55",off:"\u95DC\u9589","show-subs":"\u986F\u793A\u5B57\u5E55","hide-subs":"\u96B1\u85CF\u5B57\u5E55",volume:"\u97F3\u91CF",live:"\u76F4\u64AD","video-info":"\u5F71\u7247\u7D71\u8A08\u8A0A\u606F"},"ko-kr":{"danmaku-loading":"Danmaku\uB97C \uBD88\uB7EC\uC624\uB294 \uC911\uC785\uB2C8\uB2E4.",top:"Top",bottom:"Bottom",rolling:"Rolling","input-danmaku-enter":"Danmaku\uB97C \uC785\uB825\uD558\uACE0 Enter\uB97C \uB204\uB974\uC138\uC694.","about-author":"\uB9CC\uB4E0\uC774","dplayer-feedback":"\uD53C\uB4DC\uBC31 \uBCF4\uB0B4\uAE30","about-dplayer":"DPlayer \uC815\uBCF4",loop:"\uBC18\uBCF5",speed:"\uBC30\uC18D","opacity-danmaku":"Danmaku \uBD88\uD22C\uBA85\uB3C4",normal:"\uD45C\uC900","please-input-danmaku":"Danmaku\uB97C \uC785\uB825\uD558\uC138\uC694!","set-danmaku-color":"Danmaku \uC0C9\uC0C1","set-danmaku-type":"Danmaku \uC124\uC815","show-danmaku":"Danmaku \uD45C\uC2DC","video-failed":"\uBE44\uB514\uC624\uB97C \uBD88\uB7EC\uC624\uC9C0 \uBABB\uD588\uC2B5\uB2C8\uB2E4.","danmaku-failed":"Danmaku\uB97C \uBD88\uB7EC\uC624\uC9C0 \uBABB\uD588\uC2B5\uB2C8\uB2E4.","danmaku-send-failed":"Danmaku \uC804\uC1A1\uC5D0 \uC2E4\uD328\uD588\uC2B5\uB2C8\uB2E4.","Switching to":"","Switched to":"","switching-quality":"\uC804\uD658 %q \uD654\uC9C8","switched-quality":"\uC804\uD658 \uB428 %q \uD654\uC9C8",ff:"\uC55E\uC73C\uB85C %s \uCD08",rew:"\uB4A4\uB85C %s \uCD08","unlimited-danmaku":"\uB05D\uC5C6\uB294 Danmaku","send-danmaku":"Danmaku \uBCF4\uB0B4\uAE30",setting:"\uC124\uC815",fullscreen:"\uC804\uCCB4 \uD654\uBA74","web-fullscreen":"\uC6F9 \uB0B4 \uC804\uCCB4\uD654\uBA74",send:"\uBCF4\uB0B4\uAE30",screenshot:"\uD654\uBA74 \uCEA1\uCCD0",airplay:"\uC5D0\uC5B4\uD50C\uB808\uC774",chromecast:"ChromeCast",subtitle:"\uBD80\uC81C",off:"\uB044\uB2E4","show-subs":"\uC790\uB9C9 \uBCF4\uC774\uAE30","hide-subs":"\uC790\uB9C9 \uC228\uAE30\uAE30",Volume:"\uBCFC\uB968",live:"\uC0DD\uBC29\uC1A1","video-info":"\uBE44\uB514\uC624 \uC815\uBCF4"},de:{"danmaku-loading":"Danmaku l\xE4dt...",top:"Oben",bottom:"Unten",rolling:"Rollend","input-danmaku-enter":"Dr\xFCcke Enter nach dem Einf\xFCgen vom Danmaku","about-author":"\xDCber den Autor","dplayer-feedback":"DPlayer Feedback","about-dplayer":"\xDCber DPlayer",loop:"Wiederholen",speed:"Geschwindigkeit","opacity-danmaku":"Transparenz f\xFCr Danmaku",normal:"Normal","please-input-danmaku":"Bitte Danmaku Inhalt eingeben!","set-danmaku-color":"Danmaku Farbe festlegen","set-danmaku-type":"Danmaku Typ festlegen","show-danmaku":"Zeige Danmaku","video-failed":"Das Video konnte nicht geladen werden","danmaku-failed":"Danmaku konnte nicht geladen werden","danmaku-send-failed":"Das senden von Danmaku ist fehgeschlagen","switching-quality":"Wechsle zur %q Qualit\xE4t","switched-quality":"Zur %q Qualit\xE4t gewechselt",ff:"%s s Vorw\xE4rts",rew:"%s s Zur\xFCck","unlimited-danmaku":"Unlimitiertes Danmaku","send-danmaku":"Sende Danmaku",setting:"Einstellungen",fullscreen:"Vollbild","web-fullscreen":"Browser Vollbild",send:"Senden",screenshot:"Screenshot",airplay:"AirPlay","show-subs":"Zeige Untertitel",chromecast:"ChromeCast",subtitle:"Untertitel",off:"Schlie\xDFung","hide-subs":"Verstecke Untertitel",volume:"Lautst\xE4rke",live:"Live","video-info":"Video Info"}},Wn=Q(730),Sr=Q.n(Wn),$i=Q(74),ea=Q.n($i),ta=Q(437),Cr=Q.n(ta),vn=Q(644),na=Q.n(vn),di=Q(324),mn=Q.n(di),kr=Q(574),ui=Q.n(kr),ra=Q(300),ia=Q.n(ra),aa=Q(934),oa=Q.n(aa),ci=Q(428),wr=Q.n(ci),sa=Q(807),la=Q.n(sa),fi=Q(338),hi=Q.n(fi),zn=Q(254),pi=Q.n(zn),Lr=Q(965),vi=Q.n(Lr),Vn=Q(113),da=Q.n(Vn),mi=Q(251),ua=Q.n(mi),gn=Q(410),Ir=Q.n(gn),ca=Q(182),fa=Q.n(ca),ha=Q(193),ze=Q.n(ha);const Oe={play:Sr(),pause:ea(),volumeUp:Cr(),volumeDown:na(),volumeOff:mn(),full:ui(),fullWeb:ia(),setting:oa(),right:wr(),comment:la(),commentOff:hi(),send:pi(),pallette:vi(),camera:da(),subtitle:Ir(),loading:fa(),airplay:ua(),chromecast:ze()};var pa=Q(916),va=Q.n(pa);function wt(h){return wt=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},wt(h)}function jn(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(wt(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(wt(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),wt(m)==="symbol"?m:String(m)),f)}var m}var ma=function(){function h(m){(function(A,T){if(!(A instanceof T))throw new TypeError("Cannot call a class as a function")})(this,h),this.container=m.container,this.options=m.options,this.index=m.index,this.tran=m.tran,this.init()}var v,x,f;return v=h,f=[{key:"NewNotice",value:function(m,A,T){var L=document.createElement("div");return L.classList.add("dplayer-notice"),L.style.opacity=A,L.innerText=m,T&&(L.id="dplayer-notice-".concat(T)),L}}],(x=[{key:"init",value:function(){this.container.innerHTML=va()({options:this.options,index:this.index,tran:this.tran,icons:Oe,mobile:ne.isMobile,video:{current:!0,pic:this.options.video.pic,screenshot:this.options.screenshot,airplay:!(!ne.isSafari||ne.isChrome)&&this.options.airplay,chromecast:this.options.chromecast,preload:this.options.preload,url:this.options.video.url,subtitle:this.options.subtitle}}),this.volumeBar=this.container.querySelector(".dplayer-volume-bar-inner"),this.volumeBarWrap=this.container.querySelector(".dplayer-volume-bar"),this.volumeBarWrapWrap=this.container.querySelector(".dplayer-volume-bar-wrap"),this.volumeButton=this.container.querySelector(".dplayer-volume"),this.volumeButtonIcon=this.container.querySelector(".dplayer-volume-icon"),this.volumeIcon=this.container.querySelector(".dplayer-volume-icon .dplayer-icon-content"),this.playedBar=this.container.querySelector(".dplayer-played"),this.loadedBar=this.container.querySelector(".dplayer-loaded"),this.playedBarWrap=this.container.querySelector(".dplayer-bar-wrap"),this.playedBarTime=this.container.querySelector(".dplayer-bar-time"),this.danmaku=this.container.querySelector(".dplayer-danmaku"),this.danmakuLoading=this.container.querySelector(".dplayer-danloading"),this.video=this.container.querySelector(".dplayer-video-current"),this.bezel=this.container.querySelector(".dplayer-bezel-icon"),this.playButton=this.container.querySelector(".dplayer-play-icon"),this.mobilePlayButton=this.container.querySelector(".dplayer-mobile-play"),this.videoWrap=this.container.querySelector(".dplayer-video-wrap"),this.controllerMask=this.container.querySelector(".dplayer-controller-mask"),this.ptime=this.container.querySelector(".dplayer-ptime"),this.settingButton=this.container.querySelector(".dplayer-setting-icon"),this.settingBox=this.container.querySelector(".dplayer-setting-box"),this.mask=this.container.querySelector(".dplayer-mask"),this.loop=this.container.querySelector(".dplayer-setting-loop"),this.loopToggle=this.container.querySelector(".dplayer-setting-loop .dplayer-toggle-setting-input"),this.showDanmaku=this.container.querySelector(".dplayer-setting-showdan"),this.showDanmakuToggle=this.container.querySelector(".dplayer-showdan-setting-input"),this.unlimitDanmaku=this.container.querySelector(".dplayer-setting-danunlimit"),this.unlimitDanmakuToggle=this.container.querySelector(".dplayer-danunlimit-setting-input"),this.speed=this.container.querySelector(".dplayer-setting-speed"),this.speedItem=this.container.querySelectorAll(".dplayer-setting-speed-item"),this.danmakuOpacityBar=this.container.querySelector(".dplayer-danmaku-bar-inner"),this.danmakuOpacityBarWrap=this.container.querySelector(".dplayer-danmaku-bar"),this.danmakuOpacityBarWrapWrap=this.container.querySelector(".dplayer-danmaku-bar-wrap"),this.danmakuOpacityBox=this.container.querySelector(".dplayer-setting-danmaku"),this.dtime=this.container.querySelector(".dplayer-dtime"),this.controller=this.container.querySelector(".dplayer-controller"),this.commentInput=this.container.querySelector(".dplayer-comment-input"),this.commentButton=this.container.querySelector(".dplayer-comment-icon"),this.commentSettingBox=this.container.querySelector(".dplayer-comment-setting-box"),this.commentSettingButton=this.container.querySelector(".dplayer-comment-setting-icon"),this.commentSettingFill=this.container.querySelector(".dplayer-comment-setting-icon path"),this.commentSendButton=this.container.querySelector(".dplayer-send-icon"),this.commentSendFill=this.container.querySelector(".dplayer-send-icon path"),this.commentColorSettingBox=this.container.querySelector(".dplayer-comment-setting-color"),this.browserFullButton=this.container.querySelector(".dplayer-full-icon"),this.webFullButton=this.container.querySelector(".dplayer-full-in-icon"),this.menu=this.container.querySelector(".dplayer-menu"),this.menuItem=this.container.querySelectorAll(".dplayer-menu-item"),this.qualityList=this.container.querySelector(".dplayer-quality-list"),this.camareButton=this.container.querySelector(".dplayer-camera-icon"),this.airplayButton=this.container.querySelector(".dplayer-airplay-icon"),this.chromecastButton=this.container.querySelector(".dplayer-chromecast-icon"),this.subtitleButton=this.container.querySelector(".dplayer-subtitle-icon"),this.subtitleButtonInner=this.container.querySelector(".dplayer-subtitle-icon .dplayer-icon-content"),this.subtitlesButton=this.container.querySelector(".dplayer-subtitles-icon"),this.subtitlesBox=this.container.querySelector(".dplayer-subtitles-box"),this.subtitlesItem=this.container.querySelectorAll(".dplayer-subtitles-item"),this.subtitle=this.container.querySelector(".dplayer-subtitle"),this.subtrack=this.container.querySelector(".dplayer-subtrack"),this.qualityButton=this.container.querySelector(".dplayer-quality-icon"),this.barPreview=this.container.querySelector(".dplayer-bar-preview"),this.barWrap=this.container.querySelector(".dplayer-bar-wrap"),this.noticeList=this.container.querySelector(".dplayer-notice-list"),this.infoPanel=this.container.querySelector(".dplayer-info-panel"),this.infoPanelClose=this.container.querySelector(".dplayer-info-panel-close"),this.infoVersion=this.container.querySelector(".dplayer-info-panel-item-version .dplayer-info-panel-item-data"),this.infoFPS=this.container.querySelector(".dplayer-info-panel-item-fps .dplayer-info-panel-item-data"),this.infoType=this.container.querySelector(".dplayer-info-panel-item-type .dplayer-info-panel-item-data"),this.infoUrl=this.container.querySelector(".dplayer-info-panel-item-url .dplayer-info-panel-item-data"),this.infoResolution=this.container.querySelector(".dplayer-info-panel-item-resolution .dplayer-info-panel-item-data"),this.infoDuration=this.container.querySelector(".dplayer-info-panel-item-duration .dplayer-info-panel-item-data"),this.infoDanmakuId=this.container.querySelector(".dplayer-info-panel-item-danmaku-id .dplayer-info-panel-item-data"),this.infoDanmakuApi=this.container.querySelector(".dplayer-info-panel-item-danmaku-api .dplayer-info-panel-item-data"),this.infoDanmakuAmount=this.container.querySelector(".dplayer-info-panel-item-danmaku-amount .dplayer-info-panel-item-data")}}])&&jn(v.prototype,x),f&&jn(v,f),Object.defineProperty(v,"prototype",{writable:!1}),h}();const yn=ma;function Lt(h){return Lt=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},Lt(h)}function Ye(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(Lt(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(Lt(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),Lt(m)==="symbol"?m:String(m)),f)}var m}var ga=function(){function h(f){(function(m,A){if(!(m instanceof A))throw new TypeError("Cannot call a class as a function")})(this,h),this.options=f,this.player=this.options.player,this.container=this.options.container,this.danTunnel={right:{},top:{},bottom:{}},this.danIndex=0,this.dan=[],this.showing=!0,this._opacity=this.options.opacity,this.events=this.options.events,this.unlimited=this.options.unlimited,this._measure(""),this.load()}var v,x;return v=h,x=[{key:"load",value:function(){var f,m=this;f=this.options.api.maximum?"".concat(this.options.api.address,"v3/?id=").concat(this.options.api.id,"&max=").concat(this.options.api.maximum):"".concat(this.options.api.address,"v3/?id=").concat(this.options.api.id);var A=(this.options.api.addition||[]).slice(0);A.push(f),this.events&&this.events.trigger("danmaku_load_start",A),this._readAllEndpoints(A,function(T){m.dan=[].concat.apply([],T).sort(function(L,I){return L.time-I.time}),window.requestAnimationFrame(function(){m.frame()}),m.options.callback(),m.events&&m.events.trigger("danmaku_load_end")})}},{key:"reload",value:function(f){this.options.api=f,this.dan=[],this.clear(),this.load()}},{key:"_readAllEndpoints",value:function(f,m){for(var A=this,T=[],L=0,I=function(N){A.options.apiBackend.read({url:f[N],success:function(X){T[N]=X,++L===f.length&&m(T)},error:function(X){A.options.error(X||A.options.tran("danmaku-failed")),T[N]=[],++L===f.length&&m(T)}})},B=0;B<f.length;++B)I(B)}},{key:"send",value:function(f,m){var A=this,T={token:this.options.api.token,id:this.options.api.id,author:this.options.api.user,time:this.options.time(),text:f.text,color:f.color,type:f.type};this.options.apiBackend.send({url:this.options.api.address+"v3/",data:T,success:m,error:function(I){A.options.error(I||A.options.tran("danmaku-failed"))}}),this.dan.splice(this.danIndex,0,T),this.danIndex++;var L={text:this.htmlEncode(T.text),color:T.color,type:T.type,border:"2px solid ".concat(this.options.borderColor)};this.draw(L),this.events&&this.events.trigger("danmaku_send",T)}},{key:"frame",value:function(){var f=this;if(this.dan.length&&!this.paused&&this.showing){for(var m=this.dan[this.danIndex],A=[];m&&this.options.time()>parseFloat(m.time);)A.push(m),m=this.dan[++this.danIndex];this.draw(A)}window.requestAnimationFrame(function(){f.frame()})}},{key:"opacity",value:function(f){if(f!==void 0){for(var m=this.container.getElementsByClassName("dplayer-danmaku-item"),A=0;A<m.length;A++)m[A].style.opacity=f;this._opacity=f,this.events&&this.events.trigger("danmaku_opacity",this._opacity)}return this._opacity}},{key:"draw",value:function(f){var m=this;if(this.showing){var A=this.options.height,T=this.container.offsetWidth,L=this.container.offsetHeight,I=parseInt(L/A),B=function(W){var ae=W.offsetWidth||parseInt(W.style.width),pe=W.getBoundingClientRect().right||m.container.getBoundingClientRect().right+ae;return m.container.getBoundingClientRect().right-pe},N=function(W){return(T+W)/5},X=function(W,ae,pe){for(var Ue=T/N(pe),Me=function(Rt){var Dt=m.danTunnel[ae][Rt+""];if(!Dt||!Dt.length)return m.danTunnel[ae][Rt+""]=[W],W.addEventListener("animationend",function(){m.danTunnel[ae][Rt+""].splice(0,1)}),{v:Rt%I};if(ae!=="right")return"continue";for(var Xe=0;Xe<Dt.length;Xe++){var $n=B(Dt[Xe])-10;if($n<=T-Ue*N(parseInt(Dt[Xe].style.width))||$n<=0)break;if(Xe===Dt.length-1)return m.danTunnel[ae][Rt+""].push(W),W.addEventListener("animationend",function(){m.danTunnel[ae][Rt+""].splice(0,1)}),{v:Rt%I}}},Ie=0;m.unlimited||Ie<I;Ie++){var Et=Me(Ie);if(Et!=="continue"&&Lt(Et)==="object")return Et.v}return-1};Object.prototype.toString.call(f)!=="[object Array]"&&(f=[f]);for(var J=document.createDocumentFragment(),re=function(){f[$].type=ne.number2Type(f[$].type),f[$].color||(f[$].color=16777215);var W=document.createElement("div");W.classList.add("dplayer-danmaku-item"),W.classList.add("dplayer-danmaku-".concat(f[$].type)),f[$].border?W.innerHTML='<span style="border:'.concat(f[$].border,'">').concat(f[$].text,"</span>"):W.innerHTML=f[$].text,W.style.opacity=m._opacity,W.style.color=ne.number2Color(f[$].color),W.addEventListener("animationend",function(){m.container.removeChild(W)});var ae,pe=m._measure(f[$].text);switch(f[$].type){case"right":(ae=X(W,f[$].type,pe))>=0&&(W.style.width=pe+1+"px",W.style.top=A*ae+"px",W.style.transform="translateX(-".concat(T,"px)"));break;case"top":(ae=X(W,f[$].type))>=0&&(W.style.top=A*ae+"px");break;case"bottom":(ae=X(W,f[$].type))>=0&&(W.style.bottom=A*ae+"px");break;default:console.error("Can't handled danmaku type: ".concat(f[$].type))}ae>=0&&(W.classList.add("dplayer-danmaku-move"),W.style.animationDuration=m._danAnimation(f[$].type),J.appendChild(W))},$=0;$<f.length;$++)re();return this.container.appendChild(J),J}}},{key:"play",value:function(){this.paused=!1}},{key:"pause",value:function(){this.paused=!0}},{key:"_measure",value:function(f){if(!this.context){var m=getComputedStyle(this.container.getElementsByClassName("dplayer-danmaku-item")[0],null);this.context=document.createElement("canvas").getContext("2d"),this.context.font=m.getPropertyValue("font")}return this.context.measureText(f).width}},{key:"seek",value:function(){this.clear();for(var f=0;f<this.dan.length;f++){if(this.dan[f].time>=this.options.time()){this.danIndex=f;break}this.danIndex=this.dan.length}}},{key:"clear",value:function(){this.danTunnel={right:{},top:{},bottom:{}},this.danIndex=0,this.options.container.innerHTML="",this.events&&this.events.trigger("danmaku_clear")}},{key:"htmlEncode",value:function(f){return f.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/"/g,"&quot;").replace(/'/g,"&#x27;").replace(/\//g,"&#x2f;")}},{key:"resize",value:function(){for(var f=this.container.offsetWidth,m=this.container.getElementsByClassName("dplayer-danmaku-item"),A=0;A<m.length;A++)m[A].style.transform="translateX(-".concat(f,"px)")}},{key:"hide",value:function(){this.showing=!1,this.pause(),this.clear(),this.events&&this.events.trigger("danmaku_hide")}},{key:"show",value:function(){this.seek(),this.showing=!0,this.play(),this.events&&this.events.trigger("danmaku_show")}},{key:"unlimit",value:function(f){this.unlimited=f}},{key:"speed",value:function(f){this.options.api.speedRate=f}},{key:"_danAnimation",value:function(f){var m=this.options.api.speedRate||1,A=!!this.player.fullScreen.isFullScreen();return{top:"".concat((A?6:4)/m,"s"),right:"".concat((A?8:5)/m,"s"),bottom:"".concat((A?6:4)/m,"s")}[f]}}],x&&Ye(v.prototype,x),Object.defineProperty(v,"prototype",{writable:!1}),h}();const Rr=ga;function $e(h){return $e=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},$e(h)}function gi(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if($e(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if($e(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),$e(m)==="symbol"?m:String(m)),f)}var m}const ya=function(){function h(){(function(f,m){if(!(f instanceof m))throw new TypeError("Cannot call a class as a function")})(this,h),this.events={},this.videoEvents=["abort","canplay","canplaythrough","durationchange","emptied","ended","error","loadeddata","loadedmetadata","loadstart","mozaudioavailable","pause","play","playing","progress","ratechange","seeked","seeking","stalled","suspend","timeupdate","volumechange","waiting"],this.playerEvents=["screenshot","thumbnails_show","thumbnails_hide","danmaku_show","danmaku_hide","danmaku_clear","danmaku_loaded","danmaku_send","danmaku_opacity","contextmenu_show","contextmenu_hide","notice_show","notice_hide","quality_start","quality_end","destroy","resize","fullscreen","fullscreen_cancel","webfullscreen","webfullscreen_cancel","subtitle_show","subtitle_hide","subtitle_change"]}var v,x;return v=h,(x=[{key:"on",value:function(f,m){this.type(f)&&typeof m=="function"&&(this.events[f]||(this.events[f]=[]),this.events[f].push(m))}},{key:"trigger",value:function(f,m){if(this.events[f]&&this.events[f].length)for(var A=0;A<this.events[f].length;A++)this.events[f][A](m)}},{key:"type",value:function(f){return this.playerEvents.indexOf(f)!==-1?"player":this.videoEvents.indexOf(f)!==-1?"video":(console.error("Unknown event name: ".concat(f)),null)}}])&&gi(v.prototype,x),Object.defineProperty(v,"prototype",{writable:!1}),h}();function Wt(h){return Wt=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},Wt(h)}function yi(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(Wt(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(Wt(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),Wt(m)==="symbol"?m:String(m)),f)}var m}var ht=function(){function h(f){var m=this;(function(A,T){if(!(A instanceof T))throw new TypeError("Cannot call a class as a function")})(this,h),this.player=f,this.lastScrollPosition={left:0,top:0},this.player.events.on("webfullscreen",function(){m.player.resize()}),this.player.events.on("webfullscreen_cancel",function(){m.player.resize(),ne.setScrollPosition(m.lastScrollPosition)}),this.fullscreenchange=function(){m.player.resize(),m.isFullScreen("browser")?m.player.events.trigger("fullscreen"):(ne.setScrollPosition(m.lastScrollPosition),m.player.events.trigger("fullscreen_cancel"))},this.docfullscreenchange=function(){var A=document.fullscreenElement||document.mozFullScreenElement||document.msFullscreenElement;A&&A!==m.player.container||(m.player.resize(),A?m.player.events.trigger("fullscreen"):(ne.setScrollPosition(m.lastScrollPosition),m.player.events.trigger("fullscreen_cancel")))},/Firefox/.test(navigator.userAgent)?(document.addEventListener("mozfullscreenchange",this.docfullscreenchange),document.addEventListener("fullscreenchange",this.docfullscreenchange)):(this.player.container.addEventListener("fullscreenchange",this.fullscreenchange),this.player.container.addEventListener("webkitfullscreenchange",this.fullscreenchange),document.addEventListener("msfullscreenchange",this.docfullscreenchange),document.addEventListener("MSFullscreenChange",this.docfullscreenchange))}var v,x;return v=h,x=[{key:"isFullScreen",value:function(){var f=arguments.length>0&&arguments[0]!==void 0?arguments[0]:"browser";switch(f){case"browser":return document.fullscreenElement||document.mozFullScreenElement||document.webkitFullscreenElement||document.msFullscreenElement;case"web":return this.player.container.classList.contains("dplayer-fulled")}}},{key:"request",value:function(){var f=arguments.length>0&&arguments[0]!==void 0?arguments[0]:"browser",m=f==="browser"?"web":"browser",A=this.isFullScreen(m);switch(A||(this.lastScrollPosition=ne.getScrollPosition()),f){case"browser":this.player.container.requestFullscreen?this.player.container.requestFullscreen():this.player.container.mozRequestFullScreen?this.player.container.mozRequestFullScreen():this.player.container.webkitRequestFullscreen?this.player.container.webkitRequestFullscreen():this.player.video.webkitEnterFullscreen?this.player.video.webkitEnterFullscreen():this.player.video.webkitEnterFullScreen?this.player.video.webkitEnterFullScreen():this.player.container.msRequestFullscreen&&this.player.container.msRequestFullscreen();break;case"web":this.player.container.classList.add("dplayer-fulled"),document.body.classList.add("dplayer-web-fullscreen-fix"),this.player.events.trigger("webfullscreen")}A&&this.cancel(m)}},{key:"cancel",value:function(){var f=arguments.length>0&&arguments[0]!==void 0?arguments[0]:"browser";switch(f){case"browser":document.cancelFullScreen?document.cancelFullScreen():document.mozCancelFullScreen?document.mozCancelFullScreen():document.webkitCancelFullScreen?document.webkitCancelFullScreen():document.webkitCancelFullscreen?document.webkitCancelFullscreen():document.msCancelFullScreen?document.msCancelFullScreen():document.msExitFullscreen&&document.msExitFullscreen();break;case"web":this.player.container.classList.remove("dplayer-fulled"),document.body.classList.remove("dplayer-web-fullscreen-fix"),this.player.events.trigger("webfullscreen_cancel")}}},{key:"toggle",value:function(){var f=arguments.length>0&&arguments[0]!==void 0?arguments[0]:"browser";this.isFullScreen(f)?this.cancel(f):this.request(f)}},{key:"destroy",value:function(){/Firefox/.test(navigator.userAgent)?(document.removeEventListener("mozfullscreenchange",this.docfullscreenchange),document.removeEventListener("fullscreenchange",this.docfullscreenchange)):(this.player.container.removeEventListener("fullscreenchange",this.fullscreenchange),this.player.container.removeEventListener("webkitfullscreenchange",this.fullscreenchange),document.removeEventListener("msfullscreenchange",this.docfullscreenchange),document.removeEventListener("MSFullscreenChange",this.docfullscreenchange))}}],x&&yi(v.prototype,x),Object.defineProperty(v,"prototype",{writable:!1}),h}();const Aa=ht;function An(h){return An=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},An(h)}function ba(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(An(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(An(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),An(m)==="symbol"?m:String(m)),f)}var m}var Fe=function(){function h(f){(function(m,A){if(!(m instanceof A))throw new TypeError("Cannot call a class as a function")})(this,h),this.storageName={opacity:"dplayer-danmaku-opacity",volume:"dplayer-volume",unlimited:"dplayer-danmaku-unlimited",danmaku:"dplayer-danmaku-show",subtitle:"dplayer-subtitle-show"},this.default={opacity:.7,volume:f.options.hasOwnProperty("volume")?f.options.volume:.7,unlimited:(f.options.danmaku&&f.options.danmaku.unlimited?1:0)||0,danmaku:1,subtitle:1},this.data={},this.init()}var v,x;return v=h,(x=[{key:"init",value:function(){for(var f in this.storageName){var m=this.storageName[f];this.data[f]=parseFloat(ne.storage.get(m)||this.default[f])}}},{key:"get",value:function(f){return this.data[f]}},{key:"set",value:function(f,m){this.data[f]=m,ne.storage.set(this.storageName[f],m)}}])&&ba(v.prototype,x),Object.defineProperty(v,"prototype",{writable:!1}),h}();const Dr=Fe;function At(h){return At=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},At(h)}function Ai(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(At(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(At(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),At(m)==="symbol"?m:String(m)),f)}var m}var Ea=function(){function h(f,m,A,T){(function(L,I){if(!(L instanceof I))throw new TypeError("Cannot call a class as a function")})(this,h),this.container=f,this.video=m,this.options=A,this.events=T,this.init()}var v,x;return v=h,x=[{key:"init",value:function(){var f=this;if(this.container.style.fontSize=this.options.fontSize,this.container.style.bottom=this.options.bottom,this.container.style.color=this.options.color,this.video.textTracks&&this.video.textTracks[0]){var m=this.video.textTracks[0];m.oncuechange=function(){var A=m.activeCues[m.activeCues.length-1];if(f.container.innerHTML="",A){var T=document.createElement("div");T.appendChild(A.getCueAsHTML());var L=T.innerHTML.split(/\r?\n/).map(function(I){return"<p>".concat(I,"</p>")}).join("");f.container.innerHTML=L}f.events.trigger("subtitle_change")}}}},{key:"show",value:function(){this.container.classList.remove("dplayer-subtitle-hide"),this.events.trigger("subtitle_show")}},{key:"hide",value:function(){this.container.classList.add("dplayer-subtitle-hide"),this.events.trigger("subtitle_hide")}},{key:"toggle",value:function(){this.container.classList.contains("dplayer-subtitle-hide")?this.show():this.hide()}}],x&&Ai(v.prototype,x),Object.defineProperty(v,"prototype",{writable:!1}),h}();const xa=Ea;function zt(h){return zt=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},zt(h)}function _r(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(zt(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(zt(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),zt(m)==="symbol"?m:String(m)),f)}var m}var Ta=function(){function h(f){var m=this;(function(I,B){if(!(I instanceof B))throw new TypeError("Cannot call a class as a function")})(this,h),this.player=f,this.player.template.mask.addEventListener("click",function(){m.hide()}),this.player.template.subtitlesButton.addEventListener("click",function(){m.adaptiveHeight(),m.show()});for(var A=this.player.template.subtitlesItem.length-1,T=function(I){m.player.template.subtitlesItem[I].addEventListener("click",function(){m.hide(),m.player.options.subtitle.index!==I&&(m.player.template.subtitle.innerHTML="<p></p>",m.player.template.subtrack.src=m.player.template.subtitlesItem[I].dataset.subtitle,m.player.options.subtitle.index=I,m.player.template.subtitle.classList.contains("dplayer-subtitle-hide")&&m.subContainerShow())})},L=0;L<A;L++)T(L);this.player.template.subtitlesItem[A].addEventListener("click",function(){m.hide(),m.player.options.subtitle.index!==A&&(m.player.template.subtitle.innerHTML="<p></p>",m.player.template.subtrack.src="",m.player.options.subtitle.index=A,m.subContainerHide())})}var v,x;return v=h,(x=[{key:"subContainerShow",value:function(){this.player.template.subtitle.classList.remove("dplayer-subtitle-hide"),this.player.events.trigger("subtitle_show")}},{key:"subContainerHide",value:function(){this.player.template.subtitle.classList.add("dplayer-subtitle-hide"),this.player.events.trigger("subtitle_hide")}},{key:"hide",value:function(){this.player.template.subtitlesBox.classList.remove("dplayer-subtitles-box-open"),this.player.template.mask.classList.remove("dplayer-mask-show"),this.player.controller.disableAutoHide=!1}},{key:"show",value:function(){this.player.template.subtitlesBox.classList.add("dplayer-subtitles-box-open"),this.player.template.mask.classList.add("dplayer-mask-show"),this.player.controller.disableAutoHide=!0}},{key:"adaptiveHeight",value:function(){var f=30*this.player.template.subtitlesItem.length+14,m=.8*this.player.template.videoWrap.offsetHeight;f>=m-50?(this.player.template.subtitlesBox.style.bottom="8px",this.player.template.subtitlesBox.style["max-height"]=m-8+"px"):(this.player.template.subtitlesBox.style.bottom="50px",this.player.template.subtitlesBox.style["max-height"]=m-50+"px")}}])&&_r(v.prototype,x),Object.defineProperty(v,"prototype",{writable:!1}),h}();const Sa=Ta;function bn(h){return bn=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},bn(h)}function bi(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(bn(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(bn(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),bn(m)==="symbol"?m:String(m)),f)}var m}var Ca=function(){function h(f){(function(m,A){if(!(m instanceof A))throw new TypeError("Cannot call a class as a function")})(this,h),this.elements={},this.elements.volume=f.volumeBar,this.elements.played=f.playedBar,this.elements.loaded=f.loadedBar,this.elements.danmaku=f.danmakuOpacityBar}var v,x;return v=h,(x=[{key:"set",value:function(f,m,A){m=Math.max(m,0),m=Math.min(m,1),this.elements[f].style[A]=100*m+"%"}},{key:"get",value:function(f){return parseFloat(this.elements[f].style.width)/100}}])&&bi(v.prototype,x),Object.defineProperty(v,"prototype",{writable:!1}),h}();const ka=Ca;function En(h){return En=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},En(h)}function wa(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(En(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(En(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),En(m)==="symbol"?m:String(m)),f)}var m}var La=function(){function h(f){(function(m,A){if(!(m instanceof A))throw new TypeError("Cannot call a class as a function")})(this,h),this.player=f,window.requestAnimationFrame=window.requestAnimationFrame||window.webkitRequestAnimationFrame||window.mozRequestAnimationFrame||window.oRequestAnimationFrame||window.msRequestAnimationFrame||function(m){window.setTimeout(m,1e3/60)},this.types=["loading","info","fps"],this.init()}var v,x;return v=h,(x=[{key:"init",value:function(){var f=this;this.types.map(function(m){return m!=="fps"&&f["init".concat(m,"Checker")](),m})}},{key:"initloadingChecker",value:function(){var f=this,m=0,A=0,T=!1;this.loadingChecker=setInterval(function(){f.enableloadingChecker&&(A=f.player.video.currentTime,T||A!==m||f.player.video.paused||(f.player.container.classList.add("dplayer-loading"),T=!0),T&&A>m&&!f.player.video.paused&&(f.player.container.classList.remove("dplayer-loading"),T=!1),m=A)},100)}},{key:"initfpsChecker",value:function(){var f=this;window.requestAnimationFrame(function(){if(f.enablefpsChecker)if(f.initfpsChecker(),f.fpsStart){f.fpsIndex++;var m=new Date;m-f.fpsStart>1e3&&(f.player.infoPanel.fps(f.fpsIndex/(m-f.fpsStart)*1e3),f.fpsStart=new Date,f.fpsIndex=0)}else f.fpsStart=new Date,f.fpsIndex=0;else f.fpsStart=0,f.fpsIndex=0})}},{key:"initinfoChecker",value:function(){var f=this;this.infoChecker=setInterval(function(){f.enableinfoChecker&&f.player.infoPanel.update()},1e3)}},{key:"enable",value:function(f){this["enable".concat(f,"Checker")]=!0,f==="fps"&&this.initfpsChecker()}},{key:"disable",value:function(f){this["enable".concat(f,"Checker")]=!1}},{key:"destroy",value:function(){var f=this;this.types.map(function(m){return f["enable".concat(m,"Checker")]=!1,f["".concat(m,"Checker")]&&clearInterval(f["".concat(m,"Checker")]),m})}}])&&wa(v.prototype,x),Object.defineProperty(v,"prototype",{writable:!1}),h}();const Pr=La;function xn(h){return xn=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},xn(h)}function Y(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(xn(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(xn(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),xn(m)==="symbol"?m:String(m)),f)}var m}const Br=function(){function h(f){var m=this;(function(A,T){if(!(A instanceof T))throw new TypeError("Cannot call a class as a function")})(this,h),this.container=f,this.container.addEventListener("animationend",function(){m.container.classList.remove("dplayer-bezel-transition")})}var v,x;return v=h,(x=[{key:"switch",value:function(f){this.container.innerHTML=f,this.container.classList.add("dplayer-bezel-transition")}}])&&Y(v.prototype,x),Object.defineProperty(v,"prototype",{writable:!1}),h}();function It(h){return It=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},It(h)}function Ei(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(It(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(It(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),It(m)==="symbol"?m:String(m)),f)}var m}var Ia=function(){function h(f){(function(m,A){if(!(m instanceof A))throw new TypeError("Cannot call a class as a function")})(this,h),this.container=f.container,this.barWidth=f.barWidth,this.container.style.backgroundImage="url('".concat(f.url,"')"),this.events=f.events}var v,x;return v=h,(x=[{key:"resize",value:function(f,m,A){this.container.style.width="".concat(f,"px"),this.container.style.height="".concat(m,"px"),this.container.style.top="".concat(2-m,"px"),this.barWidth=A}},{key:"show",value:function(){this.container.style.display="block",this.events&&this.events.trigger("thumbnails_show")}},{key:"move",value:function(f){this.container.style.backgroundPosition="-".concat(160*(Math.ceil(f/this.barWidth*100)-1),"px 0"),this.container.style.left="".concat(Math.min(Math.max(f-this.container.offsetWidth/2,-10),this.barWidth-150),"px")}},{key:"hide",value:function(){this.container.style.display="none",this.events&&this.events.trigger("thumbnails_hide")}}])&&Ei(v.prototype,x),Object.defineProperty(v,"prototype",{writable:!1}),h}();const Ra=Ia;function Ve(h){return Ve=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},Ve(h)}function xi(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(Ve(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(Ve(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),Ve(m)==="symbol"?m:String(m)),f)}var m}var bt,Ti=!0,Qn=!1,Si=function(){function h(f){(function(m,A){if(!(m instanceof A))throw new TypeError("Cannot call a class as a function")})(this,h),this.player=f,this.autoHideTimer=0,ne.isMobile||(this.setAutoHideHandler=this.setAutoHide.bind(this),this.player.container.addEventListener("mousemove",this.setAutoHideHandler),this.player.container.addEventListener("click",this.setAutoHideHandler),this.player.on("play",this.setAutoHideHandler),this.player.on("pause",this.setAutoHideHandler)),this.initPlayButton(),this.initThumbnails(),this.initPlayedBar(),this.initFullButton(),this.initQualityButton(),this.initScreenshotButton(),this.player.options.subtitle&&typeof this.player.options.subtitle.url=="string"&&this.initSubtitleButton(),this.initHighlights(),this.initAirplayButton(),this.initChromecastButton(),ne.isMobile||this.initVolumeButton()}var v,x;return v=h,(x=[{key:"initPlayButton",value:function(){var f=this;this.player.template.playButton.addEventListener("click",function(){f.player.toggle()}),this.player.template.mobilePlayButton.addEventListener("click",function(){f.player.toggle()}),ne.isMobile?(this.player.template.videoWrap.addEventListener("click",function(){f.toggle()}),this.player.template.controllerMask.addEventListener("click",function(){f.toggle()})):this.player.options.preventClickToggle||(this.player.template.videoWrap.addEventListener("click",function(){f.player.toggle()}),this.player.template.controllerMask.addEventListener("click",function(){f.player.toggle()}))}},{key:"initHighlights",value:function(){var f=this;this.player.on("durationchange",function(){if(f.player.video.duration!==1&&f.player.video.duration!==1/0&&f.player.options.highlight){var m=f.player.template.playedBarWrap.querySelectorAll(".dplayer-highlight");[].slice.call(m,0).forEach(function(L){f.player.template.playedBarWrap.removeChild(L)});for(var A=0;A<f.player.options.highlight.length;A++)if(f.player.options.highlight[A].text&&f.player.options.highlight[A].time){var T=document.createElement("div");T.classList.add("dplayer-highlight"),T.style.left=f.player.options.highlight[A].time/f.player.video.duration*100+"%",T.innerHTML='<span class="dplayer-highlight-text">'+f.player.options.highlight[A].text+"</span>",f.player.template.playedBarWrap.insertBefore(T,f.player.template.playedBarTime)}}})}},{key:"initThumbnails",value:function(){var f=this;this.player.options.video.thumbnails&&(this.thumbnails=new Ra({container:this.player.template.barPreview,barWidth:this.player.template.barWrap.offsetWidth,url:this.player.options.video.thumbnails,events:this.player.events}),this.player.on("loadedmetadata",function(){f.thumbnails.resize(160,f.player.video.videoHeight/f.player.video.videoWidth*160,f.player.template.barWrap.offsetWidth)}))}},{key:"initPlayedBar",value:function(){var f=this,m=function(T){var L=((T.clientX||T.changedTouches[0].clientX)-ne.getBoundingClientRectViewLeft(f.player.template.playedBarWrap))/f.player.template.playedBarWrap.clientWidth;L=Math.max(L,0),L=Math.min(L,1),f.player.bar.set("played",L,"width"),f.player.template.ptime.innerHTML=ne.secondToTime(L*f.player.video.duration)},A=function T(L){document.removeEventListener(ne.nameMap.dragEnd,T),document.removeEventListener(ne.nameMap.dragMove,m);var I=((L.clientX||L.changedTouches[0].clientX)-ne.getBoundingClientRectViewLeft(f.player.template.playedBarWrap))/f.player.template.playedBarWrap.clientWidth;I=Math.max(I,0),I=Math.min(I,1),f.player.bar.set("played",I,"width"),f.player.seek(f.player.bar.get("played")*f.player.video.duration),f.player.timer.enable("progress")};this.player.template.playedBarWrap.addEventListener(ne.nameMap.dragStart,function(){f.player.timer.disable("progress"),document.addEventListener(ne.nameMap.dragMove,m),document.addEventListener(ne.nameMap.dragEnd,A)}),this.player.template.playedBarWrap.addEventListener(ne.nameMap.dragMove,function(T){if(f.player.video.duration){var L=f.player.template.playedBarWrap.getBoundingClientRect().left,I=(T.clientX||T.changedTouches[0].clientX)-L;if(I<0||I>f.player.template.playedBarWrap.offsetWidth)return;var B=f.player.video.duration*(I/f.player.template.playedBarWrap.offsetWidth);ne.isMobile&&f.thumbnails&&f.thumbnails.show(),f.thumbnails&&f.thumbnails.move(I),f.player.template.playedBarTime.style.left="".concat(I-(B>=3600?25:20),"px"),f.player.template.playedBarTime.innerText=ne.secondToTime(B),f.player.template.playedBarTime.classList.remove("hidden")}}),this.player.template.playedBarWrap.addEventListener(ne.nameMap.dragEnd,function(){ne.isMobile&&f.thumbnails&&f.thumbnails.hide()}),ne.isMobile||(this.player.template.playedBarWrap.addEventListener("mouseenter",function(){f.player.video.duration&&(f.thumbnails&&f.thumbnails.show(),f.player.template.playedBarTime.classList.remove("hidden"))}),this.player.template.playedBarWrap.addEventListener("mouseleave",function(){f.player.video.duration&&(f.thumbnails&&f.thumbnails.hide(),f.player.template.playedBarTime.classList.add("hidden"))}))}},{key:"initFullButton",value:function(){var f=this;this.player.template.browserFullButton.addEventListener("click",function(){f.player.fullScreen.toggle("browser")}),this.player.template.webFullButton.addEventListener("click",function(){f.player.fullScreen.toggle("web")})}},{key:"initVolumeButton",value:function(){var f=this,m=function(T){var L=T||window.event,I=((L.clientX||L.changedTouches[0].clientX)-ne.getBoundingClientRectViewLeft(f.player.template.volumeBarWrap)-5.5)/35;f.player.volume(I)},A=function T(){document.removeEventListener(ne.nameMap.dragEnd,T),document.removeEventListener(ne.nameMap.dragMove,m),f.player.template.volumeButton.classList.remove("dplayer-volume-active")};this.player.template.volumeBarWrapWrap.addEventListener("click",function(T){var L=T||window.event,I=((L.clientX||L.changedTouches[0].clientX)-ne.getBoundingClientRectViewLeft(f.player.template.volumeBarWrap)-5.5)/35;f.player.volume(I)}),this.player.template.volumeBarWrapWrap.addEventListener(ne.nameMap.dragStart,function(){document.addEventListener(ne.nameMap.dragMove,m),document.addEventListener(ne.nameMap.dragEnd,A),f.player.template.volumeButton.classList.add("dplayer-volume-active")}),this.player.template.volumeButtonIcon.addEventListener("click",function(){f.player.video.muted?(f.player.video.muted=!1,f.player.switchVolumeIcon(),f.player.bar.set("volume",f.player.volume(),"width")):(f.player.video.muted=!0,f.player.template.volumeIcon.innerHTML=Oe.volumeOff,f.player.bar.set("volume",0,"width"))})}},{key:"initQualityButton",value:function(){var f=this;this.player.options.video.quality&&this.player.template.qualityList.addEventListener("click",function(m){m.target.classList.contains("dplayer-quality-item")&&f.player.switchQuality(m.target.dataset.index)})}},{key:"initScreenshotButton",value:function(){var f=this;this.player.options.screenshot&&this.player.template.camareButton.addEventListener("click",function(){var m,A=document.createElement("canvas");A.width=f.player.video.videoWidth,A.height=f.player.video.videoHeight,A.getContext("2d").drawImage(f.player.video,0,0,A.width,A.height),A.toBlob(function(T){m=URL.createObjectURL(T);var L=document.createElement("a");L.href=m,L.download="DPlayer.png",L.style.display="none",document.body.appendChild(L),L.click(),document.body.removeChild(L),URL.revokeObjectURL(m),f.player.events.trigger("screenshot",m)})})}},{key:"initAirplayButton",value:function(){this.player.options.airplay&&(window.WebKitPlaybackTargetAvailabilityEvent?this.player.video.addEventListener("webkitplaybacktargetavailabilitychanged",function(f){f.availability==="available"?this.template.airplayButton.disable=!1:this.template.airplayButton.disable=!0,this.template.airplayButton.addEventListener("click",function(){this.video.webkitShowPlaybackTargetPicker()}.bind(this))}.bind(this.player)):this.player.template.airplayButton.style.display="none")}},{key:"initChromecast",value:function(){var f=window.document.createElement("script");f.setAttribute("type","text/javascript"),f.setAttribute("src","https://www.gstatic.com/cv/js/sender/v1/cast_sender.js?loadCastFramework=1"),window.document.body.appendChild(f),window.__onGCastApiAvailable=function(m){if(m){var A=new(bt=window.chrome.cast).SessionRequest(bt.media.DEFAULT_MEDIA_RECEIVER_APP_ID),T=new bt.ApiConfig(A,function(){},function(L){L===bt.ReceiverAvailability.AVAILABLE&&console.log("chromecast: ",L)});bt.initialize(T,function(){})}}}},{key:"initChromecastButton",value:function(){var f=this;if(this.player.options.chromecast){Ti&&(Ti=!1,this.initChromecast());var m=function(T,L){f.currentMedia=L},A=function(T){console.error("Error launching media",T)};this.player.template.chromecastButton.addEventListener("click",function(){Qn?(Qn=!1,f.currentMedia.stop(),f.session.stop(),f.initChromecast()):(Qn=!0,bt.requestSession(function(T){var L,I,B;f.session=T,L=f.player.options.video.url,I=new bt.media.MediaInfo(L),B=new bt.media.LoadRequest(I),f.session?f.session.loadMedia(B,m.bind(f,"loadMedia"),A).play():window.open(L)},function(T){T.code==="cancel"?f.session=void 0:console.error("Error selecting a cast device",T)}))})}}},{key:"initSubtitleButton",value:function(){var f=this;this.player.events.on("subtitle_show",function(){f.player.template.subtitleButton.dataset.balloon=f.player.tran("hide-subs"),f.player.template.subtitleButtonInner.style.opacity="",f.player.user.set("subtitle",1)}),this.player.events.on("subtitle_hide",function(){f.player.template.subtitleButton.dataset.balloon=f.player.tran("show-subs"),f.player.template.subtitleButtonInner.style.opacity="0.4",f.player.user.set("subtitle",0)}),this.player.template.subtitleButton.addEventListener("click",function(){f.player.subtitle.toggle()})}},{key:"setAutoHide",value:function(){var f=this;this.show(),clearTimeout(this.autoHideTimer),this.autoHideTimer=setTimeout(function(){!f.player.video.played.length||f.player.paused||f.disableAutoHide||f.hide()},3e3)}},{key:"show",value:function(){this.player.container.classList.remove("dplayer-hide-controller")}},{key:"hide",value:function(){this.player.container.classList.add("dplayer-hide-controller"),this.player.setting.hide(),this.player.comment&&this.player.comment.hide()}},{key:"isShow",value:function(){return!this.player.container.classList.contains("dplayer-hide-controller")}},{key:"toggle",value:function(){this.isShow()?this.hide():this.show()}},{key:"destroy",value:function(){ne.isMobile||(this.player.container.removeEventListener("mousemove",this.setAutoHideHandler),this.player.container.removeEventListener("click",this.setAutoHideHandler)),clearTimeout(this.autoHideTimer)}}])&&xi(v.prototype,x),Object.defineProperty(v,"prototype",{writable:!1}),h}();const Or=Si;function Tn(h){return Tn=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},Tn(h)}function Xn(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(Tn(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(Tn(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),Tn(m)==="symbol"?m:String(m)),f)}var m}var Da=function(){function h(f){var m=this;(function(B,N){if(!(B instanceof N))throw new TypeError("Cannot call a class as a function")})(this,h),this.player=f,this.player.template.mask.addEventListener("click",function(){m.hide()}),this.player.template.settingButton.addEventListener("click",function(){m.show()}),this.loop=this.player.options.loop,this.player.template.loopToggle.checked=this.loop,this.player.template.loop.addEventListener("click",function(){m.player.template.loopToggle.checked=!m.player.template.loopToggle.checked,m.player.template.loopToggle.checked?m.loop=!0:m.loop=!1,m.hide()}),this.showDanmaku=this.player.user.get("danmaku"),this.showDanmaku||this.player.danmaku&&this.player.danmaku.hide(),this.player.template.showDanmakuToggle.checked=this.showDanmaku,this.player.template.showDanmaku.addEventListener("click",function(){m.player.template.showDanmakuToggle.checked=!m.player.template.showDanmakuToggle.checked,m.player.template.showDanmakuToggle.checked?(m.showDanmaku=!0,m.player.danmaku.show()):(m.showDanmaku=!1,m.player.danmaku.hide()),m.player.user.set("danmaku",m.showDanmaku?1:0),m.hide()}),this.unlimitDanmaku=this.player.user.get("unlimited"),this.player.template.unlimitDanmakuToggle.checked=this.unlimitDanmaku,this.player.template.unlimitDanmaku.addEventListener("click",function(){m.player.template.unlimitDanmakuToggle.checked=!m.player.template.unlimitDanmakuToggle.checked,m.player.template.unlimitDanmakuToggle.checked?(m.unlimitDanmaku=!0,m.player.danmaku.unlimit(!0)):(m.unlimitDanmaku=!1,m.player.danmaku.unlimit(!1)),m.player.user.set("unlimited",m.unlimitDanmaku?1:0),m.hide()}),this.player.template.speed.addEventListener("click",function(){m.player.template.settingBox.classList.add("dplayer-setting-box-narrow"),m.player.template.settingBox.classList.add("dplayer-setting-box-speed")});for(var A=function(B){m.player.template.speedItem[B].addEventListener("click",function(){m.player.speed(m.player.template.speedItem[B].dataset.speed),m.hide()})},T=0;T<this.player.template.speedItem.length;T++)A(T);if(this.player.danmaku){this.player.on("danmaku_opacity",function(B){m.player.bar.set("danmaku",B,"width"),m.player.user.set("opacity",B)}),this.player.danmaku.opacity(this.player.user.get("opacity"));var L=function(B){var N=B||window.event,X=((N.clientX||N.changedTouches[0].clientX)-ne.getBoundingClientRectViewLeft(m.player.template.danmakuOpacityBarWrap))/130;X=Math.max(X,0),X=Math.min(X,1),m.player.danmaku.opacity(X)},I=function B(){document.removeEventListener(ne.nameMap.dragEnd,B),document.removeEventListener(ne.nameMap.dragMove,L),m.player.template.danmakuOpacityBox.classList.remove("dplayer-setting-danmaku-active")};this.player.template.danmakuOpacityBarWrapWrap.addEventListener("click",function(B){var N=B||window.event,X=((N.clientX||N.changedTouches[0].clientX)-ne.getBoundingClientRectViewLeft(m.player.template.danmakuOpacityBarWrap))/130;X=Math.max(X,0),X=Math.min(X,1),m.player.danmaku.opacity(X)}),this.player.template.danmakuOpacityBarWrapWrap.addEventListener(ne.nameMap.dragStart,function(){document.addEventListener(ne.nameMap.dragMove,L),document.addEventListener(ne.nameMap.dragEnd,I),m.player.template.danmakuOpacityBox.classList.add("dplayer-setting-danmaku-active")})}}var v,x;return v=h,(x=[{key:"hide",value:function(){var f=this;this.player.template.settingBox.classList.remove("dplayer-setting-box-open"),this.player.template.mask.classList.remove("dplayer-mask-show"),setTimeout(function(){f.player.template.settingBox.classList.remove("dplayer-setting-box-narrow"),f.player.template.settingBox.classList.remove("dplayer-setting-box-speed")},300),this.player.controller.disableAutoHide=!1}},{key:"show",value:function(){this.player.template.settingBox.classList.add("dplayer-setting-box-open"),this.player.template.mask.classList.add("dplayer-mask-show"),this.player.controller.disableAutoHide=!0}}])&&Xn(v.prototype,x),Object.defineProperty(v,"prototype",{writable:!1}),h}();const _a=Da;function Vt(h){return Vt=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},Vt(h)}function Ci(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(Vt(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(Vt(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),Vt(m)==="symbol"?m:String(m)),f)}var m}var Pa=function(){function h(f){var m=this;(function(A,T){if(!(A instanceof T))throw new TypeError("Cannot call a class as a function")})(this,h),this.player=f,this.player.template.mask.addEventListener("click",function(){m.hide()}),this.player.template.commentButton.addEventListener("click",function(){m.show()}),this.player.template.commentSettingButton.addEventListener("click",function(){m.toggleSetting()}),this.player.template.commentColorSettingBox.addEventListener("click",function(){if(m.player.template.commentColorSettingBox.querySelector("input:checked+span")){var A=m.player.template.commentColorSettingBox.querySelector("input:checked").value;m.player.template.commentSettingFill.style.fill=A,m.player.template.commentInput.style.color=A,m.player.template.commentSendFill.style.fill=A}}),this.player.template.commentInput.addEventListener("click",function(){m.hideSetting()}),this.player.template.commentInput.addEventListener("keydown",function(A){(A||window.event).keyCode===13&&m.send()}),this.player.template.commentSendButton.addEventListener("click",function(){m.send()})}var v,x;return v=h,(x=[{key:"show",value:function(){this.player.controller.disableAutoHide=!0,this.player.template.controller.classList.add("dplayer-controller-comment"),this.player.template.mask.classList.add("dplayer-mask-show"),this.player.container.classList.add("dplayer-show-controller"),this.player.template.commentInput.focus()}},{key:"hide",value:function(){this.player.template.controller.classList.remove("dplayer-controller-comment"),this.player.template.mask.classList.remove("dplayer-mask-show"),this.player.container.classList.remove("dplayer-show-controller"),this.player.controller.disableAutoHide=!1,this.hideSetting()}},{key:"showSetting",value:function(){this.player.template.commentSettingBox.classList.add("dplayer-comment-setting-open")}},{key:"hideSetting",value:function(){this.player.template.commentSettingBox.classList.remove("dplayer-comment-setting-open")}},{key:"toggleSetting",value:function(){this.player.template.commentSettingBox.classList.contains("dplayer-comment-setting-open")?this.hideSetting():this.showSetting()}},{key:"send",value:function(){var f=this;this.player.template.commentInput.blur(),this.player.template.commentInput.value.replace(/^\s+|\s+$/g,"")?this.player.danmaku.send({text:this.player.template.commentInput.value,color:ne.color2Number(this.player.container.querySelector(".dplayer-comment-setting-color input:checked").value),type:parseInt(this.player.container.querySelector(".dplayer-comment-setting-type input:checked").value)},function(){f.player.template.commentInput.value="",f.hide()}):this.player.notice(this.player.tran("please-input-danmaku"))}}])&&Ci(v.prototype,x),Object.defineProperty(v,"prototype",{writable:!1}),h}();const ki=Pa;function Sn(h){return Sn=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},Sn(h)}function Ba(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(Sn(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(Sn(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),Sn(m)==="symbol"?m:String(m)),f)}var m}var Oa=function(){function h(f){(function(m,A){if(!(m instanceof A))throw new TypeError("Cannot call a class as a function")})(this,h),this.player=f,this.doHotKeyHandler=this.doHotKey.bind(this),this.cancelFullScreenHandler=this.cancelFullScreen.bind(this),this.player.options.hotkey&&document.addEventListener("keydown",this.doHotKeyHandler),document.addEventListener("keydown",this.cancelFullScreenHandler)}var v,x;return v=h,(x=[{key:"doHotKey",value:function(f){if(this.player.focus){var m=document.activeElement.tagName.toUpperCase(),A=document.activeElement.getAttribute("contenteditable");if(m!=="INPUT"&&m!=="TEXTAREA"&&A!==""&&A!=="true"){var T,L=f||window.event;switch(L.keyCode){case 32:L.preventDefault(),this.player.toggle();break;case 37:if(L.preventDefault(),this.player.options.live)break;this.player.seek(this.player.video.currentTime-5),this.player.controller.setAutoHide();break;case 39:if(L.preventDefault(),this.player.options.live)break;this.player.seek(this.player.video.currentTime+5),this.player.controller.setAutoHide();break;case 38:L.preventDefault(),T=this.player.volume()+.1,this.player.volume(T);break;case 40:L.preventDefault(),T=this.player.volume()-.1,this.player.volume(T)}}}}},{key:"cancelFullScreen",value:function(f){(f||window.event).keyCode===27&&this.player.fullScreen.isFullScreen("web")&&this.player.fullScreen.cancel("web")}},{key:"destroy",value:function(){this.player.options.hotkey&&document.removeEventListener("keydown",this.doHotKeyHandler),document.removeEventListener("keydown",this.cancelFullScreenHandler)}}])&&Ba(v.prototype,x),Object.defineProperty(v,"prototype",{writable:!1}),h}();const Zn=Oa;function Cn(h){return Cn=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},Cn(h)}function Ma(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(Cn(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(Cn(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),Cn(m)==="symbol"?m:String(m)),f)}var m}var Fa=function(){function h(f){var m=this;(function(A,T){if(!(A instanceof T))throw new TypeError("Cannot call a class as a function")})(this,h),this.player=f,this.shown=!1,Array.prototype.slice.call(this.player.template.menuItem).forEach(function(A,T){m.player.options.contextmenu[T].click&&A.addEventListener("click",function(){m.player.options.contextmenu[T].click(m.player),m.hide()})}),this.contextmenuHandler=function(A){if(m.shown)m.hide();else{var T=A||window.event;T.preventDefault();var L=m.player.container.getBoundingClientRect();m.show(T.clientX-L.left,T.clientY-L.top),m.player.template.mask.addEventListener("click",function(){m.hide()})}},this.player.container.addEventListener("contextmenu",this.contextmenuHandler)}var v,x;return v=h,(x=[{key:"show",value:function(f,m){this.player.template.menu.classList.add("dplayer-menu-show");var A=this.player.container.getBoundingClientRect();f+this.player.template.menu.offsetWidth>=A.width?(this.player.template.menu.style.right=A.width-f+"px",this.player.template.menu.style.left="initial"):(this.player.template.menu.style.left=f+"px",this.player.template.menu.style.right="initial"),m+this.player.template.menu.offsetHeight>=A.height?(this.player.template.menu.style.bottom=A.height-m+"px",this.player.template.menu.style.top="initial"):(this.player.template.menu.style.top=m+"px",this.player.template.menu.style.bottom="initial"),this.player.template.mask.classList.add("dplayer-mask-show"),this.shown=!0,this.player.events.trigger("contextmenu_show")}},{key:"hide",value:function(){this.player.template.mask.classList.remove("dplayer-mask-show"),this.player.template.menu.classList.remove("dplayer-menu-show"),this.shown=!1,this.player.events.trigger("contextmenu_hide")}},{key:"destroy",value:function(){this.player.container.removeEventListener("contextmenu",this.contextmenuHandler)}}])&&Ma(v.prototype,x),Object.defineProperty(v,"prototype",{writable:!1}),h}();const Na=Fa;function jt(h){return jt=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},jt(h)}function wi(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,(m=function(A,T){if(jt(A)!=="object"||A===null)return A;var L=A[Symbol.toPrimitive];if(L!==void 0){var I=L.call(A,"string");if(jt(I)!=="object")return I;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(A)}(f.key),jt(m)==="symbol"?m:String(m)),f)}var m}var Mr=function(){function h(f){var m=this;(function(A,T){if(!(A instanceof T))throw new TypeError("Cannot call a class as a function")})(this,h),this.container=f.template.infoPanel,this.template=f.template,this.video=f.video,this.player=f,this.template.infoPanelClose.addEventListener("click",function(){m.hide()})}var v,x;return v=h,(x=[{key:"show",value:function(){this.beginTime=Date.now(),this.update(),this.player.timer.enable("info"),this.player.timer.enable("fps"),this.container.classList.remove("dplayer-info-panel-hide")}},{key:"hide",value:function(){this.player.timer.disable("info"),this.player.timer.disable("fps"),this.container.classList.add("dplayer-info-panel-hide")}},{key:"triggle",value:function(){this.container.classList.contains("dplayer-info-panel-hide")?this.show():this.hide()}},{key:"update",value:function(){this.template.infoVersion.innerHTML="v".concat("1.27.1"," ").concat("v1.27.0-12-g4f61091"),this.template.infoType.innerHTML=this.player.type,this.template.infoUrl.innerHTML=this.player.options.video.url,this.template.infoResolution.innerHTML="".concat(this.player.video.videoWidth," x ").concat(this.player.video.videoHeight),this.template.infoDuration.innerHTML=this.player.video.duration,this.player.options.danmaku&&(this.template.infoDanmakuId.innerHTML=this.player.options.danmaku.id,this.template.infoDanmakuApi.innerHTML=this.player.options.danmaku.api,this.template.infoDanmakuAmount.innerHTML=this.player.danmaku.dan.length)}},{key:"fps",value:function(f){this.template.infoFPS.innerHTML="".concat(f.toFixed(1))}}])&&wi(v.prototype,x),Object.defineProperty(v,"prototype",{writable:!1}),h}();const Li=Mr;var Ua=Q(568),Ka=Q.n(Ua);function Qt(h){return Qt=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(v){return typeof v}:function(v){return v&&typeof Symbol=="function"&&v.constructor===Symbol&&v!==Symbol.prototype?"symbol":typeof v},Qt(h)}function Ii(h,v){var x=Object.keys(h);if(Object.getOwnPropertySymbols){var f=Object.getOwnPropertySymbols(h);v&&(f=f.filter(function(m){return Object.getOwnPropertyDescriptor(h,m).enumerable})),x.push.apply(x,f)}return x}function je(h,v,x){return(v=kn(v))in h?Object.defineProperty(h,v,{value:x,enumerable:!0,configurable:!0,writable:!0}):h[v]=x,h}function Ri(h,v){for(var x=0;x<v.length;x++){var f=v[x];f.enumerable=f.enumerable||!1,f.configurable=!0,"value"in f&&(f.writable=!0),Object.defineProperty(h,kn(f.key),f)}}function kn(h){var v=function(x,f){if(Qt(x)!=="object"||x===null)return x;var m=x[Symbol.toPrimitive];if(m!==void 0){var A=m.call(x,"string");if(Qt(A)!=="object")return A;throw new TypeError("@@toPrimitive must return a primitive value.")}return String(x)}(h);return Qt(v)==="symbol"?v:String(v)}var Jn=0,Xt=[],Ga=function(){function h(m){var A=this;(function(T,L){if(!(T instanceof L))throw new TypeError("Cannot call a class as a function")})(this,h),this.options=function(T){var L={container:T.element||document.getElementsByClassName("dplayer")[0],live:!1,autoplay:!1,theme:"#b7daff",loop:!1,lang:(navigator.language||navigator.browserLanguage).toLowerCase(),screenshot:!1,airplay:!0,chromecast:!1,hotkey:!0,preload:"metadata",volume:.7,playbackSpeed:[.5,.75,1,1.25,1.5,2],apiBackend:Ji,video:{},contextmenu:[],mutex:!0,pluginOptions:{hls:{},flv:{},dash:{},webtorrent:{}},preventClickToggle:!1};for(var I in L)L.hasOwnProperty(I)&&!T.hasOwnProperty(I)&&(T[I]=L[I]);return T.video&&!T.video.type&&(T.video.type="auto"),xr(T.danmaku)==="object"&&T.danmaku&&!T.danmaku.user&&(T.danmaku.user="DIYgod"),T.subtitle&&(!T.subtitle.type&&(T.subtitle.type="webvtt"),!T.subtitle.fontSize&&(T.subtitle.fontSize="20px"),!T.subtitle.bottom&&(T.subtitle.bottom="40px"),!T.subtitle.color&&(T.subtitle.color="#fff")),T.video.quality&&(T.video.url=T.video.quality[T.video.defaultQuality].url),T.lang&&(T.lang=T.lang.toLowerCase()),T.contextmenu=T.contextmenu.concat([{key:"video-info",click:function(B){B.infoPanel.triggle()}},{key:"about-author",link:"https://diygod.me"},{text:"DPlayer v".concat("1.27.1"),link:"https://github.com/MoePlayer/DPlayer"}]),T}(function(T){for(var L=1;L<arguments.length;L++){var I=arguments[L]!=null?arguments[L]:{};L%2?Ii(Object(I),!0).forEach(function(B){je(T,B,I[B])}):Object.getOwnPropertyDescriptors?Object.defineProperties(T,Object.getOwnPropertyDescriptors(I)):Ii(Object(I)).forEach(function(B){Object.defineProperty(T,B,Object.getOwnPropertyDescriptor(I,B))})}return T}({preload:m.video.type==="webtorrent"?"none":"metadata"},m)),this.options.video.quality&&(this.qualityIndex=this.options.video.defaultQuality,this.quality=this.options.video.quality[this.options.video.defaultQuality]),this.tran=new it(this.options.lang).tran,this.events=new ya,this.user=new Dr(this),this.container=this.options.container,this.noticeList={},this.container.classList.add("dplayer"),this.options.danmaku||this.container.classList.add("dplayer-no-danmaku"),this.options.live?this.container.classList.add("dplayer-live"):this.container.classList.remove("dplayer-live"),ne.isMobile&&this.container.classList.add("dplayer-mobile"),this.arrow=this.container.offsetWidth<=500,this.arrow&&this.container.classList.add("dplayer-arrow"),this.options.subtitle&&Array.isArray(this.options.subtitle.url)&&(this.options.subtitle.url.push({subtitle:"",lang:"off"}),this.options.subtitle.defaultSubtitle&&(typeof this.options.subtitle.defaultSubtitle=="string"?this.options.subtitle.index=this.options.subtitle.url.findIndex(function(T){return T.lang===A.options.subtitle.defaultSubtitle||T.name===A.options.subtitle.defaultSubtitle}):typeof this.options.subtitle.defaultSubtitle=="number"&&(this.options.subtitle.index=this.options.subtitle.defaultSubtitle)),(this.options.subtitle.index===-1||!this.options.subtitle.index||this.options.subtitle.index>this.options.subtitle.url.length-1)&&(this.options.subtitle.index=this.options.subtitle.url.findIndex(function(T){return T.lang===A.options.lang})),this.options.subtitle.index===-1&&(this.options.subtitle.index=this.options.subtitle.url.length-1)),this.template=new yn({container:this.container,options:this.options,index:Jn,tran:this.tran}),this.video=this.template.video,this.bar=new ka(this.template),this.bezel=new Br(this.template.bezel),this.fullScreen=new Aa(this),this.controller=new Or(this),this.options.danmaku&&(this.danmaku=new Rr({player:this,container:this.template.danmaku,opacity:this.user.get("opacity"),callback:function(){setTimeout(function(){A.template.danmakuLoading.style.display="none",A.options.autoplay&&A.play()},0)},error:function(T){A.notice(T)},apiBackend:this.options.apiBackend,borderColor:this.options.theme,height:this.arrow?24:30,time:function(){return A.video.currentTime},unlimited:this.user.get("unlimited"),api:{id:this.options.danmaku.id,address:this.options.danmaku.api,token:this.options.danmaku.token,maximum:this.options.danmaku.maximum,addition:this.options.danmaku.addition,user:this.options.danmaku.user,speedRate:this.options.danmaku.speedRate},events:this.events,tran:function(T){return A.tran(T)}}),this.comment=new ki(this)),this.setting=new _a(this),this.plugins={},this.docClickFun=function(){A.focus=!1},this.containerClickFun=function(){A.focus=!0},document.addEventListener("click",this.docClickFun,!0),this.container.addEventListener("click",this.containerClickFun,!0),this.paused=!0,this.timer=new Pr(this),this.hotkey=new Zn(this),this.contextmenu=new Na(this),this.initVideo(this.video,this.quality&&this.quality.type||this.options.video.type),this.infoPanel=new Li(this),!this.danmaku&&this.options.autoplay&&this.play(),Jn++,Xt.push(this)}var v,x,f;return v=h,x=[{key:"seek",value:function(m){m=Math.max(m,0),this.video.duration&&(m=Math.min(m,this.video.duration)),this.video.currentTime<m?this.notice("".concat(this.tran("ff").replace("%s",(m-this.video.currentTime).toFixed(0)))):this.video.currentTime>m&&this.notice("".concat(this.tran("rew").replace("%s",(this.video.currentTime-m).toFixed(0)))),this.video.currentTime=m,this.danmaku&&this.danmaku.seek(),this.bar.set("played",m/this.video.duration,"width"),this.template.ptime.innerHTML=ne.secondToTime(m)}},{key:"play",value:function(m){var A=this;if(this.paused=!1,this.video.paused&&!ne.isMobile&&this.bezel.switch(Oe.play),this.template.playButton.innerHTML=Oe.pause,this.template.mobilePlayButton.innerHTML=Oe.pause,m||Ki.resolve(this.video.play()).catch(function(){A.pause()}).then(function(){}),this.timer.enable("loading"),this.container.classList.remove("dplayer-paused"),this.container.classList.add("dplayer-playing"),this.danmaku&&this.danmaku.play(),this.options.mutex)for(var T=0;T<Xt.length;T++)this!==Xt[T]&&Xt[T].pause()}},{key:"pause",value:function(m){this.paused=!0,this.container.classList.remove("dplayer-loading"),this.video.paused||ne.isMobile||this.bezel.switch(Oe.pause),this.template.playButton.innerHTML=Oe.play,this.template.mobilePlayButton.innerHTML=Oe.play,m||this.video.pause(),this.timer.disable("loading"),this.container.classList.remove("dplayer-playing"),this.container.classList.add("dplayer-paused"),this.danmaku&&this.danmaku.pause()}},{key:"switchVolumeIcon",value:function(){this.volume()>=.95?this.template.volumeIcon.innerHTML=Oe.volumeUp:this.volume()>0?this.template.volumeIcon.innerHTML=Oe.volumeDown:this.template.volumeIcon.innerHTML=Oe.volumeOff}},{key:"volume",value:function(m,A,T){if(m=parseFloat(m),!isNaN(m)){m=Math.max(m,0),m=Math.min(m,1),this.bar.set("volume",m,"width");var L="".concat((100*m).toFixed(0),"%");this.template.volumeBarWrapWrap.dataset.balloon=L,A||this.user.set("volume",m),T||this.notice("".concat(this.tran("volume")," ").concat((100*m).toFixed(0),"%"),void 0,void 0,"volume"),this.video.volume=m,this.video.muted&&(this.video.muted=!1),this.switchVolumeIcon()}return this.video.volume}},{key:"toggle",value:function(){this.video.paused?this.play():this.pause()}},{key:"on",value:function(m,A){this.events.on(m,A)}},{key:"switchVideo",value:function(m,A){this.pause(),this.video.poster=m.pic?m.pic:"",this.video.src=m.url,this.initMSE(this.video,m.type||"auto"),A&&(this.template.danmakuLoading.style.display="block",this.bar.set("played",0,"width"),this.bar.set("loaded",0,"width"),this.template.ptime.innerHTML="00:00",this.template.danmaku.innerHTML="",this.danmaku&&this.danmaku.reload({id:A.id,address:A.api,token:A.token,maximum:A.maximum,addition:A.addition,user:A.user}))}},{key:"initMSE",value:function(m,A){var T=this;if(this.type=A,this.options.video.customType&&this.options.video.customType[A])Object.prototype.toString.call(this.options.video.customType[A])==="[object Function]"?this.options.video.customType[A](this.video,this):console.error("Illegal customType: ".concat(A));else switch(this.type==="auto"&&(/m3u8(#|\?|$)/i.exec(m.src)?this.type="hls":/.flv(#|\?|$)/i.exec(m.src)?this.type="flv":/.mpd(#|\?|$)/i.exec(m.src)?this.type="dash":this.type="normal"),this.type==="hls"&&(m.canPlayType("application/x-mpegURL")||m.canPlayType("application/vnd.apple.mpegURL"))&&(this.type="normal"),this.type){case"hls":if(window.Hls)if(window.Hls.isSupported()){var L=this.options.pluginOptions.hls,I=new window.Hls(L);this.plugins.hls=I,I.loadSource(m.src),I.attachMedia(m),this.events.on("destroy",function(){I.destroy(),delete T.plugins.hls})}else this.notice("Error: Hls is not supported.");else this.notice("Error: Can't find Hls.");break;case"flv":if(window.flvjs)if(window.flvjs.isSupported()){var B=window.flvjs.createPlayer(Object.assign(this.options.pluginOptions.flv.mediaDataSource||{},{type:"flv",url:m.src}),this.options.pluginOptions.flv.config);this.plugins.flvjs=B,B.attachMediaElement(m),B.load(),this.events.on("destroy",function(){B.unload(),B.detachMediaElement(),B.destroy(),delete T.plugins.flvjs})}else this.notice("Error: flvjs is not supported.");else this.notice("Error: Can't find flvjs.");break;case"dash":if(window.dashjs){var N=window.dashjs.MediaPlayer().create().initialize(m,m.src,!1),X=this.options.pluginOptions.dash;N.updateSettings(X),this.plugins.dash=N,this.events.on("destroy",function(){window.dashjs.MediaPlayer().reset(),delete T.plugins.dash})}else this.notice("Error: Can't find dashjs.");break;case"webtorrent":if(window.WebTorrent)if(window.WebTorrent.WEBRTC_SUPPORT){this.container.classList.add("dplayer-loading");var J=this.options.pluginOptions.webtorrent,re=new window.WebTorrent(J);this.plugins.webtorrent=re;var $=m.src;m.src="",m.preload="metadata",m.addEventListener("durationchange",function(){return T.container.classList.remove("dplayer-loading")},{once:!0}),re.add($,function(W){W.files.find(function(ae){return ae.name.endsWith(".mp4")}).renderTo(T.video,{autoplay:T.options.autoplay,controls:!1})}),this.events.on("destroy",function(){re.remove($),re.destroy(),delete T.plugins.webtorrent})}else this.notice("Error: Webtorrent is not supported.");else this.notice("Error: Can't find Webtorrent.")}}},{key:"initVideo",value:function(m,A){var T=this;this.initMSE(m,A),this.on("durationchange",function(){m.duration!==1&&m.duration!==1/0&&(T.template.dtime.innerHTML=ne.secondToTime(m.duration))}),this.on("progress",function(){var B=m.buffered.length?m.buffered.end(m.buffered.length-1)/m.duration:0;T.bar.set("loaded",B,"width")}),this.on("error",function(){T.video.error&&T.tran&&T.notice&&T.type!=="webtorrent"&&T.notice(T.tran("video-failed"))}),this.on("ended",function(){T.bar.set("played",1,"width"),T.setting.loop?(T.seek(0),T.play()):T.pause(),T.danmaku&&(T.danmaku.danIndex=0)}),this.on("play",function(){T.paused&&T.play(!0)}),this.on("pause",function(){T.paused||T.pause(!0)}),this.on("timeupdate",function(){T.bar.set("played",T.video.currentTime/T.video.duration,"width");var B=ne.secondToTime(T.video.currentTime);T.template.ptime.innerHTML!==B&&(T.template.ptime.innerHTML=B)});for(var L=function(B){m.addEventListener(T.events.videoEvents[B],function(N){T.events.trigger(T.events.videoEvents[B],N)})},I=0;I<this.events.videoEvents.length;I++)L(I);this.volume(this.user.get("volume"),!0,!0),this.options.subtitle&&(this.subtitle=new xa(this.template.subtitle,this.video,this.options.subtitle,this.events),Array.isArray(this.options.subtitle.url)&&(this.subtitles=new Sa(this)),this.user.get("subtitle")||this.subtitle.hide())}},{key:"switchQuality",value:function(m){var A=this;if(m=typeof m=="string"?parseInt(m):m,this.qualityIndex!==m&&!this.switchingQuality){this.prevIndex=this.qualityIndex,this.qualityIndex=m,this.switchingQuality=!0,this.quality=this.options.video.quality[m],this.template.qualityButton.innerHTML=this.quality.name;var T=this.video.paused;this.video.pause();var L=Ka()({current:!1,pic:null,screenshot:this.options.screenshot,preload:"auto",url:this.quality.url,subtitle:this.options.subtitle}),I=new DOMParser().parseFromString(L,"text/html").body.firstChild;this.template.videoWrap.insertBefore(I,this.template.videoWrap.getElementsByTagName("div")[0]),this.prevVideo=this.video,this.video=I,this.initVideo(this.video,this.quality.type||this.options.video.type),this.seek(this.prevVideo.currentTime),this.notice("".concat(this.tran("switching-quality").replace("%q",this.quality.name)),-1,void 0,"switch-quality"),this.events.trigger("quality_start",this.quality),this.on("canplay",function(){if(A.prevVideo){if(A.video.currentTime!==A.prevVideo.currentTime)return void A.seek(A.prevVideo.currentTime);A.template.videoWrap.removeChild(A.prevVideo),A.video.classList.add("dplayer-video-current"),T||A.video.play(),A.prevVideo=null,A.notice("".concat(A.tran("switched-quality").replace("%q",A.quality.name)),void 0,void 0,"switch-quality"),A.switchingQuality=!1,A.events.trigger("quality_end")}}),this.on("error",function(){A.video.error&&A.prevVideo&&(A.template.videoWrap.removeChild(A.video),A.video=A.prevVideo,T||A.video.play(),A.qualityIndex=A.prevIndex,A.quality=A.options.video.quality[A.qualityIndex],A.noticeTime=setTimeout(function(){A.template.notice.style.opacity=0,A.events.trigger("notice_hide")},3e3),A.prevVideo=null,A.switchingQuality=!1)})}}},{key:"notice",value:function(m){var A,T,L,I=arguments.length>1&&arguments[1]!==void 0?arguments[1]:2e3,B=arguments.length>2&&arguments[2]!==void 0?arguments[2]:.8,N=arguments.length>3?arguments[3]:void 0;if(N&&((A=document.getElementById("dplayer-notice-".concat(N)))&&(A.innerHTML=m),this.noticeList[N]&&(clearTimeout(this.noticeList[N]),this.noticeList[N]=null)),!A){var X=yn.NewNotice(m,B,N);this.template.noticeList.appendChild(X),A=X}this.events.trigger("notice_show",A),I>0&&(this.noticeList[N]=setTimeout((T=A,L=this,function(){T.addEventListener("animationend",function(){L.template.noticeList.removeChild(T)}),T.classList.add("remove-notice"),L.events.trigger("notice_hide"),L.noticeList[N]=null}),I))}},{key:"resize",value:function(){this.danmaku&&this.danmaku.resize(),this.controller.thumbnails&&this.controller.thumbnails.resize(160,this.video.videoHeight/this.video.videoWidth*160,this.template.barWrap.offsetWidth),this.events.trigger("resize")}},{key:"speed",value:function(m){this.video.playbackRate=m}},{key:"destroy",value:function(){Xt.splice(Xt.indexOf(this),1),this.pause(),document.removeEventListener("click",this.docClickFun,!0),this.container.removeEventListener("click",this.containerClickFun,!0),this.fullScreen.destroy(),this.hotkey.destroy(),this.contextmenu.destroy(),this.controller.destroy(),this.timer.destroy(),this.video.src="",this.container.innerHTML="",this.events.trigger("destroy")}}],f=[{key:"version",get:function(){return"1.27.1"}}],x&&Ri(v.prototype,x),f&&Ri(v,f),Object.defineProperty(v,"prototype",{writable:!1}),h}();const Zt=Ga;console.log(`
`.concat(" %c DPlayer v","1.27.1"," ").concat("v1.27.0-12-g4f61091"," %c https://dplayer.diygod.dev ",`
`,`
`),"color: #fadfa3; background: #030307; padding:5px 0;","background: #fadfa3; padding:5px 0;");const Fr=Zt})(),Nr.default})())},67631:function(Ni){(function Ui(nr){(function(rr,Ke){Ni.exports=Ke()})(this,function(){"use strict";function rr(s,a){var r=Object.keys(s);if(Object.getOwnPropertySymbols){var e=Object.getOwnPropertySymbols(s);a&&(e=e.filter(function(t){return Object.getOwnPropertyDescriptor(s,t).enumerable})),r.push.apply(r,e)}return r}function Ke(s){for(var a=1;a<arguments.length;a++){var r=arguments[a]!=null?arguments[a]:{};a%2?rr(Object(r),!0).forEach(function(e){Nr(s,e,r[e])}):Object.getOwnPropertyDescriptors?Object.defineProperties(s,Object.getOwnPropertyDescriptors(r)):rr(Object(r)).forEach(function(e){Object.defineProperty(s,e,Object.getOwnPropertyDescriptor(r,e))})}return s}function tn(s,a){for(var r=0;r<a.length;r++){var e=a[r];e.enumerable=e.enumerable||!1,e.configurable=!0,"value"in e&&(e.writable=!0),Object.defineProperty(s,Se(e.key),e)}}function Q(s,a,r){return a&&tn(s.prototype,a),r&&tn(s,r),Object.defineProperty(s,"prototype",{writable:!1}),s}function Nr(s,a,r){return a=Se(a),a in s?Object.defineProperty(s,a,{value:r,enumerable:!0,configurable:!0,writable:!0}):s[a]=r,s}function q(){return q=Object.assign?Object.assign.bind():function(s){for(var a=1;a<arguments.length;a++){var r=arguments[a];for(var e in r)Object.prototype.hasOwnProperty.call(r,e)&&(s[e]=r[e])}return s},q.apply(this,arguments)}function j(s,a){s.prototype=Object.create(a.prototype),s.prototype.constructor=s,le(s,a)}function Z(s){return Z=Object.setPrototypeOf?Object.getPrototypeOf.bind():function(r){return r.__proto__||Object.getPrototypeOf(r)},Z(s)}function le(s,a){return le=Object.setPrototypeOf?Object.setPrototypeOf.bind():function(e,t){return e.__proto__=t,e},le(s,a)}function ce(){if(typeof Reflect=="undefined"||!Reflect.construct||Reflect.construct.sham)return!1;if(typeof Proxy=="function")return!0;try{return Boolean.prototype.valueOf.call(Reflect.construct(Boolean,[],function(){})),!0}catch(s){return!1}}function ee(s,a,r){return ce()?ee=Reflect.construct.bind():ee=function(t,n,i){var o=[null];o.push.apply(o,n);var l=Function.bind.apply(t,o),d=new l;return i&&le(d,i.prototype),d},ee.apply(null,arguments)}function R(s){return Function.toString.call(s).indexOf("[native code]")!==-1}function ie(s){var a=typeof Map=="function"?new Map:void 0;return ie=function(e){if(e===null||!R(e))return e;if(typeof e!="function")throw new TypeError("Super expression must either be null or a function");if(typeof a!="undefined"){if(a.has(e))return a.get(e);a.set(e,t)}function t(){return ee(e,arguments,Z(this).constructor)}return t.prototype=Object.create(e.prototype,{constructor:{value:t,enumerable:!1,writable:!0,configurable:!0}}),le(t,e)},ie(s)}function se(s){if(s===void 0)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return s}function V(s,a){if(!!s){if(typeof s=="string")return ue(s,a);var r=Object.prototype.toString.call(s).slice(8,-1);if(r==="Object"&&s.constructor&&(r=s.constructor.name),r==="Map"||r==="Set")return Array.from(s);if(r==="Arguments"||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(r))return ue(s,a)}}function ue(s,a){(a==null||a>s.length)&&(a=s.length);for(var r=0,e=new Array(a);r<a;r++)e[r]=s[r];return e}function ve(s,a){var r=typeof Symbol!="undefined"&&s[Symbol.iterator]||s["@@iterator"];if(r)return(r=r.call(s)).next.bind(r);if(Array.isArray(s)||(r=V(s))||a&&s&&typeof s.length=="number"){r&&(s=r);var e=0;return function(){return e>=s.length?{done:!0}:{done:!1,value:s[e++]}}}throw new TypeError(`Invalid attempt to iterate non-iterable instance.
In order to be iterable, non-array objects must have a [Symbol.iterator]() method.`)}function de(s,a){if(typeof s!="object"||s===null)return s;var r=s[Symbol.toPrimitive];if(r!==void 0){var e=r.call(s,a||"default");if(typeof e!="object")return e;throw new TypeError("@@toPrimitive must return a primitive value.")}return(a==="string"?String:Number)(s)}function Se(s){var a=de(s,"string");return typeof a=="symbol"?a:String(a)}var ge={},st={get exports(){return ge},set exports(s){ge=s}};(function(s,a){(function(r){var e=/^(?=((?:[a-zA-Z0-9+\-.]+:)?))\1(?=((?:\/\/[^\/?#]*)?))\2(?=((?:(?:[^?#\/]*\/)*[^;?#\/]*)?))\3((?:;[^?#]*)?)(\?[^#]*)?(#[^]*)?$/,t=/^(?=([^\/?#]*))\1([^]*)$/,n=/(?:\/|^)\.(?=\/)/g,i=/(?:\/|^)\.\.\/(?!\.\.\/)[^\/]*(?=\/)/g,o={buildAbsoluteURL:function(l,d,u){if(u=u||{},l=l.trim(),d=d.trim(),!d){if(!u.alwaysNormalize)return l;var c=o.parseURL(l);if(!c)throw new Error("Error trying to parse base URL.");return c.path=o.normalizePath(c.path),o.buildURLFromParts(c)}var p=o.parseURL(d);if(!p)throw new Error("Error trying to parse relative URL.");if(p.scheme)return u.alwaysNormalize?(p.path=o.normalizePath(p.path),o.buildURLFromParts(p)):d;var g=o.parseURL(l);if(!g)throw new Error("Error trying to parse base URL.");if(!g.netLoc&&g.path&&g.path[0]!=="/"){var y=t.exec(g.path);g.netLoc=y[1],g.path=y[2]}g.netLoc&&!g.path&&(g.path="/");var b={scheme:g.scheme,netLoc:p.netLoc,path:null,params:p.params,query:p.query,fragment:p.fragment};if(!p.netLoc&&(b.netLoc=g.netLoc,p.path[0]!=="/"))if(!p.path)b.path=g.path,p.params||(b.params=g.params,p.query||(b.query=g.query));else{var E=g.path,C=E.substring(0,E.lastIndexOf("/")+1)+p.path;b.path=o.normalizePath(C)}return b.path===null&&(b.path=u.alwaysNormalize?o.normalizePath(p.path):p.path),o.buildURLFromParts(b)},parseURL:function(l){var d=e.exec(l);return d?{scheme:d[1]||"",netLoc:d[2]||"",path:d[3]||"",params:d[4]||"",query:d[5]||"",fragment:d[6]||""}:null},normalizePath:function(l){for(l=l.split("").reverse().join("").replace(n,"");l.length!==(l=l.replace(i,"")).length;);return l.split("").reverse().join("")},buildURLFromParts:function(l){return l.scheme+l.netLoc+l.path+l.params+l.query+l.fragment}};s.exports=o})()})(st);var oe=Number.isFinite||function(s){return typeof s=="number"&&isFinite(s)},S=function(s){return s.MEDIA_ATTACHING="hlsMediaAttaching",s.MEDIA_ATTACHED="hlsMediaAttached",s.MEDIA_DETACHING="hlsMediaDetaching",s.MEDIA_DETACHED="hlsMediaDetached",s.BUFFER_RESET="hlsBufferReset",s.BUFFER_CODECS="hlsBufferCodecs",s.BUFFER_CREATED="hlsBufferCreated",s.BUFFER_APPENDING="hlsBufferAppending",s.BUFFER_APPENDED="hlsBufferAppended",s.BUFFER_EOS="hlsBufferEos",s.BUFFER_FLUSHING="hlsBufferFlushing",s.BUFFER_FLUSHED="hlsBufferFlushed",s.MANIFEST_LOADING="hlsManifestLoading",s.MANIFEST_LOADED="hlsManifestLoaded",s.MANIFEST_PARSED="hlsManifestParsed",s.LEVEL_SWITCHING="hlsLevelSwitching",s.LEVEL_SWITCHED="hlsLevelSwitched",s.LEVEL_LOADING="hlsLevelLoading",s.LEVEL_LOADED="hlsLevelLoaded",s.LEVEL_UPDATED="hlsLevelUpdated",s.LEVEL_PTS_UPDATED="hlsLevelPtsUpdated",s.LEVELS_UPDATED="hlsLevelsUpdated",s.AUDIO_TRACKS_UPDATED="hlsAudioTracksUpdated",s.AUDIO_TRACK_SWITCHING="hlsAudioTrackSwitching",s.AUDIO_TRACK_SWITCHED="hlsAudioTrackSwitched",s.AUDIO_TRACK_LOADING="hlsAudioTrackLoading",s.AUDIO_TRACK_LOADED="hlsAudioTrackLoaded",s.SUBTITLE_TRACKS_UPDATED="hlsSubtitleTracksUpdated",s.SUBTITLE_TRACKS_CLEARED="hlsSubtitleTracksCleared",s.SUBTITLE_TRACK_SWITCH="hlsSubtitleTrackSwitch",s.SUBTITLE_TRACK_LOADING="hlsSubtitleTrackLoading",s.SUBTITLE_TRACK_LOADED="hlsSubtitleTrackLoaded",s.SUBTITLE_FRAG_PROCESSED="hlsSubtitleFragProcessed",s.CUES_PARSED="hlsCuesParsed",s.NON_NATIVE_TEXT_TRACKS_FOUND="hlsNonNativeTextTracksFound",s.INIT_PTS_FOUND="hlsInitPtsFound",s.FRAG_LOADING="hlsFragLoading",s.FRAG_LOAD_EMERGENCY_ABORTED="hlsFragLoadEmergencyAborted",s.FRAG_LOADED="hlsFragLoaded",s.FRAG_DECRYPTED="hlsFragDecrypted",s.FRAG_PARSING_INIT_SEGMENT="hlsFragParsingInitSegment",s.FRAG_PARSING_USERDATA="hlsFragParsingUserdata",s.FRAG_PARSING_METADATA="hlsFragParsingMetadata",s.FRAG_PARSED="hlsFragParsed",s.FRAG_BUFFERED="hlsFragBuffered",s.FRAG_CHANGED="hlsFragChanged",s.FPS_DROP="hlsFpsDrop",s.FPS_DROP_LEVEL_CAPPING="hlsFpsDropLevelCapping",s.ERROR="hlsError",s.DESTROYING="hlsDestroying",s.KEY_LOADING="hlsKeyLoading",s.KEY_LOADED="hlsKeyLoaded",s.LIVE_BACK_BUFFER_REACHED="hlsLiveBackBufferReached",s.BACK_BUFFER_REACHED="hlsBackBufferReached",s}({}),fe=function(s){return s.NETWORK_ERROR="networkError",s.MEDIA_ERROR="mediaError",s.KEY_SYSTEM_ERROR="keySystemError",s.MUX_ERROR="muxError",s.OTHER_ERROR="otherError",s}({}),U=function(s){return s.KEY_SYSTEM_NO_KEYS="keySystemNoKeys",s.KEY_SYSTEM_NO_ACCESS="keySystemNoAccess",s.KEY_SYSTEM_NO_SESSION="keySystemNoSession",s.KEY_SYSTEM_NO_CONFIGURED_LICENSE="keySystemNoConfiguredLicense",s.KEY_SYSTEM_LICENSE_REQUEST_FAILED="keySystemLicenseRequestFailed",s.KEY_SYSTEM_SERVER_CERTIFICATE_REQUEST_FAILED="keySystemServerCertificateRequestFailed",s.KEY_SYSTEM_SERVER_CERTIFICATE_UPDATE_FAILED="keySystemServerCertificateUpdateFailed",s.KEY_SYSTEM_SESSION_UPDATE_FAILED="keySystemSessionUpdateFailed",s.KEY_SYSTEM_STATUS_OUTPUT_RESTRICTED="keySystemStatusOutputRestricted",s.KEY_SYSTEM_STATUS_INTERNAL_ERROR="keySystemStatusInternalError",s.MANIFEST_LOAD_ERROR="manifestLoadError",s.MANIFEST_LOAD_TIMEOUT="manifestLoadTimeOut",s.MANIFEST_PARSING_ERROR="manifestParsingError",s.MANIFEST_INCOMPATIBLE_CODECS_ERROR="manifestIncompatibleCodecsError",s.LEVEL_EMPTY_ERROR="levelEmptyError",s.LEVEL_LOAD_ERROR="levelLoadError",s.LEVEL_LOAD_TIMEOUT="levelLoadTimeOut",s.LEVEL_PARSING_ERROR="levelParsingError",s.LEVEL_SWITCH_ERROR="levelSwitchError",s.AUDIO_TRACK_LOAD_ERROR="audioTrackLoadError",s.AUDIO_TRACK_LOAD_TIMEOUT="audioTrackLoadTimeOut",s.SUBTITLE_LOAD_ERROR="subtitleTrackLoadError",s.SUBTITLE_TRACK_LOAD_TIMEOUT="subtitleTrackLoadTimeOut",s.FRAG_LOAD_ERROR="fragLoadError",s.FRAG_LOAD_TIMEOUT="fragLoadTimeOut",s.FRAG_DECRYPT_ERROR="fragDecryptError",s.FRAG_PARSING_ERROR="fragParsingError",s.FRAG_GAP="fragGap",s.REMUX_ALLOC_ERROR="remuxAllocError",s.KEY_LOAD_ERROR="keyLoadError",s.KEY_LOAD_TIMEOUT="keyLoadTimeOut",s.BUFFER_ADD_CODEC_ERROR="bufferAddCodecError",s.BUFFER_INCOMPATIBLE_CODECS_ERROR="bufferIncompatibleCodecsError",s.BUFFER_APPEND_ERROR="bufferAppendError",s.BUFFER_APPENDING_ERROR="bufferAppendingError",s.BUFFER_STALLED_ERROR="bufferStalledError",s.BUFFER_FULL_ERROR="bufferFullError",s.BUFFER_SEEK_OVER_HOLE="bufferSeekOverHole",s.BUFFER_NUDGE_ON_STALL="bufferNudgeOnStall",s.INTERNAL_EXCEPTION="internalException",s.INTERNAL_ABORTED="aborted",s.UNKNOWN="unknown",s}({}),ke=function(){},In={trace:ke,debug:ke,log:ke,warn:ke,info:ke,error:ke},Tt=In;function nn(s){var a=self.console[s];return a?a.bind(self.console,"["+s+"] >"):ke}function ir(s){for(var a=arguments.length,r=new Array(a>1?a-1:0),e=1;e<a;e++)r[e-1]=arguments[e];r.forEach(function(t){Tt[t]=s[t]?s[t].bind(s):nn(t)})}function Ur(s,a){if(self.console&&s===!0||typeof s=="object"){ir(s,"debug","log","info","warn","error");try{Tt.log('Debug logs enabled for "'+a+'" in hls.js version 1.4.0')}catch(r){Tt=In}}else Tt=In}var P=Tt,Ki=/^(\d+)x(\d+)$/,rn=/(.+?)=(".*?"|.*?)(?:,|$)/g,ne=function(){function s(r){typeof r=="string"&&(r=s.parseAttrList(r));for(var e in r)r.hasOwnProperty(e)&&(e.substring(0,2)==="X-"&&(this.clientAttrs=this.clientAttrs||[],this.clientAttrs.push(e)),this[e]=r[e])}var a=s.prototype;return a.decimalInteger=function(e){var t=parseInt(this[e],10);return t>Number.MAX_SAFE_INTEGER?Infinity:t},a.hexadecimalInteger=function(e){if(this[e]){var t=(this[e]||"0x").slice(2);t=(t.length&1?"0":"")+t;for(var n=new Uint8Array(t.length/2),i=0;i<t.length/2;i++)n[i]=parseInt(t.slice(i*2,i*2+2),16);return n}else return null},a.hexadecimalIntegerAsNumber=function(e){var t=parseInt(this[e],16);return t>Number.MAX_SAFE_INTEGER?Infinity:t},a.decimalFloatingPoint=function(e){return parseFloat(this[e])},a.optionalFloat=function(e,t){var n=this[e];return n?parseFloat(n):t},a.enumeratedString=function(e){return this[e]},a.bool=function(e){return this[e]==="YES"},a.decimalResolution=function(e){var t=Ki.exec(this[e]);if(t!==null)return{width:parseInt(t[1],10),height:parseInt(t[2],10)}},s.parseAttrList=function(e){var t,n={},i='"';for(rn.lastIndex=0;(t=rn.exec(e))!==null;){var o=t[2];o.indexOf(i)===0&&o.lastIndexOf(i)===o.length-1&&(o=o.slice(1,-1));var l=t[1].trim();n[l]=o}return n},s}();function Kr(s){return s!=="ID"&&s!=="CLASS"&&s!=="START-DATE"&&s!=="DURATION"&&s!=="END-DATE"&&s!=="END-ON-NEXT"}function an(s){return s==="SCTE35-OUT"||s==="SCTE35-IN"}var Rn=function(){function s(a,r){if(this.attr=void 0,this._startDate=void 0,this._endDate=void 0,this._badValueForSameId=void 0,r){var e=r.attr;for(var t in e)if(Object.prototype.hasOwnProperty.call(a,t)&&a[t]!==e[t]){P.warn('DATERANGE tag attribute: "'+t+'" does not match for tags with ID: "'+a.ID+'"'),this._badValueForSameId=t;break}a=q(new ne({}),e,a)}if(this.attr=a,this._startDate=new Date(a["START-DATE"]),"END-DATE"in this.attr){var n=new Date(this.attr["END-DATE"]);oe(n.getTime())&&(this._endDate=n)}}return Q(s,[{key:"id",get:function(){return this.attr.ID}},{key:"class",get:function(){return this.attr.CLASS}},{key:"startDate",get:function(){return this._startDate}},{key:"endDate",get:function(){if(this._endDate)return this._endDate;var r=this.duration;return r!==null?new Date(this._startDate.getTime()+r*1e3):null}},{key:"duration",get:function(){if("DURATION"in this.attr){var r=this.attr.decimalFloatingPoint("DURATION");if(oe(r))return r}else if(this._endDate)return(this._endDate.getTime()-this._startDate.getTime())/1e3;return null}},{key:"plannedDuration",get:function(){return"PLANNED-DURATION"in this.attr?this.attr.decimalFloatingPoint("PLANNED-DURATION"):null}},{key:"endOnNext",get:function(){return this.attr.bool("END-ON-NEXT")}},{key:"isValid",get:function(){return!!this.id&&!this._badValueForSameId&&oe(this.startDate.getTime())&&(this.duration===null||this.duration>=0)&&(!this.endOnNext||!!this.class)}}]),s}(),on=function(){this.aborted=!1,this.loaded=0,this.retry=0,this.total=0,this.chunkCount=0,this.bwEstimate=0,this.loading={start:0,first:0,end:0},this.parsing={start:0,end:0},this.buffering={start:0,first:0,end:0}},Ce={AUDIO:"audio",VIDEO:"video",AUDIOVIDEO:"audiovideo"},Dn=function(){function s(r){var e;this._byteRange=null,this._url=null,this.baseurl=void 0,this.relurl=void 0,this.elementaryStreams=(e={},e[Ce.AUDIO]=null,e[Ce.VIDEO]=null,e[Ce.AUDIOVIDEO]=null,e),this.baseurl=r}var a=s.prototype;return a.setByteRange=function(e,t){var n=e.split("@",2),i=[];n.length===1?i[0]=t?t.byteRangeEndOffset:0:i[0]=parseInt(n[1]),i[1]=parseInt(n[0])+i[0],this._byteRange=i},Q(s,[{key:"byteRange",get:function(){return this._byteRange?this._byteRange:[]}},{key:"byteRangeStartOffset",get:function(){return this.byteRange[0]}},{key:"byteRangeEndOffset",get:function(){return this.byteRange[1]}},{key:"url",get:function(){return!this._url&&this.baseurl&&this.relurl&&(this._url=ge.buildAbsoluteURL(this.baseurl,this.relurl,{alwaysNormalize:!0})),this._url||""},set:function(e){this._url=e}}]),s}(),nt=function(s){j(a,s);function a(e,t){var n;return n=s.call(this,t)||this,n._decryptdata=null,n.rawProgramDateTime=null,n.programDateTime=null,n.tagList=[],n.duration=0,n.sn=0,n.levelkeys=void 0,n.type=void 0,n.loader=null,n.keyLoader=null,n.level=-1,n.cc=0,n.startPTS=void 0,n.endPTS=void 0,n.startDTS=void 0,n.endDTS=void 0,n.start=0,n.deltaPTS=void 0,n.maxStartPTS=void 0,n.minEndPTS=void 0,n.stats=new on,n.urlId=0,n.data=void 0,n.bitrateTest=!1,n.title=null,n.initSegment=null,n.endList=void 0,n.gap=void 0,n.type=e,n}var r=a.prototype;return r.setKeyFormat=function(t){if(this.levelkeys){var n=this.levelkeys[t];n&&!this._decryptdata&&(this._decryptdata=n.getDecryptData(this.sn))}},r.abortRequests=function(){var t,n;(t=this.loader)==null||t.abort(),(n=this.keyLoader)==null||n.abort()},r.setElementaryStreamInfo=function(t,n,i,o,l,d){d===void 0&&(d=!1);var u=this.elementaryStreams,c=u[t];if(!c){u[t]={startPTS:n,endPTS:i,startDTS:o,endDTS:l,partial:d};return}c.startPTS=Math.min(c.startPTS,n),c.endPTS=Math.max(c.endPTS,i),c.startDTS=Math.min(c.startDTS,o),c.endDTS=Math.max(c.endDTS,l)},r.clearElementaryStreamInfo=function(){var t=this.elementaryStreams;t[Ce.AUDIO]=null,t[Ce.VIDEO]=null,t[Ce.AUDIOVIDEO]=null},Q(a,[{key:"decryptdata",get:function(){var t=this.levelkeys;if(!t&&!this._decryptdata)return null;if(!this._decryptdata&&this.levelkeys&&!this.levelkeys.NONE){var n=this.levelkeys.identity;if(n)this._decryptdata=n.getDecryptData(this.sn);else{var i=Object.keys(this.levelkeys);if(i.length===1)return this._decryptdata=this.levelkeys[i[0]].getDecryptData(this.sn)}}return this._decryptdata}},{key:"end",get:function(){return this.start+this.duration}},{key:"endProgramDateTime",get:function(){if(this.programDateTime===null||!oe(this.programDateTime))return null;var t=oe(this.duration)?this.duration:0;return this.programDateTime+t*1e3}},{key:"encrypted",get:function(){var t;if((t=this._decryptdata)!=null&&t.encrypted)return!0;if(this.levelkeys){var n=Object.keys(this.levelkeys),i=n.length;if(i>1||i===1&&this.levelkeys[n[0]].encrypted)return!0}return!1}}]),a}(Dn),_n=function(s){j(a,s);function a(r,e,t,n,i){var o;o=s.call(this,t)||this,o.fragOffset=0,o.duration=0,o.gap=!1,o.independent=!1,o.relurl=void 0,o.fragment=void 0,o.index=void 0,o.stats=new on,o.duration=r.decimalFloatingPoint("DURATION"),o.gap=r.bool("GAP"),o.independent=r.bool("INDEPENDENT"),o.relurl=r.enumeratedString("URI"),o.fragment=e,o.index=n;var l=r.enumeratedString("BYTERANGE");return l&&o.setByteRange(l,i),i&&(o.fragOffset=i.fragOffset+i.duration),o}return Q(a,[{key:"start",get:function(){return this.fragment.start+this.fragOffset}},{key:"end",get:function(){return this.start+this.duration}},{key:"loaded",get:function(){var e=this.elementaryStreams;return!!(e.audio||e.video||e.audiovideo)}}]),a}(Dn),Ft=10,sn=function(){function s(r){this.PTSKnown=!1,this.alignedSliding=!1,this.averagetargetduration=void 0,this.endCC=0,this.endSN=0,this.fragments=void 0,this.fragmentHint=void 0,this.partList=null,this.dateRanges=void 0,this.live=!0,this.ageHeader=0,this.advancedDateTime=void 0,this.updated=!0,this.advanced=!0,this.availabilityDelay=void 0,this.misses=0,this.startCC=0,this.startSN=0,this.startTimeOffset=null,this.targetduration=0,this.totalduration=0,this.type=null,this.url=void 0,this.m3u8="",this.version=null,this.canBlockReload=!1,this.canSkipUntil=0,this.canSkipDateRanges=!1,this.skippedSegments=0,this.recentlyRemovedDateranges=void 0,this.partHoldBack=0,this.holdBack=0,this.partTarget=0,this.preloadHint=void 0,this.renditionReports=void 0,this.tuneInGoal=0,this.deltaUpdateFailed=void 0,this.driftStartTime=0,this.driftEndTime=0,this.driftStart=0,this.driftEnd=0,this.encryptedFragments=void 0,this.playlistParsingError=null,this.variableList=null,this.hasVariableRefs=!1,this.fragments=[],this.encryptedFragments=[],this.dateRanges={},this.url=r}var a=s.prototype;return a.reloaded=function(e){if(!e){this.advanced=!0,this.updated=!0;return}var t=this.lastPartSn-e.lastPartSn,n=this.lastPartIndex-e.lastPartIndex;this.updated=this.endSN!==e.endSN||!!n||!!t,this.advanced=this.endSN>e.endSN||t>0||t===0&&n>0,this.updated||this.advanced?this.misses=Math.floor(e.misses*.6):this.misses=e.misses+1,this.availabilityDelay=e.availabilityDelay},Q(s,[{key:"hasProgramDateTime",get:function(){return this.fragments.length?oe(this.fragments[this.fragments.length-1].programDateTime):!1}},{key:"levelTargetDuration",get:function(){return this.averagetargetduration||this.targetduration||Ft}},{key:"drift",get:function(){var e=this.driftEndTime-this.driftStartTime;if(e>0){var t=this.driftEnd-this.driftStart;return t*1e3/e}return 1}},{key:"edge",get:function(){return this.partEnd||this.fragmentEnd}},{key:"partEnd",get:function(){var e;return(e=this.partList)!=null&&e.length?this.partList[this.partList.length-1].end:this.fragmentEnd}},{key:"fragmentEnd",get:function(){var e;return(e=this.fragments)!=null&&e.length?this.fragments[this.fragments.length-1].end:0}},{key:"age",get:function(){return this.advancedDateTime?Math.max(Date.now()-this.advancedDateTime,0)/1e3:0}},{key:"lastPartIndex",get:function(){var e;return(e=this.partList)!=null&&e.length?this.partList[this.partList.length-1].index:-1}},{key:"lastPartSn",get:function(){var e;return(e=this.partList)!=null&&e.length?this.partList[this.partList.length-1].fragment.sn:this.endSN}}]),s}();function Pn(s){return Uint8Array.from(atob(s),function(a){return a.charCodeAt(0)})}function Gi(s){var a=Bn(s).subarray(0,16),r=new Uint8Array(16);return r.set(a,16-a.length),r}function St(s){var a=function(e,t,n){var i=e[t];e[t]=e[n],e[n]=i};a(s,0,3),a(s,1,2),a(s,4,5),a(s,6,7)}function Gr(s){var a=s.split(":"),r=null;if(a[0]==="data"&&a.length===2){var e=a[1].split(";"),t=e[e.length-1].split(",");if(t.length===2){var n=t[0]==="base64",i=t[1];n?(e.splice(-1,1),r=Pn(i)):r=Gi(i)}}return r}function Bn(s){return Uint8Array.from(unescape(encodeURIComponent(s)),function(a){return a.charCodeAt(0)})}var De={CLEARKEY:"org.w3.clearkey",FAIRPLAY:"com.apple.fps",PLAYREADY:"com.microsoft.playready",WIDEVINE:"com.widevine.alpha"},We={CLEARKEY:"org.w3.clearkey",FAIRPLAY:"com.apple.streamingkeydelivery",PLAYREADY:"com.microsoft.playready",WIDEVINE:"urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed"};function Hr(s){switch(s){case We.FAIRPLAY:return De.FAIRPLAY;case We.PLAYREADY:return De.PLAYREADY;case We.WIDEVINE:return De.WIDEVINE;case We.CLEARKEY:return De.CLEARKEY}}var qr={WIDEVINE:"edef8ba979d64acea3c827dcd51d21ed"};function Hi(s){if(s===qr.WIDEVINE)return De.WIDEVINE}function Yr(s){switch(s){case De.FAIRPLAY:return We.FAIRPLAY;case De.PLAYREADY:return We.PLAYREADY;case De.WIDEVINE:return We.WIDEVINE;case De.CLEARKEY:return We.CLEARKEY}}function Ct(s){var a=s.drmSystems,r=s.widevineLicenseUrl,e=a?[De.FAIRPLAY,De.WIDEVINE,De.PLAYREADY,De.CLEARKEY].filter(function(t){return!!a[t]}):[];return!e[De.WIDEVINE]&&r&&e.push(De.WIDEVINE),e}var ar=function(){return typeof self!="undefined"&&self.navigator&&self.navigator.requestMediaKeySystemAccess?self.navigator.requestMediaKeySystemAccess.bind(self.navigator):null}();function or(s,a,r,e){var t;switch(s){case De.FAIRPLAY:t=["cenc","sinf"];break;case De.WIDEVINE:case De.PLAYREADY:t=["cenc"];break;case De.CLEARKEY:t=["cenc","keyids"];break;default:throw new Error("Unknown key-system: "+s)}return Wr(t,a,r,e)}function Wr(s,a,r,e){var t={initDataTypes:s,persistentState:e.persistentState||"not-allowed",distinctiveIdentifier:e.distinctiveIdentifier||"not-allowed",sessionTypes:e.sessionTypes||[e.sessionType||"temporary"],audioCapabilities:a.map(function(n){return{contentType:'audio/mp4; codecs="'+n+'"',robustness:e.audioRobustness||"",encryptionScheme:e.audioEncryptionScheme||null}}),videoCapabilities:r.map(function(n){return{contentType:'video/mp4; codecs="'+n+'"',robustness:e.videoRobustness||"",encryptionScheme:e.videoEncryptionScheme||null}})};return[t]}function mt(s,a,r){return Uint8Array.prototype.slice?s.slice(a,r):new Uint8Array(Array.prototype.slice.call(s,a,r))}var On=function(a,r){return r+10<=a.length&&a[r]===73&&a[r+1]===68&&a[r+2]===51&&a[r+3]<255&&a[r+4]<255&&a[r+6]<128&&a[r+7]<128&&a[r+8]<128&&a[r+9]<128},zr=function(a,r){return r+10<=a.length&&a[r]===51&&a[r+1]===68&&a[r+2]===73&&a[r+3]<255&&a[r+4]<255&&a[r+6]<128&&a[r+7]<128&&a[r+8]<128&&a[r+9]<128},Mn=function(a,r){for(var e=r,t=0;On(a,r);){t+=10;var n=ln(a,r+6);t+=n,zr(a,r+10)&&(t+=10),r+=t}if(t>0)return a.subarray(e,e+t)},ln=function(a,r){var e=0;return e=(a[r]&127)<<21,e|=(a[r+1]&127)<<14,e|=(a[r+2]&127)<<7,e|=a[r+3]&127,e},qi=function(a,r){return On(a,r)&&ln(a,r+6)+10<=a.length-r},Vr=function(a){for(var r=sr(a),e=0;e<r.length;e++){var t=r[e];if(K(t))return dr(t)}},K=function(a){return a&&a.key==="PRIV"&&a.info==="com.apple.streaming.transportStreamTimestamp"},Nt=function(a){var r=String.fromCharCode(a[0],a[1],a[2],a[3]),e=ln(a,4),t=10;return{type:r,size:e,data:a.subarray(t,t+e)}},sr=function(a){for(var r=0,e=[];On(a,r);){var t=ln(a,r+6);r+=10;for(var n=r+t;r+8<n;){var i=Nt(a.subarray(r)),o=jr(i);o&&e.push(o),r+=i.size+10}zr(a,r)&&(r+=10)}return e},jr=function(a){return a.type==="PRIV"?Le(a):a.type[0]==="W"?lr(a):Yi(a)},Le=function(a){if(!(a.size<2)){var r=rt(a.data,!0),e=new Uint8Array(a.data.subarray(r.length+1));return{key:a.type,info:r,data:e.buffer}}},Yi=function(a){if(!(a.size<2)){if(a.type==="TXXX"){var r=1,e=rt(a.data.subarray(r),!0);r+=e.length+1;var t=rt(a.data.subarray(r));return{key:a.type,info:e,data:t}}var n=rt(a.data.subarray(1));return{key:a.type,data:n}}},lr=function(a){if(a.type==="WXXX"){if(a.size<2)return;var r=1,e=rt(a.data.subarray(r),!0);r+=e.length+1;var t=rt(a.data.subarray(r));return{key:a.type,info:e,data:t}}var n=rt(a.data);return{key:a.type,data:n}},dr=function(a){if(a.data.byteLength===8){var r=new Uint8Array(a.data),e=r[3]&1,t=(r[4]<<23)+(r[5]<<15)+(r[6]<<7)+r[7];return t/=45,e&&(t+=4772185884e-2),Math.round(t)}},rt=function(a,r){r===void 0&&(r=!1);var e=Wi();if(e){var t=e.decode(a);if(r){var n=t.indexOf("\0");return n!==-1?t.substring(0,n):t}return t.replace(/\0/g,"")}for(var i=a.length,o,l,d,u="",c=0;c<i;){if(o=a[c++],o===0&&r)return u;if(o===0||o===3)continue;switch(o>>4){case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:u+=String.fromCharCode(o);break;case 12:case 13:l=a[c++],u+=String.fromCharCode((o&31)<<6|l&63);break;case 14:l=a[c++],d=a[c++],u+=String.fromCharCode((o&15)<<12|(l&63)<<6|(d&63)<<0);break}}return u},Fn;function Wi(){return!Fn&&typeof self.TextDecoder!="undefined"&&(Fn=new self.TextDecoder("utf-8")),Fn}var Qe={hexDump:function(a){for(var r="",e=0;e<a.length;e++){var t=a[e].toString(16);t.length<2&&(t="0"+t),r+=t}return r}},dn=Math.pow(2,32)-1,Qr=[].push,ur={video:1,audio:2,id3:3,text:4};function Ge(s){return String.fromCharCode.apply(null,s)}function Xr(s,a){var r=s[a]<<8|s[a+1];return r<0?65536+r:r}function me(s,a){var r=Ut(s,a);return r<0?4294967296+r:r}function Ut(s,a){return s[a]<<24|s[a+1]<<16|s[a+2]<<8|s[a+3]}function cr(s,a,r){s[a]=r>>24,s[a+1]=r>>16&255,s[a+2]=r>>8&255,s[a+3]=r&255}function Te(s,a){var r=[];if(!a.length)return r;for(var e=s.byteLength,t=0;t<e;){var n=me(s,t),i=Ge(s.subarray(t+4,t+8)),o=n>1?t+n:e;if(i===a[0])if(a.length===1)r.push(s.subarray(t+8,o));else{var l=Te(s.subarray(t+8,o),a.slice(1));l.length&&Qr.apply(r,l)}t=o}return r}function Zr(s){var a=[],r=s[0],e=8,t=me(s,e);e+=4;var n=0,i=0;r===0?e+=8:e+=16,e+=2;var o=s.length+i,l=Xr(s,e);e+=2;for(var d=0;d<l;d++){var u=e,c=me(s,u);u+=4;var p=c&2147483647,g=(c&2147483648)>>>31;if(g===1)return P.warn("SIDX has hierarchical references (not supported)"),null;var y=me(s,u);u+=4,a.push({referenceSize:p,subsegmentDuration:y,info:{duration:y/t,start:o,end:o+p-1}}),o+=p,u+=4,e=u}return{earliestPresentationTime:n,timescale:t,version:r,referencesCount:l,references:a}}function fr(s){for(var a=[],r=Te(s,["moov","trak"]),e=0;e<r.length;e++){var t=r[e],n=Te(t,["tkhd"])[0];if(n){var i=n[0],o=i===0?12:20,l=me(n,o),d=Te(t,["mdia","mdhd"])[0];if(d){i=d[0],o=i===0?12:20;var u=me(d,o),c=Te(t,["mdia","hdlr"])[0];if(c){var p=Ge(c.subarray(8,12)),g={soun:Ce.AUDIO,vide:Ce.VIDEO}[p];if(g){var y=Te(t,["mdia","minf","stbl","stsd"])[0],b=void 0;y&&(b=Ge(y.subarray(12,16))),a[l]={timescale:u,type:g},a[g]={timescale:u,id:l,codec:b}}}}}}var E=Te(s,["moov","mvex","trex"]);return E.forEach(function(C){var w=me(C,4),k=a[w];k&&(k.default={duration:me(C,12),flags:me(C,20)})}),a}function zi(s,a){if(!s||!a)return s;var r=a.keyId;if(r&&a.isCommonEncryption){var e=Te(s,["moov","trak"]);e.forEach(function(t){var n=Te(t,["mdia","minf","stbl","stsd"])[0],i=n.subarray(8),o=Te(i,["enca"]),l=o.length>0;l||(o=Te(i,["encv"])),o.forEach(function(d){var u=l?d.subarray(28):d.subarray(78),c=Te(u,["sinf"]);c.forEach(function(p){var g=Jr(p);if(g){var y=g.subarray(8,24);y.some(function(b){return b!==0})||(P.log("[eme] Patching keyId in 'enc"+(l?"a":"v")+">sinf>>tenc' box: "+Qe.hexDump(y)+" -> "+Qe.hexDump(r)),g.set(r,8))}})})})}return s}function Jr(s){var a=Te(s,["schm"])[0];if(a){var r=Ge(a.subarray(4,8));if(r==="cbcs"||r==="cenc")return Te(s,["schi","tenc"])[0]}return P.error("[eme] missing 'schm' box"),null}function hr(s,a){return Te(a,["moof","traf"]).reduce(function(r,e){var t=Te(e,["tfdt"])[0],n=t[0],i=Te(e,["tfhd"]).reduce(function(o,l){var d=me(l,4),u=s[d];if(u){var c=me(t,4);if(n===1){if(c===dn)return P.warn("[mp4-demuxer]: Ignoring assumed invalid signed 64-bit track fragment decode time"),o;c*=dn+1,c+=me(t,8)}var p=u.timescale||9e4,g=c/p;if(isFinite(g)&&(o===null||g<o))return g}return o},null);return i!==null&&isFinite(i)&&(r===null||i<r)?i:r},null)}function Vi(s,a){for(var r=0,e=0,t=0,n=Te(s,["moof","traf"]),i=0;i<n.length;i++){var o=n[i],l=Te(o,["tfhd"])[0],d=me(l,4),u=a[d];if(!!u){var c=u.default,p=me(l,0)|(c==null?void 0:c.flags),g=c==null?void 0:c.duration;p&8&&(p&2?g=me(l,12):g=me(l,8));for(var y=u.timescale||9e4,b=Te(o,["trun"]),E=0;E<b.length;E++){if(r=ji(b[E]),!r&&g){var C=me(b[E],4);r=g*C}u.type===Ce.VIDEO?e+=r/y:u.type===Ce.AUDIO&&(t+=r/y)}}}if(e===0&&t===0){for(var w=0,k=Te(s,["sidx"]),D=0;D<k.length;D++){var _=Zr(k[D]);_!=null&&_.references&&(w+=_.references.reduce(function(F,O){return F+O.info.duration||0},0))}return w}return e||t}function ji(s){var a=me(s,0),r=8;a&1&&(r+=4),a&4&&(r+=4);for(var e=0,t=me(s,4),n=0;n<t;n++){if(a&256){var i=me(s,r);e+=i,r+=4}a&512&&(r+=4),a&1024&&(r+=4),a&2048&&(r+=4)}return e}function lt(s,a,r){Te(a,["moof","traf"]).forEach(function(e){Te(e,["tfhd"]).forEach(function(t){var n=me(t,4),i=s[n];if(!!i){var o=i.timescale||9e4;Te(e,["tfdt"]).forEach(function(l){var d=l[0],u=me(l,4);if(d===0)u-=r*o,u=Math.max(u,0),cr(l,4,u);else{u*=Math.pow(2,32),u+=me(l,8),u-=r*o,u=Math.max(u,0);var c=Math.floor(u/(dn+1)),p=Math.floor(u%(dn+1));cr(l,4,c),cr(l,8,p)}})}})})}function $r(s){var a={valid:null,remainder:null},r=Te(s,["moof"]);if(r){if(r.length<2)return a.remainder=s,a}else return a;var e=r[r.length-1];return a.valid=mt(s,0,e.byteOffset-8),a.remainder=mt(s,e.byteOffset-8),a}function kt(s,a){var r=new Uint8Array(s.length+a.length);return r.set(s),r.set(a,s.length),r}function un(s,a){var r=[],e=a.samples,t=a.timescale,n=a.id,i=!1,o=Te(e,["moof"]);return o.map(function(l){var d=l.byteOffset-8,u=Te(l,["traf"]);u.map(function(c){var p=Te(c,["tfdt"]).map(function(g){var y=g[0],b=me(g,4);return y===1&&(b*=Math.pow(2,32),b+=me(g,8)),b/t})[0];return p!==void 0&&(s=p),Te(c,["tfhd"]).map(function(g){var y=me(g,4),b=me(g,0)&16777215,E=(b&1)!=0,C=(b&2)!=0,w=(b&8)!=0,k=0,D=(b&16)!=0,_=0,F=(b&32)!=0,O=8;y===n&&(E&&(O+=8),C&&(O+=4),w&&(k=me(g,O),O+=4),D&&(_=me(g,O),O+=4),F&&(O+=4),a.type==="video"&&(i=pr(a.codec)),Te(c,["trun"]).map(function(M){var z=M[0],H=me(M,0)&16777215,G=(H&1)!=0,te=0,ye=(H&4)!=0,Ae=(H&256)!=0,Ee=0,we=(H&512)!=0,xe=0,_e=(H&1024)!=0,Re=(H&2048)!=0,Be=0,ut=me(M,4),Ne=8;G&&(te=me(M,Ne),Ne+=4),ye&&(Ne+=4);for(var He=te+d,at=0;at<ut;at++){if(Ae?(Ee=me(M,Ne),Ne+=4):Ee=k,we?(xe=me(M,Ne),Ne+=4):xe=_,_e&&(Ne+=4),Re&&(z===0?Be=me(M,Ne):Be=Ut(M,Ne),Ne+=4),a.type===Ce.VIDEO)for(var tt=0;tt<xe;){var ot=me(e,He);if(He+=4,Qi(i,e[He])){var Pt=e.subarray(He,He+ot);Kt(Pt,i?2:1,s+Be/t,r)}He+=ot,tt+=ot+4}s+=Ee/t}}))})})}),r}function pr(s){if(!s)return!1;var a=s.indexOf("."),r=a<0?s:s.substring(0,a);return r==="hvc1"||r==="hev1"||r==="dvh1"||r==="dvhe"}function Qi(s,a){if(s){var r=a>>1&63;return r===39||r===40}else{var e=a&31;return e===6}}function Kt(s,a,r,e){var t=vr(s),n=0;n+=a;for(var i=0,o=0,l=!1,d=0;n<t.length;){i=0;do{if(n>=t.length)break;d=t[n++],i+=d}while(d===255);o=0;do{if(n>=t.length)break;d=t[n++],o+=d}while(d===255);var u=t.length-n;if(!l&&i===4&&n<t.length){l=!0;var c=t[n++];if(c===181){var p=Xr(t,n);if(n+=2,p===49){var g=me(t,n);if(n+=4,g===1195456820){var y=t[n++];if(y===3){var b=t[n++],E=31&b,C=64&b,w=C?2+E*3:0,k=new Uint8Array(w);if(C){k[0]=b;for(var D=1;D<w;D++)k[D]=t[n++]}e.push({type:y,payloadType:i,pts:r,bytes:k})}}}}}else if(i===5&&o<u){if(l=!0,o>16){for(var _=[],F=0;F<16;F++){var O=t[n++].toString(16);_.push(O.length==1?"0"+O:O),(F===3||F===5||F===7||F===9)&&_.push("-")}for(var M=o-16,z=new Uint8Array(M),H=0;H<M;H++)z[H]=t[n++];e.push({payloadType:i,pts:r,uuid:_.join(""),userData:rt(z),userDataBytes:z})}}else if(o<u)n+=o;else if(o>u)break}}function vr(s){for(var a=s.byteLength,r=[],e=1;e<a-2;)s[e]===0&&s[e+1]===0&&s[e+2]===3?(r.push(e+2),e+=2):e++;if(r.length===0)return s;var t=a-r.length,n=new Uint8Array(t),i=0;for(e=0;e<t;i++,e++)i===r[0]&&(i++,r.shift()),n[e]=s[i];return n}function ei(s){var a=s[0],r="",e="",t=0,n=0,i=0,o=0,l=0,d=0;if(a===0){for(;Ge(s.subarray(d,d+1))!=="\0";)r+=Ge(s.subarray(d,d+1)),d+=1;for(r+=Ge(s.subarray(d,d+1)),d+=1;Ge(s.subarray(d,d+1))!=="\0";)e+=Ge(s.subarray(d,d+1)),d+=1;e+=Ge(s.subarray(d,d+1)),d+=1,t=me(s,12),n=me(s,16),o=me(s,20),l=me(s,24),d=28}else if(a===1){d+=4,t=me(s,d),d+=4;var u=me(s,d);d+=4;var c=me(s,d);for(d+=4,i=Math.pow(2,32)*u+c,Number.isSafeInteger(i)||(i=Number.MAX_SAFE_INTEGER,P.warn("Presentation time exceeds safe integer limit and wrapped to max safe integer in parsing emsg box")),o=me(s,d),d+=4,l=me(s,d),d+=4;Ge(s.subarray(d,d+1))!=="\0";)r+=Ge(s.subarray(d,d+1)),d+=1;for(r+=Ge(s.subarray(d,d+1)),d+=1;Ge(s.subarray(d,d+1))!=="\0";)e+=Ge(s.subarray(d,d+1)),d+=1;e+=Ge(s.subarray(d,d+1)),d+=1}var p=s.subarray(d,s.byteLength);return{schemeIdUri:r,value:e,timeScale:t,presentationTime:i,presentationTimeDelta:n,eventDuration:o,id:l,payload:p}}function ti(s){for(var a=arguments.length,r=new Array(a>1?a-1:0),e=1;e<a;e++)r[e-1]=arguments[e];for(var t=r.length,n=8,i=t;i--;)n+=r[i].byteLength;var o=new Uint8Array(n);for(o[0]=n>>24&255,o[1]=n>>16&255,o[2]=n>>8&255,o[3]=n&255,o.set(s,4),i=0,n=8;i<t;i++)o.set(r[i],n),n+=r[i].byteLength;return o}function cn(s,a,r){if(s.byteLength!==16)throw new RangeError("Invalid system id");var e,t;if(a){e=1,t=new Uint8Array(a.length*16);for(var n=0;n<a.length;n++){var i=a[n];if(i.byteLength!==16)throw new RangeError("Invalid key");t.set(i,n*16)}}else e=0,t=new Uint8Array;var o;e>0?(o=new Uint8Array(4),a.length>0&&new DataView(o.buffer).setUint32(0,a.length,!1)):o=new Uint8Array;var l=new Uint8Array(4);return r&&r.byteLength>0&&new DataView(l.buffer).setUint32(0,r.byteLength,!1),ti([112,115,115,104],new Uint8Array([e,0,0,0]),s,o,t,l,r||new Uint8Array)}function Nn(s){if(!(s instanceof ArrayBuffer)||s.byteLength<32)return null;var a={version:0,systemId:"",kids:null,data:null},r=new DataView(s),e=r.getUint32(0);if(s.byteLength!==e&&e>44)return null;var t=r.getUint32(4);if(t!==1886614376||(a.version=r.getUint32(8)>>>24,a.version>1))return null;a.systemId=Qe.hexDump(new Uint8Array(s,12,16));var n=r.getUint32(28);if(a.version===0){if(e-32<n)return null;a.data=new Uint8Array(s,32,n)}else if(a.version===1){a.kids=[];for(var i=0;i<n;i++)a.kids.push(new Uint8Array(s,32+i*16,16))}return a}var fn={},Gt=function(){s.clearKeyUriToKeyIdMap=function(){fn={}};function s(r,e,t,n,i){n===void 0&&(n=[1]),i===void 0&&(i=null),this.uri=void 0,this.method=void 0,this.keyFormat=void 0,this.keyFormatVersions=void 0,this.encrypted=void 0,this.isCommonEncryption=void 0,this.iv=null,this.key=null,this.keyId=null,this.pssh=null,this.method=r,this.uri=e,this.keyFormat=t,this.keyFormatVersions=n,this.iv=i,this.encrypted=r?r!=="NONE":!1,this.isCommonEncryption=this.encrypted&&r!=="AES-128"}var a=s.prototype;return a.isSupported=function(){if(this.method){if(this.method==="AES-128"||this.method==="NONE")return!0;if(this.keyFormat==="identity")return this.method==="SAMPLE-AES";switch(this.keyFormat){case We.FAIRPLAY:case We.WIDEVINE:case We.PLAYREADY:case We.CLEARKEY:return["ISO-23001-7","SAMPLE-AES","SAMPLE-AES-CENC","SAMPLE-AES-CTR"].indexOf(this.method)!==-1}}return!1},a.getDecryptData=function(e){if(!this.encrypted||!this.uri)return null;if(this.method==="AES-128"&&this.uri&&!this.iv){typeof e!="number"&&(this.method==="AES-128"&&!this.iv&&P.warn('missing IV for initialization segment with method="'+this.method+'" - compliance issue'),e=0);var t=ct(e),n=new s(this.method,this.uri,"identity",this.keyFormatVersions,t);return n}var i=Gr(this.uri);if(i)switch(this.keyFormat){case We.WIDEVINE:this.pssh=i,i.length>=22&&(this.keyId=i.subarray(i.length-22,i.length-6));break;case We.PLAYREADY:{var o=new Uint8Array([154,4,240,121,152,64,66,134,171,146,230,91,224,136,95,149]);this.pssh=cn(o,null,i);var l=new Uint16Array(i.buffer,i.byteOffset,i.byteLength/2),d=String.fromCharCode.apply(null,Array.from(l)),u=d.substring(d.indexOf("<"),d.length),c=new DOMParser,p=c.parseFromString(u,"text/xml"),g=p.getElementsByTagName("KID")[0];if(g){var y=g.childNodes[0]?g.childNodes[0].nodeValue:g.getAttribute("VALUE");if(y){var b=Pn(y).subarray(0,16);St(b),this.keyId=b}}break}default:{var E=i.subarray(0,16);if(E.length!==16){var C=new Uint8Array(16);C.set(E,16-E.length),E=C}this.keyId=E;break}}if(!this.keyId||this.keyId.byteLength!==16){var w=fn[this.uri];if(!w){var k=Object.keys(fn).length%Number.MAX_SAFE_INTEGER;w=new Uint8Array(16);var D=new DataView(w.buffer,12,4);D.setUint32(0,k),fn[this.uri]=w}this.keyId=w}return this},s}();function ct(s){for(var a=new Uint8Array(16),r=12;r<16;r++)a[r]=s>>8*(15-r)&255;return a}var Un=/\{\$([a-zA-Z0-9-_]+)\}/g;function mr(s){return Un.test(s)}function qe(s,a,r){if(s.variableList!==null||s.hasVariableRefs)for(var e=r.length;e--;){var t=r[e],n=a[t];n&&(a[t]=Ht(s,n))}}function Ht(s,a){if(s.variableList!==null||s.hasVariableRefs){var r=s.variableList;return a.replace(Un,function(e){var t=e.substring(2,e.length-1),n=r==null?void 0:r[t];return n===void 0?(s.playlistParsingError||(s.playlistParsingError=new Error('Missing preceding EXT-X-DEFINE tag for Variable Reference: "'+t+'"')),e):n})}return a}function ni(s,a,r){var e=s.variableList;e||(s.variableList=e={});var t,n;if("QUERYPARAM"in a){t=a.QUERYPARAM;try{var i=new self.URL(r).searchParams;if(i.has(t))n=i.get(t);else throw new Error('"'+t+'" does not match any query parameter in URI: "'+r+'"')}catch(o){s.playlistParsingError||(s.playlistParsingError=new Error("EXT-X-DEFINE QUERYPARAM: "+o.message))}}else t=a.NAME,n=a.VALUE;t in e?s.playlistParsingError||(s.playlistParsingError=new Error('EXT-X-DEFINE duplicate Variable Name declarations: "'+t+'"')):e[t]=n||""}function ri(s,a,r){var e=a.IMPORT;if(r&&e in r){var t=s.variableList;t||(s.variableList=t={}),t[e]=r[e]}else s.playlistParsingError||(s.playlistParsingError=new Error('EXT-X-DEFINE IMPORT attribute not found in Multivariant Playlist: "'+e+'"'))}var Xi={audio:{a3ds:!0,"ac-3":!0,"ac-4":!0,alac:!0,alaw:!0,dra1:!0,"dts+":!0,"dts-":!0,dtsc:!0,dtse:!0,dtsh:!0,"ec-3":!0,enca:!0,g719:!0,g726:!0,m4ae:!0,mha1:!0,mha2:!0,mhm1:!0,mhm2:!0,mlpa:!0,mp4a:!0,"raw ":!0,Opus:!0,opus:!0,samr:!0,sawb:!0,sawp:!0,sevc:!0,sqcp:!0,ssmv:!0,twos:!0,ulaw:!0},video:{avc1:!0,avc2:!0,avc3:!0,avc4:!0,avcp:!0,av01:!0,drac:!0,dva1:!0,dvav:!0,dvh1:!0,dvhe:!0,encv:!0,hev1:!0,hvc1:!0,mjp2:!0,mp4v:!0,mvc1:!0,mvc2:!0,mvc3:!0,mvc4:!0,resv:!0,rv60:!0,s263:!0,svc1:!0,svc2:!0,"vc-1":!0,vp08:!0,vp09:!0},text:{stpp:!0,wvtt:!0}};function ii(s,a){var r=Xi[a];return!!r&&r[s.slice(0,4)]===!0}function gr(s,a){return MediaSource.isTypeSupported((a||"video")+'/mp4;codecs="'+s+'"')}var Kn=/#EXT-X-STREAM-INF:([^\r\n]*)(?:[\r\n](?:#[^\r\n]*)?)*([^\r\n]+)|#EXT-X-(SESSION-DATA|SESSION-KEY|DEFINE|CONTENT-STEERING|START):([^\r\n]*)[\r\n]+/g,Gn=/#EXT-X-MEDIA:(.*)/g,ai=/^#EXT(?:INF|-X-TARGETDURATION):/m,yr=new RegExp([/#EXTINF:\s*(\d*(?:\.\d+)?)(?:,(.*)\s+)?/.source,/(?!#) *(\S[\S ]*)/.source,/#EXT-X-BYTERANGE:*(.+)/.source,/#EXT-X-PROGRAM-DATE-TIME:(.+)/.source,/#.*/.source].join("|"),"g"),qt=new RegExp([/#(EXTM3U)/.source,/#EXT-X-(DATERANGE|DEFINE|KEY|MAP|PART|PART-INF|PLAYLIST-TYPE|PRELOAD-HINT|RENDITION-REPORT|SERVER-CONTROL|SKIP|START):(.+)/.source,/#EXT-X-(BITRATE|DISCONTINUITY-SEQUENCE|MEDIA-SEQUENCE|TARGETDURATION|VERSION): *(\d+)/.source,/#EXT-X-(DISCONTINUITY|ENDLIST|GAP)/.source,/(#)([^:]*):(.*)/.source,/(#)(.*)(?:.*)\r?\n?/.source].join("|")),gt=function(){function s(){}return s.findGroup=function(r,e){for(var t=0;t<r.length;t++){var n=r[t];if(n.id===e)return n}},s.convertAVC1ToAVCOTI=function(r){var e=r.split(".");if(e.length>2){var t=e.shift()+".";return t+=parseInt(e.shift()).toString(16),t+=("000"+parseInt(e.shift()).toString(16)).slice(-4),t}return r},s.resolve=function(r,e){return ge.buildAbsoluteURL(e,r,{alwaysNormalize:!0})},s.isMediaPlaylist=function(r){return ai.test(r)},s.parseMasterPlaylist=function(r,e){var t=mr(r),n={contentSteering:null,levels:[],playlistParsingError:null,sessionData:null,sessionKeys:null,startTimeOffset:null,variableList:null,hasVariableRefs:t},i=[];Kn.lastIndex=0;for(var o;(o=Kn.exec(r))!=null;)if(o[1]){var l,d=new ne(o[1]);qe(n,d,["CODECS","SUPPLEMENTAL-CODECS","ALLOWED-CPC","PATHWAY-ID","STABLE-VARIANT-ID","AUDIO","VIDEO","SUBTITLES","CLOSED-CAPTIONS","NAME"]);var u=Ht(n,o[2]),c={attrs:d,bitrate:d.decimalInteger("AVERAGE-BANDWIDTH")||d.decimalInteger("BANDWIDTH"),name:d.NAME,url:s.resolve(u,e)},p=d.decimalResolution("RESOLUTION");p&&(c.width=p.width,c.height=p.height),br((d.CODECS||"").split(/[ ,]+/).filter(function(_){return _}),c),c.videoCodec&&c.videoCodec.indexOf("avc1")!==-1&&(c.videoCodec=s.convertAVC1ToAVCOTI(c.videoCodec)),(l=c.unknownCodecs)!=null&&l.length||i.push(c),n.levels.push(c)}else if(o[3]){var g=o[3],y=o[4];switch(g){case"SESSION-DATA":{var b=new ne(y);qe(n,b,["DATA-ID","LANGUAGE","VALUE","URI"]);var E=b["DATA-ID"];E&&(n.sessionData===null&&(n.sessionData={}),n.sessionData[E]=b);break}case"SESSION-KEY":{var C=Hn(y,e,n);C.encrypted&&C.isSupported()?(n.sessionKeys===null&&(n.sessionKeys=[]),n.sessionKeys.push(C)):P.warn('[Keys] Ignoring invalid EXT-X-SESSION-KEY tag: "'+y+'"');break}case"DEFINE":{{var w=new ne(y);qe(n,w,["NAME","VALUE","QUERYPARAM"]),ni(n,w,e)}break}case"CONTENT-STEERING":{var k=new ne(y);qe(n,k,["SERVER-URI","PATHWAY-ID"]),n.contentSteering={uri:s.resolve(k["SERVER-URI"],e),pathwayId:k["PATHWAY-ID"]||"."};break}case"START":{n.startTimeOffset=Ar(y);break}}}var D=i.length>0&&i.length<n.levels.length;return n.levels=D?i:n.levels,n.levels.length===0&&(n.playlistParsingError=new Error("no levels found in manifest")),n},s.parseMasterPlaylistMedia=function(r,e,t){var n,i={},o=t.levels,l={AUDIO:o.map(function(E){return{id:E.attrs.AUDIO,audioCodec:E.audioCodec}}),SUBTITLES:o.map(function(E){return{id:E.attrs.SUBTITLES,textCodec:E.textCodec}}),"CLOSED-CAPTIONS":[]},d=0;for(Gn.lastIndex=0;(n=Gn.exec(r))!==null;){var u=new ne(n[1]),c=u.TYPE;if(c){var p=l[c],g=i[c]||[];i[c]=g,qe(t,u,["URI","GROUP-ID","LANGUAGE","ASSOC-LANGUAGE","STABLE-RENDITION-ID","NAME","INSTREAM-ID","CHARACTERISTICS","CHANNELS"]);var y={attrs:u,bitrate:0,id:d++,groupId:u["GROUP-ID"]||"",instreamId:u["INSTREAM-ID"],name:u.NAME||u.LANGUAGE||"",type:c,default:u.bool("DEFAULT"),autoselect:u.bool("AUTOSELECT"),forced:u.bool("FORCED"),lang:u.LANGUAGE,url:u.URI?s.resolve(u.URI,e):""};if(p!=null&&p.length){var b=s.findGroup(p,y.groupId)||p[0];Yt(y,b,"audioCodec"),Yt(y,b,"textCodec")}g.push(y)}}return i},s.parseLevelPlaylist=function(r,e,t,n,i,o){var l=new sn(e),d=l.fragments,u=null,c=0,p=0,g=0,y=0,b=null,E=new nt(n,e),C,w,k,D=-1,_=!1;for(yr.lastIndex=0,l.m3u8=r,l.hasVariableRefs=mr(r);(C=yr.exec(r))!==null;){_&&(_=!1,E=new nt(n,e),E.start=g,E.sn=c,E.cc=y,E.level=t,u&&(E.initSegment=u,E.rawProgramDateTime=u.rawProgramDateTime,u.rawProgramDateTime=null));var F=C[1];if(F){E.duration=parseFloat(F);var O=(" "+C[2]).slice(1);E.title=O||null,E.tagList.push(O?["INF",F,O]:["INF",F])}else if(C[3]){if(oe(E.duration)){E.start=g,k&&pn(E,k,l),E.sn=c,E.level=t,E.cc=y,E.urlId=i,d.push(E);var M=(" "+C[3]).slice(1);E.relurl=Ht(l,M),ft(E,b),b=E,g+=E.duration,c++,p=0,_=!0}}else if(C[4]){var z=(" "+C[4]).slice(1);b?E.setByteRange(z,b):E.setByteRange(z)}else if(C[5])E.rawProgramDateTime=(" "+C[5]).slice(1),E.tagList.push(["PROGRAM-DATE-TIME",E.rawProgramDateTime]),D===-1&&(D=d.length);else{if(C=C[0].match(qt),!C){P.warn("No matches on slow regex match for level playlist!");continue}for(w=1;w<C.length&&typeof C[w]=="undefined";w++);var H=(" "+C[w]).slice(1),G=(" "+C[w+1]).slice(1),te=C[w+2]?(" "+C[w+2]).slice(1):"";switch(H){case"PLAYLIST-TYPE":l.type=G.toUpperCase();break;case"MEDIA-SEQUENCE":c=l.startSN=parseInt(G);break;case"SKIP":{var ye=new ne(G);qe(l,ye,["RECENTLY-REMOVED-DATERANGES"]);var Ae=ye.decimalInteger("SKIPPED-SEGMENTS");if(oe(Ae)){l.skippedSegments=Ae;for(var Ee=Ae;Ee--;)d.unshift(null);c+=Ae}var we=ye.enumeratedString("RECENTLY-REMOVED-DATERANGES");we&&(l.recentlyRemovedDateranges=we.split("	"));break}case"TARGETDURATION":l.targetduration=Math.max(parseInt(G),1);break;case"VERSION":l.version=parseInt(G);break;case"EXTM3U":break;case"ENDLIST":l.live=!1;break;case"#":(G||te)&&E.tagList.push(te?[G,te]:[G]);break;case"DISCONTINUITY":y++,E.tagList.push(["DIS"]);break;case"GAP":E.gap=!0,E.tagList.push([H]);break;case"BITRATE":E.tagList.push([H,G]);break;case"DATERANGE":{var xe=new ne(G);qe(l,xe,["ID","CLASS","START-DATE","END-DATE","SCTE35-CMD","SCTE35-OUT","SCTE35-IN"]),qe(l,xe,xe.clientAttrs);var _e=new Rn(xe,l.dateRanges[xe.ID]);_e.isValid||l.skippedSegments?l.dateRanges[_e.id]=_e:P.warn('Ignoring invalid DATERANGE tag: "'+G+'"'),E.tagList.push(["EXT-X-DATERANGE",G]);break}case"DEFINE":{{var Re=new ne(G);qe(l,Re,["NAME","VALUE","IMPORT","QUERYPARAM"]),"IMPORT"in Re?ri(l,Re,o):ni(l,Re,e)}break}case"DISCONTINUITY-SEQUENCE":y=parseInt(G);break;case"KEY":{var Be=Hn(G,e,l);if(Be.isSupported()){if(Be.method==="NONE"){k=void 0;break}k||(k={}),k[Be.keyFormat]&&(k=q({},k)),k[Be.keyFormat]=Be}else P.warn('[Keys] Ignoring invalid EXT-X-KEY tag: "'+G+'"');break}case"START":l.startTimeOffset=Ar(G);break;case"MAP":{var ut=new ne(G);if(qe(l,ut,["BYTERANGE","URI"]),E.duration){var Ne=new nt(n,e);hn(Ne,ut,t,k),u=Ne,E.initSegment=u,u.rawProgramDateTime&&!E.rawProgramDateTime&&(E.rawProgramDateTime=u.rawProgramDateTime)}else hn(E,ut,t,k),u=E,_=!0;break}case"SERVER-CONTROL":{var He=new ne(G);l.canBlockReload=He.bool("CAN-BLOCK-RELOAD"),l.canSkipUntil=He.optionalFloat("CAN-SKIP-UNTIL",0),l.canSkipDateRanges=l.canSkipUntil>0&&He.bool("CAN-SKIP-DATERANGES"),l.partHoldBack=He.optionalFloat("PART-HOLD-BACK",0),l.holdBack=He.optionalFloat("HOLD-BACK",0);break}case"PART-INF":{var at=new ne(G);l.partTarget=at.decimalFloatingPoint("PART-TARGET");break}case"PART":{var tt=l.partList;tt||(tt=l.partList=[]);var ot=p>0?tt[tt.length-1]:void 0,Pt=p++,Jt=new ne(G);qe(l,Jt,["BYTERANGE","URI"]);var xt=new _n(Jt,E,e,Pt,ot);tt.push(xt),E.duration+=xt.duration;break}case"PRELOAD-HINT":{var Bt=new ne(G);qe(l,Bt,["URI"]),l.preloadHint=Bt;break}case"RENDITION-REPORT":{var Ze=new ne(G);qe(l,Ze,["URI"]),l.renditionReports=l.renditionReports||[],l.renditionReports.push(Ze);break}default:P.warn("line parsed but not handled: "+C);break}}}b&&!b.relurl?(d.pop(),g-=b.duration,l.partList&&(l.fragmentHint=b)):l.partList&&(ft(E,b),E.cc=y,l.fragmentHint=E,k&&pn(E,k,l));var Je=d.length,$t=d[0],Ot=d[Je-1];if(g+=l.skippedSegments*l.targetduration,g>0&&Je&&Ot){l.averagetargetduration=g/Je;var en=Ot.sn;l.endSN=en!=="initSegment"?en:0,l.live||(Ot.endList=!0),$t&&(l.startCC=$t.cc)}else l.endSN=0,l.startCC=0;return l.fragmentHint&&(g+=l.fragmentHint.duration),l.totalduration=g,l.endCC=y,D>0&&Zi(d,D),l},s}();function Hn(s,a,r){var e,t,n=new ne(s);qe(r,n,["KEYFORMAT","KEYFORMATVERSIONS","URI","IV","URI"]);var i=(e=n.METHOD)!=null?e:"",o=n.URI,l=n.hexadecimalInteger("IV"),d=n.KEYFORMATVERSIONS,u=(t=n.KEYFORMAT)!=null?t:"identity";o&&n.IV&&!l&&P.error("Invalid IV: "+n.IV);var c=o?gt.resolve(o,a):"",p=(d||"1").split("/").map(Number).filter(Number.isFinite);return new Gt(i,c,u,p,l)}function Ar(s){var a=new ne(s),r=a.decimalFloatingPoint("TIME-OFFSET");return oe(r)?r:null}function br(s,a){["video","audio","text"].forEach(function(r){var e=s.filter(function(n){return ii(n,r)});if(e.length){var t=e.filter(function(n){return n.lastIndexOf("avc1",0)===0||n.lastIndexOf("mp4a",0)===0});a[r+"Codec"]=t.length>0?t[0]:e[0],s=s.filter(function(n){return e.indexOf(n)===-1})}}),a.unknownCodecs=s}function Yt(s,a,r){var e=a[r];e&&(s[r]=e)}function Zi(s,a){for(var r=s[a],e=a;e--;){var t=s[e];if(!t)return;t.programDateTime=r.programDateTime-t.duration*1e3,r=t}}function ft(s,a){s.rawProgramDateTime?s.programDateTime=Date.parse(s.rawProgramDateTime):a!=null&&a.programDateTime&&(s.programDateTime=a.endProgramDateTime),oe(s.programDateTime)||(s.programDateTime=null,s.rawProgramDateTime=null)}function hn(s,a,r,e){s.relurl=a.URI,a.BYTERANGE&&s.setByteRange(a.BYTERANGE),s.level=r,s.sn="initSegment",e&&(s.levelkeys=e),s.initSegment=null}function pn(s,a,r){s.levelkeys=a;var e=r.encryptedFragments;(!e.length||e[e.length-1].levelkeys!==a)&&Object.keys(a).some(function(t){return a[t].isCommonEncryption})&&e.push(s)}var be={MANIFEST:"manifest",LEVEL:"level",AUDIO_TRACK:"audioTrack",SUBTITLE_TRACK:"subtitleTrack"},he={MAIN:"main",AUDIO:"audio",SUBTITLE:"subtitle"};function oi(s){var a=s.type;switch(a){case be.AUDIO_TRACK:return he.AUDIO;case be.SUBTITLE_TRACK:return he.SUBTITLE;default:return he.MAIN}}function Er(s,a){var r=s.url;return(r===void 0||r.indexOf("data:")===0)&&(r=a.url),r}var si=function(){function s(r){this.hls=void 0,this.loaders=Object.create(null),this.variableList=null,this.hls=r,this.registerListeners()}var a=s.prototype;return a.startLoad=function(e){},a.stopLoad=function(){this.destroyInternalLoaders()},a.registerListeners=function(){var e=this.hls;e.on(S.MANIFEST_LOADING,this.onManifestLoading,this),e.on(S.LEVEL_LOADING,this.onLevelLoading,this),e.on(S.AUDIO_TRACK_LOADING,this.onAudioTrackLoading,this),e.on(S.SUBTITLE_TRACK_LOADING,this.onSubtitleTrackLoading,this)},a.unregisterListeners=function(){var e=this.hls;e.off(S.MANIFEST_LOADING,this.onManifestLoading,this),e.off(S.LEVEL_LOADING,this.onLevelLoading,this),e.off(S.AUDIO_TRACK_LOADING,this.onAudioTrackLoading,this),e.off(S.SUBTITLE_TRACK_LOADING,this.onSubtitleTrackLoading,this)},a.createInternalLoader=function(e){var t=this.hls.config,n=t.pLoader,i=t.loader,o=n||i,l=new o(t);return this.loaders[e.type]=l,l},a.getInternalLoader=function(e){return this.loaders[e.type]},a.resetInternalLoader=function(e){this.loaders[e]&&delete this.loaders[e]},a.destroyInternalLoaders=function(){for(var e in this.loaders){var t=this.loaders[e];t&&t.destroy(),this.resetInternalLoader(e)}},a.destroy=function(){this.variableList=null,this.unregisterListeners(),this.destroyInternalLoaders()},a.onManifestLoading=function(e,t){var n=t.url;this.variableList=null,this.load({id:null,level:0,responseType:"text",type:be.MANIFEST,url:n,deliveryDirectives:null})},a.onLevelLoading=function(e,t){var n=t.id,i=t.level,o=t.url,l=t.deliveryDirectives;this.load({id:n,level:i,responseType:"text",type:be.LEVEL,url:o,deliveryDirectives:l})},a.onAudioTrackLoading=function(e,t){var n=t.id,i=t.groupId,o=t.url,l=t.deliveryDirectives;this.load({id:n,groupId:i,level:null,responseType:"text",type:be.AUDIO_TRACK,url:o,deliveryDirectives:l})},a.onSubtitleTrackLoading=function(e,t){var n=t.id,i=t.groupId,o=t.url,l=t.deliveryDirectives;this.load({id:n,groupId:i,level:null,responseType:"text",type:be.SUBTITLE_TRACK,url:o,deliveryDirectives:l})},a.load=function(e){var t,n=this,i=this.hls.config,o=this.getInternalLoader(e);if(o){var l=o.context;if(l&&l.url===e.url){P.trace("[playlist-loader]: playlist request ongoing");return}P.log("[playlist-loader]: aborting previous loader for type: "+e.type),o.abort()}var d;if(e.type===be.MANIFEST?d=i.manifestLoadPolicy.default:d=q({},i.playlistLoadPolicy.default,{timeoutRetry:null,errorRetry:null}),o=this.createInternalLoader(e),(t=e.deliveryDirectives)!=null&&t.part){var u;if(e.type===be.LEVEL&&e.level!==null?u=this.hls.levels[e.level].details:e.type===be.AUDIO_TRACK&&e.id!==null?u=this.hls.audioTracks[e.id].details:e.type===be.SUBTITLE_TRACK&&e.id!==null&&(u=this.hls.subtitleTracks[e.id].details),u){var c=u.partTarget,p=u.targetduration;if(c&&p){var g=Math.max(c*3,p*.8)*1e3;d=q({},d,{maxTimeToFirstByteMs:Math.min(g,d.maxTimeToFirstByteMs),maxLoadTimeMs:Math.min(g,d.maxTimeToFirstByteMs)})}}}var y=d.errorRetry||d.timeoutRetry||{},b={loadPolicy:d,timeout:d.maxLoadTimeMs,maxRetry:y.maxNumRetry||0,retryDelay:y.retryDelayMs||0,maxRetryDelay:y.maxRetryDelayMs||0},E={onSuccess:function(w,k,D,_){var F=n.getInternalLoader(D);n.resetInternalLoader(D.type);var O=w.data;if(O.indexOf("#EXTM3U")!==0){n.handleManifestParsingError(w,D,new Error("no EXTM3U delimiter"),_||null,k);return}k.parsing.start=performance.now(),gt.isMediaPlaylist(O)?n.handleTrackOrLevelPlaylist(w,k,D,_||null,F):n.handleMasterPlaylist(w,k,D,_)},onError:function(w,k,D,_){n.handleNetworkError(k,D,!1,w,_)},onTimeout:function(w,k,D){n.handleNetworkError(k,D,!0,void 0,w)}};o.load(e,b,E)},a.handleMasterPlaylist=function(e,t,n,i){var o=this.hls,l=e.data,d=Er(e,n),u=gt.parseMasterPlaylist(l,d);if(u.playlistParsingError){this.handleManifestParsingError(e,n,u.playlistParsingError,i,t);return}var c=u.contentSteering,p=u.levels,g=u.sessionData,y=u.sessionKeys,b=u.startTimeOffset,E=u.variableList;this.variableList=E;var C=gt.parseMasterPlaylistMedia(l,d,u),w=C.AUDIO,k=w===void 0?[]:w,D=C.SUBTITLES,_=C["CLOSED-CAPTIONS"];if(k.length){var F=k.some(function(O){return!O.url});!F&&p[0].audioCodec&&!p[0].attrs.AUDIO&&(P.log("[playlist-loader]: audio codec signaled in quality level, but no embedded audio track signaled, create one"),k.unshift({type:"main",name:"main",groupId:"main",default:!1,autoselect:!1,forced:!1,id:-1,attrs:new ne({}),bitrate:0,url:""}))}o.trigger(S.MANIFEST_LOADED,{levels:p,audioTracks:k,subtitles:D,captions:_,contentSteering:c,url:d,stats:t,networkDetails:i,sessionData:g,sessionKeys:y,startTimeOffset:b,variableList:E})},a.handleTrackOrLevelPlaylist=function(e,t,n,i,o){var l=this.hls,d=n.id,u=n.level,c=n.type,p=Er(e,n),g=oe(d)?d:0,y=oe(u)?u:g,b=oi(n),E=gt.parseLevelPlaylist(e.data,p,y,b,g,this.variableList);if(c===be.MANIFEST){var C={attrs:new ne({}),bitrate:0,details:E,name:"",url:p};l.trigger(S.MANIFEST_LOADED,{levels:[C],audioTracks:[],url:p,stats:t,networkDetails:i,sessionData:null,sessionKeys:null,contentSteering:null,startTimeOffset:null,variableList:null})}t.parsing.end=performance.now(),n.levelDetails=E,this.handlePlaylistLoaded(E,e,t,n,i,o)},a.handleManifestParsingError=function(e,t,n,i,o){this.hls.trigger(S.ERROR,{type:fe.NETWORK_ERROR,details:U.MANIFEST_PARSING_ERROR,fatal:t.type===be.MANIFEST,url:e.url,err:n,error:n,reason:n.message,response:e,context:t,networkDetails:i,stats:o})},a.handleNetworkError=function(e,t,n,i,o){n===void 0&&(n=!1);var l="A network "+(n?"timeout":"error"+(i?" (status "+i.code+")":""))+" occurred while loading "+e.type;e.type===be.LEVEL?l+=": "+e.level+" id: "+e.id:(e.type===be.AUDIO_TRACK||e.type===be.SUBTITLE_TRACK)&&(l+=" id: "+e.id+' group-id: "'+e.groupId+'"');var d=new Error(l);P.warn("[playlist-loader]: "+l);var u=U.UNKNOWN,c=!1,p=this.getInternalLoader(e);switch(e.type){case be.MANIFEST:u=n?U.MANIFEST_LOAD_TIMEOUT:U.MANIFEST_LOAD_ERROR,c=!0;break;case be.LEVEL:u=n?U.LEVEL_LOAD_TIMEOUT:U.LEVEL_LOAD_ERROR,c=!1;break;case be.AUDIO_TRACK:u=n?U.AUDIO_TRACK_LOAD_TIMEOUT:U.AUDIO_TRACK_LOAD_ERROR,c=!1;break;case be.SUBTITLE_TRACK:u=n?U.SUBTITLE_TRACK_LOAD_TIMEOUT:U.SUBTITLE_LOAD_ERROR,c=!1;break}p&&this.resetInternalLoader(e.type);var g={type:fe.NETWORK_ERROR,details:u,fatal:c,url:e.url,loader:p,context:e,error:d,networkDetails:t,stats:o};if(i){var y=(t==null?void 0:t.url)||e.url;g.response=Ke({url:y,data:void 0},i)}this.hls.trigger(S.ERROR,g)},a.handlePlaylistLoaded=function(e,t,n,i,o,l){var d=this.hls,u=i.type,c=i.level,p=i.id,g=i.groupId,y=i.deliveryDirectives,b=Er(t,i),E=oi(i),C=typeof i.level=="number"&&E===he.MAIN?c:void 0;if(!e.fragments.length){var w=new Error("No Segments found in Playlist");d.trigger(S.ERROR,{type:fe.NETWORK_ERROR,details:U.LEVEL_EMPTY_ERROR,fatal:!1,url:b,error:w,reason:w.message,response:t,context:i,level:C,parent:E,networkDetails:o,stats:n});return}e.targetduration||(e.playlistParsingError=new Error("Missing Target Duration"));var k=e.playlistParsingError;if(k){d.trigger(S.ERROR,{type:fe.NETWORK_ERROR,details:U.LEVEL_PARSING_ERROR,fatal:!1,url:b,error:k,reason:k.message,response:t,context:i,level:C,parent:E,networkDetails:o,stats:n});return}switch(e.live&&l&&(l.getCacheAge&&(e.ageHeader=l.getCacheAge()||0),(!l.getCacheAge||isNaN(e.ageHeader))&&(e.ageHeader=0)),u){case be.MANIFEST:case be.LEVEL:d.trigger(S.LEVEL_LOADED,{details:e,level:C||0,id:p||0,stats:n,networkDetails:o,deliveryDirectives:y});break;case be.AUDIO_TRACK:d.trigger(S.AUDIO_TRACK_LOADED,{details:e,id:p||0,groupId:g||"",stats:n,networkDetails:o,deliveryDirectives:y});break;case be.SUBTITLE_TRACK:d.trigger(S.SUBTITLE_TRACK_LOADED,{details:e,id:p||0,groupId:g||"",stats:n,networkDetails:o,deliveryDirectives:y});break}},s}();function qn(s,a){var r;try{r=new Event("addtrack")}catch(e){r=document.createEvent("Event"),r.initEvent("addtrack",!1,!1)}r.track=s,a.dispatchEvent(r)}function li(s,a){var r=s.mode;if(r==="disabled"&&(s.mode="hidden"),s.cues&&!s.cues.getCueById(a.id))try{if(s.addCue(a),!s.cues.getCueById(a.id))throw new Error("addCue is failed for: "+a)}catch(t){P.debug("[texttrack-utils]: "+t);var e=new self.TextTrackCue(a.startTime,a.endTime,a.text);e.id=a.id,s.addCue(e)}r==="disabled"&&(s.mode=r)}function Pe(s){var a=s.mode;if(a==="disabled"&&(s.mode="hidden"),s.cues)for(var r=s.cues.length;r--;)s.removeCue(s.cues[r]);a==="disabled"&&(s.mode=a)}function Yn(s,a,r,e){var t=s.mode;if(t==="disabled"&&(s.mode="hidden"),s.cues&&s.cues.length>0)for(var n=xr(s.cues,a,r),i=0;i<n.length;i++)(!e||e(n[i]))&&s.removeCue(n[i]);t==="disabled"&&(s.mode=t)}function Ji(s,a){if(a<s[0].startTime)return 0;var r=s.length-1;if(a>s[r].endTime)return-1;for(var e=0,t=r;e<=t;){var n=Math.floor((t+e)/2);if(a<s[n].startTime)t=n-1;else if(a>s[n].startTime&&e<r)e=n+1;else return n}return s[e].startTime-a<a-s[t].startTime?e:t}function xr(s,a,r){var e=[],t=Ji(s,a);if(t>-1)for(var n=t,i=s.length;n<i;n++){var o=s[n];if(o.startTime>=a&&o.endTime<=r)e.push(o);else if(o.startTime>r)return e}return e}var it={audioId3:"org.id3",dateRange:"com.apple.quicktime.HLS",emsg:"https://aomedia.org/emsg/ID3"},Tr=.25;function yt(){if(typeof self!="undefined")return self.WebKitDataCue||self.VTTCue||self.TextTrackCue}var Wn=function(){var s=yt();try{new s(0,Number.POSITIVE_INFINITY,"")}catch(a){return Number.MAX_VALUE}return Number.POSITIVE_INFINITY}();function Sr(s,a){return s.getTime()/1e3-a}function $i(s){return Uint8Array.from(s.replace(/^0x/,"").replace(/([\da-fA-F]{2}) ?/g,"0x$1 ").replace(/ +$/,"").split(" ")).buffer}var ea=function(){function s(r){this.hls=void 0,this.id3Track=null,this.media=null,this.dateRangeCuesAppended={},this.hls=r,this._registerListeners()}var a=s.prototype;return a.destroy=function(){this._unregisterListeners(),this.id3Track=null,this.media=null,this.dateRangeCuesAppended={},this.hls=null},a._registerListeners=function(){var e=this.hls;e.on(S.MEDIA_ATTACHED,this.onMediaAttached,this),e.on(S.MEDIA_DETACHING,this.onMediaDetaching,this),e.on(S.MANIFEST_LOADING,this.onManifestLoading,this),e.on(S.FRAG_PARSING_METADATA,this.onFragParsingMetadata,this),e.on(S.BUFFER_FLUSHING,this.onBufferFlushing,this),e.on(S.LEVEL_UPDATED,this.onLevelUpdated,this)},a._unregisterListeners=function(){var e=this.hls;e.off(S.MEDIA_ATTACHED,this.onMediaAttached,this),e.off(S.MEDIA_DETACHING,this.onMediaDetaching,this),e.off(S.MANIFEST_LOADING,this.onManifestLoading,this),e.off(S.FRAG_PARSING_METADATA,this.onFragParsingMetadata,this),e.off(S.BUFFER_FLUSHING,this.onBufferFlushing,this),e.off(S.LEVEL_UPDATED,this.onLevelUpdated,this)},a.onMediaAttached=function(e,t){this.media=t.media},a.onMediaDetaching=function(){!this.id3Track||(Pe(this.id3Track),this.id3Track=null,this.media=null,this.dateRangeCuesAppended={})},a.onManifestLoading=function(){this.dateRangeCuesAppended={}},a.createTrack=function(e){var t=this.getID3Track(e.textTracks);return t.mode="hidden",t},a.getID3Track=function(e){if(!!this.media){for(var t=0;t<e.length;t++){var n=e[t];if(n.kind==="metadata"&&n.label==="id3")return qn(n,this.media),n}return this.media.addTextTrack("metadata","id3")}},a.onFragParsingMetadata=function(e,t){if(!!this.media){var n=this.hls.config,i=n.enableEmsgMetadataCues,o=n.enableID3MetadataCues;if(!(!i&&!o)){var l=t.samples;this.id3Track||(this.id3Track=this.createTrack(this.media));for(var d=yt(),u=0;u<l.length;u++){var c=l[u].type;if(!(c===it.emsg&&!i||!o)){var p=sr(l[u].data);if(p){var g=l[u].pts,y=g+l[u].duration;y>Wn&&(y=Wn);var b=y-g;b<=0&&(y=g+Tr);for(var E=0;E<p.length;E++){var C=p[E];if(!K(C)){this.updateId3CueEnds(g);var w=new d(g,y,"");w.value=C,c&&(w.type=c),this.id3Track.addCue(w)}}}}}}}},a.updateId3CueEnds=function(e){var t,n=(t=this.id3Track)==null?void 0:t.cues;if(n)for(var i=n.length;i--;){var o=n[i];o.startTime<e&&o.endTime===Wn&&(o.endTime=e)}},a.onBufferFlushing=function(e,t){var n=t.startOffset,i=t.endOffset,o=t.type,l=this.id3Track,d=this.hls;if(!!d){var u=d.config,c=u.enableEmsgMetadataCues,p=u.enableID3MetadataCues;if(l&&(c||p)){var g;o==="audio"?g=function(b){return b.type===it.audioId3&&p}:o==="video"?g=function(b){return b.type===it.emsg&&c}:g=function(b){return b.type===it.audioId3&&p||b.type===it.emsg&&c},Yn(l,n,i,g)}}},a.onLevelUpdated=function(e,t){var n=this,i=t.details;if(!(!this.media||!i.hasProgramDateTime||!this.hls.config.enableDateRangeMetadataCues)){var o=this.dateRangeCuesAppended,l=this.id3Track,d=i.dateRanges,u=Object.keys(d);if(l)for(var c=Object.keys(o).filter(function(k){return!u.includes(k)}),p=function(){var D=c[g];Object.keys(o[D].cues).forEach(function(_){l.removeCue(o[D].cues[_])}),delete o[D]},g=c.length;g--;)p();var y=i.fragments[i.fragments.length-1];if(!(u.length===0||!oe(y==null?void 0:y.programDateTime))){this.id3Track||(this.id3Track=this.createTrack(this.media));for(var b=y.programDateTime/1e3-y.start,E=yt(),C=function(){var D=u[w],_=d[D],F=o[D],O=(F==null?void 0:F.cues)||{},M=(F==null?void 0:F.durationKnown)||!1,z=Sr(_.startDate,b),H=Wn,G=_.endDate;if(G)H=Sr(G,b),M=!0;else if(_.endOnNext&&!M){var te=u.reduce(function(_e,Re){var Be=d[Re];return Be.class===_.class&&Be.id!==Re&&Be.startDate>_.startDate&&_e.push(Be),_e},[]).sort(function(_e,Re){return _e.startDate.getTime()-Re.startDate.getTime()})[0];te&&(H=Sr(te.startDate,b),M=!0)}for(var ye=Object.keys(_.attr),Ae=0;Ae<ye.length;Ae++){var Ee=ye[Ae];if(!!Kr(Ee)){var we=O[Ee];if(we)M&&!F.durationKnown&&(we.endTime=H);else{var xe=_.attr[Ee];we=new E(z,H,""),an(Ee)&&(xe=$i(xe)),we.value={key:Ee,data:xe},we.type=it.dateRange,we.id=D,n.id3Track.addCue(we),O[Ee]=we}}}o[D]={cues:O,dateRange:_,durationKnown:M}},w=0;w<u.length;w++)C()}}},s}(),ta=function(){function s(r){var e=this;this.hls=void 0,this.config=void 0,this.media=null,this.levelDetails=null,this.currentTime=0,this.stallCount=0,this._latency=null,this.timeupdateHandler=function(){return e.timeupdate()},this.hls=r,this.config=r.config,this.registerListeners()}var a=s.prototype;return a.destroy=function(){this.unregisterListeners(),this.onMediaDetaching(),this.levelDetails=null,this.hls=this.timeupdateHandler=null},a.registerListeners=function(){this.hls.on(S.MEDIA_ATTACHED,this.onMediaAttached,this),this.hls.on(S.MEDIA_DETACHING,this.onMediaDetaching,this),this.hls.on(S.MANIFEST_LOADING,this.onManifestLoading,this),this.hls.on(S.LEVEL_UPDATED,this.onLevelUpdated,this),this.hls.on(S.ERROR,this.onError,this)},a.unregisterListeners=function(){this.hls.off(S.MEDIA_ATTACHED,this.onMediaAttached,this),this.hls.off(S.MEDIA_DETACHING,this.onMediaDetaching,this),this.hls.off(S.MANIFEST_LOADING,this.onManifestLoading,this),this.hls.off(S.LEVEL_UPDATED,this.onLevelUpdated,this),this.hls.off(S.ERROR,this.onError,this)},a.onMediaAttached=function(e,t){this.media=t.media,this.media.addEventListener("timeupdate",this.timeupdateHandler)},a.onMediaDetaching=function(){this.media&&(this.media.removeEventListener("timeupdate",this.timeupdateHandler),this.media=null)},a.onManifestLoading=function(){this.levelDetails=null,this._latency=null,this.stallCount=0},a.onLevelUpdated=function(e,t){var n=t.details;this.levelDetails=n,n.advanced&&this.timeupdate(),!n.live&&this.media&&this.media.removeEventListener("timeupdate",this.timeupdateHandler)},a.onError=function(e,t){var n;t.details===U.BUFFER_STALLED_ERROR&&(this.stallCount++,(n=this.levelDetails)!=null&&n.live&&P.warn("[playback-rate-controller]: Stall detected, adjusting target latency"))},a.timeupdate=function(){var e=this.media,t=this.levelDetails;if(!(!e||!t)){this.currentTime=e.currentTime;var n=this.computeLatency();if(n!==null){this._latency=n;var i=this.config,o=i.lowLatencyMode,l=i.maxLiveSyncPlaybackRate;if(!(!o||l===1)){var d=this.targetLatency;if(d!==null){var u=n-d,c=Math.min(this.maxLatency,d+t.targetduration),p=u<c;if(t.live&&p&&u>.05&&this.forwardBufferLength>1){var g=Math.min(2,Math.max(1,l)),y=Math.round(2/(1+Math.exp(-.75*u-this.edgeStalled))*20)/20;e.playbackRate=Math.min(g,Math.max(1,y))}else e.playbackRate!==1&&e.playbackRate!==0&&(e.playbackRate=1)}}}}},a.estimateLiveEdge=function(){var e=this.levelDetails;return e===null?null:e.edge+e.age},a.computeLatency=function(){var e=this.estimateLiveEdge();return e===null?null:e-this.currentTime},Q(s,[{key:"latency",get:function(){return this._latency||0}},{key:"maxLatency",get:function(){var e=this.config,t=this.levelDetails;return e.liveMaxLatencyDuration!==void 0?e.liveMaxLatencyDuration:t?e.liveMaxLatencyDurationCount*t.targetduration:0}},{key:"targetLatency",get:function(){var e=this.levelDetails;if(e===null)return null;var t=e.holdBack,n=e.partHoldBack,i=e.targetduration,o=this.config,l=o.liveSyncDuration,d=o.liveSyncDurationCount,u=o.lowLatencyMode,c=this.hls.userConfig,p=u&&n||t;(c.liveSyncDuration||c.liveSyncDurationCount||p===0)&&(p=l!==void 0?l:d*i);var g=i,y=1;return p+Math.min(this.stallCount*y,g)}},{key:"liveSyncPosition",get:function(){var e=this.estimateLiveEdge(),t=this.targetLatency,n=this.levelDetails;if(e===null||t===null||n===null)return null;var i=n.edge,o=e-t-this.edgeStalled,l=i-n.totalduration,d=i-(this.config.lowLatencyMode&&n.partTarget||n.targetduration);return Math.min(Math.max(l,o),d)}},{key:"drift",get:function(){var e=this.levelDetails;return e===null?1:e.drift}},{key:"edgeStalled",get:function(){var e=this.levelDetails;if(e===null)return 0;var t=(this.config.lowLatencyMode&&e.partTarget||e.targetduration)*3;return Math.max(e.age-t,0)}},{key:"forwardBufferLength",get:function(){var e=this.media,t=this.levelDetails;if(!e||!t)return 0;var n=e.buffered.length;return(n?e.buffered.end(n-1):t.edge)-this.currentTime}}]),s}(),Cr=["NONE","TYPE-0","TYPE-1",null],vn={No:"",Yes:"YES",v2:"v2"};function na(s,a){var r=s.canSkipUntil,e=s.canSkipDateRanges,t=s.endSN,n=a!==void 0?a-t:0;return r&&n<r?e?vn.v2:vn.Yes:vn.No}var di=function(){function s(r,e,t){this.msn=void 0,this.part=void 0,this.skip=void 0,this.msn=r,this.part=e,this.skip=t}var a=s.prototype;return a.addDirectives=function(e){var t=new self.URL(e);return this.msn!==void 0&&t.searchParams.set("_HLS_msn",this.msn.toString()),this.part!==void 0&&t.searchParams.set("_HLS_part",this.part.toString()),this.skip&&t.searchParams.set("_HLS_skip",this.skip),t.href},s}(),mn=function(){function s(r){this._attrs=void 0,this.audioCodec=void 0,this.bitrate=void 0,this.codecSet=void 0,this.height=void 0,this.id=void 0,this.name=void 0,this.videoCodec=void 0,this.width=void 0,this.unknownCodecs=void 0,this.audioGroupIds=void 0,this.details=void 0,this.fragmentError=0,this.loadError=0,this.loaded=void 0,this.realBitrate=0,this.textGroupIds=void 0,this.url=void 0,this._urlId=0,this.url=[r.url],this._attrs=[r.attrs],this.bitrate=r.bitrate,r.details&&(this.details=r.details),this.id=r.id||0,this.name=r.name,this.width=r.width||0,this.height=r.height||0,this.audioCodec=r.audioCodec,this.videoCodec=r.videoCodec,this.unknownCodecs=r.unknownCodecs,this.codecSet=[r.videoCodec,r.audioCodec].filter(function(e){return e}).join(",").replace(/\.[^.,]+/g,"")}var a=s.prototype;return a.addFallback=function(e){this.url.push(e.url),this._attrs.push(e.attrs)},Q(s,[{key:"maxBitrate",get:function(){return Math.max(this.realBitrate,this.bitrate)}},{key:"attrs",get:function(){return this._attrs[this._urlId]}},{key:"pathwayId",get:function(){return this.attrs["PATHWAY-ID"]||"."}},{key:"uri",get:function(){return this.url[this._urlId]||""}},{key:"urlId",get:function(){return this._urlId},set:function(e){var t=e%this.url.length;this._urlId!==t&&(this.fragmentError=0,this.loadError=0,this.details=void 0,this._urlId=t)}},{key:"audioGroupId",get:function(){var e;return(e=this.audioGroupIds)==null?void 0:e[this.urlId]}},{key:"textGroupId",get:function(){var e;return(e=this.textGroupIds)==null?void 0:e[this.urlId]}}]),s}();function kr(s,a){var r=a.startPTS;if(oe(r)){var e=0,t;a.sn>s.sn?(e=r-s.start,t=s):(e=s.start-r,t=a),t.duration!==e&&(t.duration=e)}else if(a.sn>s.sn){var n=s.cc===a.cc;n&&s.minEndPTS?a.start=s.start+(s.minEndPTS-s.start):a.start=s.start+s.duration}else a.start=Math.max(s.start-a.duration,0)}function ui(s,a,r,e,t,n){var i=e-r;i<=0&&(P.warn("Fragment should have a positive duration",a),e=r+a.duration,n=t+a.duration);var o=r,l=e,d=a.startPTS,u=a.endPTS;if(oe(d)){var c=Math.abs(d-r);oe(a.deltaPTS)?a.deltaPTS=Math.max(c,a.deltaPTS):a.deltaPTS=c,o=Math.max(r,d),r=Math.min(r,d),t=Math.min(t,a.startDTS),l=Math.min(e,u),e=Math.max(e,u),n=Math.max(n,a.endDTS)}var p=r-a.start;a.start!==0&&(a.start=r),a.duration=e-a.start,a.startPTS=r,a.maxStartPTS=o,a.startDTS=t,a.endPTS=e,a.minEndPTS=l,a.endDTS=n;var g=a.sn;if(!s||g<s.startSN||g>s.endSN)return 0;var y,b=g-s.startSN,E=s.fragments;for(E[b]=a,y=b;y>0;y--)kr(E[y],E[y-1]);for(y=b;y<E.length-1;y++)kr(E[y],E[y+1]);return s.fragmentHint&&kr(E[E.length-1],s.fragmentHint),s.PTSKnown=s.alignedSliding=!0,p}function ra(s,a){for(var r=null,e=s.fragments,t=e.length-1;t>=0;t--){var n=e[t].initSegment;if(n){r=n;break}}s.fragmentHint&&delete s.fragmentHint.endPTS;var i=0,o;if(oa(s,a,function(y,b){y.relurl&&(i=y.cc-b.cc),oe(y.startPTS)&&oe(y.endPTS)&&(b.start=b.startPTS=y.startPTS,b.startDTS=y.startDTS,b.maxStartPTS=y.maxStartPTS,b.endPTS=y.endPTS,b.endDTS=y.endDTS,b.minEndPTS=y.minEndPTS,b.duration=y.endPTS-y.startPTS,b.duration&&(o=b),a.PTSKnown=a.alignedSliding=!0),b.elementaryStreams=y.elementaryStreams,b.loader=y.loader,b.stats=y.stats,b.urlId=y.urlId,y.initSegment&&(b.initSegment=y.initSegment,r=y.initSegment)}),r){var l=a.fragmentHint?a.fragments.concat(a.fragmentHint):a.fragments;l.forEach(function(y){var b;(!y.initSegment||y.initSegment.relurl===((b=r)==null?void 0:b.relurl))&&(y.initSegment=r)})}if(a.skippedSegments)if(a.deltaUpdateFailed=a.fragments.some(function(y){return!y}),a.deltaUpdateFailed){P.warn("[level-helper] Previous playlist missing segments skipped in delta playlist");for(var d=a.skippedSegments;d--;)a.fragments.shift();a.startSN=a.fragments[0].sn,a.startCC=a.fragments[0].cc}else a.canSkipDateRanges&&(a.dateRanges=ia(s.dateRanges,a.dateRanges,a.recentlyRemovedDateranges));var u=a.fragments;if(i){P.warn("discontinuity sliding from playlist, take drift into account");for(var c=0;c<u.length;c++)u[c].cc+=i}a.skippedSegments&&(a.startCC=a.fragments[0].cc),aa(s.partList,a.partList,function(y,b){b.elementaryStreams=y.elementaryStreams,b.stats=y.stats}),o?ui(a,o,o.startPTS,o.endPTS,o.startDTS,o.endDTS):ci(s,a),u.length&&(a.totalduration=a.edge-u[0].start),a.driftStartTime=s.driftStartTime,a.driftStart=s.driftStart;var p=a.advancedDateTime;if(a.advanced&&p){var g=a.edge;a.driftStart||(a.driftStartTime=p,a.driftStart=g),a.driftEndTime=p,a.driftEnd=g}else a.driftEndTime=s.driftEndTime,a.driftEnd=s.driftEnd,a.advancedDateTime=s.advancedDateTime}function ia(s,a,r){var e=q({},s);return r&&r.forEach(function(t){delete e[t]}),Object.keys(a).forEach(function(t){var n=new Rn(a[t].attr,e[t]);n.isValid?e[t]=n:P.warn('Ignoring invalid Playlist Delta Update DATERANGE tag: "'+JSON.stringify(a[t].attr)+'"')}),e}function aa(s,a,r){if(s&&a)for(var e=0,t=0,n=s.length;t<=n;t++){var i=s[t],o=a[t+e];i&&o&&i.index===o.index&&i.fragment.sn===o.fragment.sn?r(i,o):e--}}function oa(s,a,r){for(var e=a.skippedSegments,t=Math.max(s.startSN,a.startSN)-a.startSN,n=(s.fragmentHint?1:0)+(e?a.endSN:Math.min(s.endSN,a.endSN))-a.startSN,i=a.startSN-s.startSN,o=a.fragmentHint?a.fragments.concat(a.fragmentHint):a.fragments,l=s.fragmentHint?s.fragments.concat(s.fragmentHint):s.fragments,d=t;d<=n;d++){var u=l[i+d],c=o[d];e&&!c&&d<e&&(c=a.fragments[d]=u),u&&c&&r(u,c)}}function ci(s,a){var r=a.startSN+a.skippedSegments-s.startSN,e=s.fragments;r<0||r>=e.length||wr(a,e[r].start)}function wr(s,a){if(a){for(var r=s.fragments,e=s.skippedSegments;e<r.length;e++)r[e].start+=a;s.fragmentHint&&(s.fragmentHint.start+=a)}}function sa(s,a){a===void 0&&(a=Infinity);var r=1e3*s.targetduration;if(s.updated){var e=s.fragments,t=4;if(e.length&&r*t>a){var n=e[e.length-1].duration*1e3;n<r&&(r=n)}}else r/=2;return Math.round(r)}function la(s,a,r){if(!(s!=null&&s.details))return null;var e=s.details,t=e.fragments[a-e.startSN];return t||(t=e.fragmentHint,t&&t.sn===a)?t:a<e.startSN&&r&&r.sn===a?r:null}function fi(s,a,r){var e;return s!=null&&s.details?hi((e=s.details)==null?void 0:e.partList,a,r):null}function hi(s,a,r){if(s)for(var e=s.length;e--;){var t=s[e];if(t.index===r&&t.fragment.sn===a)return t}return null}function zn(s){switch(s.details){case U.FRAG_LOAD_TIMEOUT:case U.KEY_LOAD_TIMEOUT:case U.LEVEL_LOAD_TIMEOUT:case U.MANIFEST_LOAD_TIMEOUT:return!0}return!1}function pi(s,a){var r=zn(a);return s.default[(r?"timeout":"error")+"Retry"]}function Lr(s,a){var r=s.backoff==="linear"?1:Math.pow(2,a);return Math.min(r*s.retryDelayMs,s.maxRetryDelayMs)}function vi(s){return Ke(Ke({},s),{errorRetry:null,timeoutRetry:null})}function Vn(s,a,r,e){return!!s&&a<s.maxNumRetry&&(da(e)||!!r)}function da(s){return s===0&&navigator.onLine===!1||!!s&&(s<400||s>499)}var mi={search:function(a,r){for(var e=0,t=a.length-1,n=null,i=null;e<=t;){n=(e+t)/2|0,i=a[n];var o=r(i);if(o>0)e=n+1;else if(o<0)t=n-1;else return i}return null}};function ua(s,a,r){if(a===null||!Array.isArray(s)||!s.length||!oe(a))return null;var e=s[0].programDateTime;if(a<(e||0))return null;var t=s[s.length-1].endProgramDateTime;if(a>=(t||0))return null;r=r||0;for(var n=0;n<s.length;++n){var i=s[n];if(ca(a,r,i))return i}return null}function gn(s,a,r,e){r===void 0&&(r=0),e===void 0&&(e=0);var t=null;if(s?t=a[s.sn-a[0].sn+1]||null:r===0&&a[0].start===0&&(t=a[0]),t&&Ir(r,e,t)===0)return t;var n=mi.search(a,Ir.bind(null,r,e));return n&&(n!==s||!t)?n:t}function Ir(s,a,r){if(s===void 0&&(s=0),a===void 0&&(a=0),r.start<=s&&r.start+r.duration>s)return 0;var e=Math.min(a,r.duration+(r.deltaPTS?r.deltaPTS:0));return r.start+r.duration-e<=s?1:r.start-e>s&&r.start?-1:0}function ca(s,a,r){var e=Math.min(a,r.duration+(r.deltaPTS?r.deltaPTS:0))*1e3,t=r.endProgramDateTime||0;return t-e>s}function fa(s,a){return mi.search(s,function(r){return r.cc<a?1:r.cc>a?-1:0})}var ha=3e5,ze={DoNothing:0,SendEndCallback:1,SendAlternateToPenaltyBox:2,RemoveAlternatePermanently:3,InsertDiscontinuity:4,RetryRequest:5},Oe={None:0,MoveAllAlternatesMatchingHost:1,MoveAllAlternatesMatchingHDCP:2,SwitchToSDR:4},pa=function(){function s(r){this.hls=void 0,this.playlistError=0,this.penalizedRenditions={},this.log=void 0,this.warn=void 0,this.error=void 0,this.hls=r,this.log=P.log.bind(P,"[info]:"),this.warn=P.warn.bind(P,"[warning]:"),this.error=P.error.bind(P,"[error]:"),this.registerListeners()}var a=s.prototype;return a.registerListeners=function(){var e=this.hls;e.on(S.ERROR,this.onError,this),e.on(S.MANIFEST_LOADING,this.onManifestLoading,this)},a.unregisterListeners=function(){var e=this.hls;!e||(e.off(S.ERROR,this.onError,this),e.off(S.ERROR,this.onErrorOut,this),e.off(S.MANIFEST_LOADING,this.onManifestLoading,this))},a.destroy=function(){this.unregisterListeners(),this.hls=null,this.penalizedRenditions={}},a.startLoad=function(e){this.playlistError=0},a.stopLoad=function(){},a.getVariantLevelIndex=function(e){return(e==null?void 0:e.type)===he.MAIN?e.level:this.hls.loadLevel},a.onManifestLoading=function(){this.playlistError=0,this.penalizedRenditions={}},a.onError=function(e,t){var n;if(!t.fatal){var i=this.hls,o=t.context;switch(t.details){case U.FRAG_LOAD_ERROR:case U.FRAG_LOAD_TIMEOUT:case U.KEY_LOAD_ERROR:case U.KEY_LOAD_TIMEOUT:t.errorAction=this.getFragRetryOrSwitchAction(t);return;case U.FRAG_GAP:case U.FRAG_PARSING_ERROR:case U.FRAG_DECRYPT_ERROR:{t.errorAction=this.getFragRetryOrSwitchAction(t),t.errorAction.action=ze.SendAlternateToPenaltyBox;return}case U.LEVEL_EMPTY_ERROR:case U.LEVEL_PARSING_ERROR:{var l,d,u=t.parent===he.MAIN?t.level:i.loadLevel;t.details===U.LEVEL_EMPTY_ERROR&&!!((l=t.context)!=null&&(d=l.levelDetails)!=null&&d.live)?t.errorAction=this.getPlaylistRetryOrSwitchAction(t,u):(t.levelRetry=!1,t.errorAction=this.getLevelSwitchAction(t,u))}return;case U.LEVEL_LOAD_ERROR:case U.LEVEL_LOAD_TIMEOUT:typeof(o==null?void 0:o.level)=="number"&&(t.errorAction=this.getPlaylistRetryOrSwitchAction(t,o.level));return;case U.AUDIO_TRACK_LOAD_ERROR:case U.AUDIO_TRACK_LOAD_TIMEOUT:case U.SUBTITLE_LOAD_ERROR:case U.SUBTITLE_TRACK_LOAD_TIMEOUT:if(o){var c=i.levels[i.loadLevel];if(c&&(o.type===be.AUDIO_TRACK&&o.groupId===c.audioGroupId||o.type===be.SUBTITLE_TRACK&&o.groupId===c.textGroupId)){t.errorAction=this.getPlaylistRetryOrSwitchAction(t,i.loadLevel),t.errorAction.action=ze.SendAlternateToPenaltyBox,t.errorAction.flags=Oe.MoveAllAlternatesMatchingHost;return}}return;case U.KEY_SYSTEM_STATUS_OUTPUT_RESTRICTED:{var p=i.levels[i.loadLevel],g=p==null?void 0:p.attrs["HDCP-LEVEL"];g&&(t.errorAction={action:ze.SendAlternateToPenaltyBox,flags:Oe.MoveAllAlternatesMatchingHDCP,hdcpLevel:g})}return;case U.BUFFER_ADD_CODEC_ERROR:case U.REMUX_ALLOC_ERROR:t.errorAction=this.getLevelSwitchAction(t,(n=t.level)!=null?n:i.loadLevel);return;case U.INTERNAL_EXCEPTION:case U.BUFFER_APPENDING_ERROR:case U.BUFFER_APPEND_ERROR:case U.BUFFER_FULL_ERROR:case U.LEVEL_SWITCH_ERROR:case U.BUFFER_STALLED_ERROR:case U.BUFFER_SEEK_OVER_HOLE:case U.BUFFER_NUDGE_ON_STALL:t.errorAction={action:ze.DoNothing,flags:Oe.None};return}if(t.type===fe.KEY_SYSTEM_ERROR){var y=this.getVariantLevelIndex(t.frag);t.levelRetry=!1,t.errorAction=this.getLevelSwitchAction(t,y);return}}},a.getPlaylistRetryOrSwitchAction=function(e,t){var n,i,o=this.hls,l=pi(o.config.playlistLoadPolicy,e),d=this.playlistError++,u=(n=e.response)==null?void 0:n.code,c=Vn(l,d,zn(e),u);return c?{action:ze.RetryRequest,flags:Oe.None,retryConfig:l,retryCount:d}:(i=e.context)!=null&&i.deliveryDirectives?{action:ze.DoNothing,flags:Oe.None,retryConfig:l||{maxNumRetry:0,retryDelayMs:0,maxRetryDelayMs:0},retryCount:d}:this.getLevelSwitchAction(e,t)},a.getFragRetryOrSwitchAction=function(e){var t=this.hls,n=this.getVariantLevelIndex(e.frag),i=t.levels[n],o=t.config,l=o.fragLoadPolicy,d=o.keyLoadPolicy,u=pi(e.details.startsWith("key")?d:l,e),c=t.levels.reduce(function(E,C){return E+C.fragmentError},0);if(i){var p;e.details!==U.FRAG_GAP&&i.fragmentError++;var g=(p=e.response)==null?void 0:p.code,y=Vn(u,c,zn(e),g);if(y)return{action:ze.RetryRequest,flags:Oe.None,retryConfig:u,retryCount:c}}var b=this.getLevelSwitchAction(e,n);return u&&(b.retryConfig=u,b.retryCount=c),b},a.getLevelSwitchAction=function(e,t){var n=this.hls;t==null&&(t=n.loadLevel);var i=this.hls.levels[t];if(i&&(i.loadError++,n.autoLevelEnabled)){for(var o,l,d=-1,u=n.levels,c=(o=e.frag)==null?void 0:o.type,p=(l=e.context)!=null?l:{},g=p.type,y=p.groupId,b=u.length;b--;){var E=(b+n.loadLevel)%u.length;if(E!==n.loadLevel&&u[E].loadError===0){var C=u[E];if(e.details===U.FRAG_GAP&&e.frag){var w=u[E].details;if(w){var k=gn(e.frag,w.fragments,e.frag.start);if(k!=null&&k.gap)continue}}else{if(g===be.AUDIO_TRACK&&y===C.audioGroupId||g===be.SUBTITLE_TRACK&&y===C.textGroupId)continue;if(c===he.AUDIO&&i.audioGroupId===C.audioGroupId||c===he.SUBTITLE&&i.textGroupId===C.textGroupId)continue}d=E;break}}if(d>-1&&n.loadLevel!==d)return e.levelRetry=!0,{action:ze.SendAlternateToPenaltyBox,flags:Oe.None,nextAutoLevel:d}}return{action:ze.SendAlternateToPenaltyBox,flags:Oe.MoveAllAlternatesMatchingHost}},a.onErrorOut=function(e,t){var n;switch((n=t.errorAction)==null?void 0:n.action){case ze.DoNothing:break;case ze.SendAlternateToPenaltyBox:this.sendAlternateToPenaltyBox(t),!t.errorAction.resolved&&t.details!==U.FRAG_GAP&&(t.fatal=!0);break}if(t.fatal){this.hls.stopLoad();return}},a.sendAlternateToPenaltyBox=function(e){var t=this.hls,n=e.errorAction;if(!!n){var i=n.flags,o=n.hdcpLevel,l=n.nextAutoLevel;switch(i){case Oe.None:this.switchLevel(e,l);break;case Oe.MoveAllAlternatesMatchingHost:n.resolved||(n.resolved=this.redundantFailover(e));break;case Oe.MoveAllAlternatesMatchingHDCP:o&&(t.maxHdcpLevel=Cr[Cr.indexOf(o)-1],n.resolved=!0),this.warn('Restricting playback to HDCP-LEVEL of "'+t.maxHdcpLevel+'" or lower');break}n.resolved||this.switchLevel(e,l)}},a.switchLevel=function(e,t){t!==void 0&&e.errorAction&&(this.warn("switching to level "+t+" after "+e.details),this.hls.nextAutoLevel=t,e.errorAction.resolved=!0,this.hls.nextLoadLevel=this.hls.nextAutoLevel)},a.redundantFailover=function(e){var t=this,n=this.hls,i=this.penalizedRenditions,o=e.parent===he.MAIN?e.level:n.loadLevel,l=n.levels[o],d=l.url.length,u=e.frag?e.frag.urlId:l.urlId;l.urlId===u&&(!e.frag||l.details)&&this.penalizeRendition(l,e);for(var c=function(){var b=(u+p)%d,E=i[b];if(!E||va(E,e,i[u]))return t.warn("Switching to Redundant Stream "+(b+1)+"/"+d+': "'+l.url[b]+'" after '+e.details),t.playlistError=0,n.levels.forEach(function(C){C.urlId=b}),n.nextLoadLevel=o,{v:!0}},p=1;p<d;p++){var g=c();if(typeof g=="object")return g.v}return!1},a.penalizeRendition=function(e,t){var n=this.penalizedRenditions,i=n[e.urlId]||{lastErrorPerfMs:0,errors:[],details:void 0};i.lastErrorPerfMs=performance.now(),i.errors.push(t),i.details=e.details,n[e.urlId]=i},s}();function va(s,a,r){if(performance.now()-s.lastErrorPerfMs>ha)return!0;var e=s.details;if(a.details===U.FRAG_GAP&&e&&a.frag){var t=a.frag.start,n=gn(null,e.fragments,t);if(n&&!n.gap)return!0}if(r&&s.errors.length<r.errors.length){var i=s.errors[s.errors.length-1];if(e&&i.frag&&a.frag&&Math.abs(i.frag.start-a.frag.start)>e.targetduration*3)return!0}return!1}var wt=function(){function s(r,e){this.hls=void 0,this.timer=-1,this.requestScheduled=-1,this.canLoad=!1,this.log=void 0,this.warn=void 0,this.log=P.log.bind(P,e+":"),this.warn=P.warn.bind(P,e+":"),this.hls=r}var a=s.prototype;return a.destroy=function(){this.clearTimer(),this.hls=this.log=this.warn=null},a.clearTimer=function(){clearTimeout(this.timer),this.timer=-1},a.startLoad=function(){this.canLoad=!0,this.requestScheduled=-1,this.loadPlaylist()},a.stopLoad=function(){this.canLoad=!1,this.clearTimer()},a.switchParams=function(e,t){var n=t==null?void 0:t.renditionReports;if(n){for(var i=-1,o=0;o<n.length;o++){var l=n[o],d=void 0;try{d=new self.URL(l.URI,t.url).href}catch(y){P.warn("Could not construct new URL for Rendition Report: "+y),d=l.URI||""}if(d===e){i=o;break}else d===e.substring(0,d.length)&&(i=o)}if(i!==-1){var u=n[i],c=parseInt(u["LAST-MSN"])||(t==null?void 0:t.lastPartSn),p=parseInt(u["LAST-PART"])||(t==null?void 0:t.lastPartIndex);if(this.hls.config.lowLatencyMode){var g=Math.min(t.age-t.partTarget,t.targetduration);p>=0&&g>t.partTarget&&(p+=1)}return new di(c,p>=0?p:void 0,vn.No)}}},a.loadPlaylist=function(e){this.requestScheduled===-1&&(this.requestScheduled=self.performance.now())},a.shouldLoadPlaylist=function(e){return this.canLoad&&!!e&&!!e.url&&(!e.details||e.details.live)},a.shouldReloadPlaylist=function(e){return this.timer===-1&&this.requestScheduled===-1&&this.shouldLoadPlaylist(e)},a.playlistLoaded=function(e,t,n){var i=this,o=t.details,l=t.stats,d=self.performance.now(),u=l.loading.first?Math.max(0,d-l.loading.first):0;if(o.advancedDateTime=Date.now()-u,o.live||n!=null&&n.live){if(o.reloaded(n),n&&this.log("live playlist "+e+" "+(o.advanced?"REFRESHED "+o.lastPartSn+"-"+o.lastPartIndex:"MISSED")),n&&o.fragments.length>0&&ra(n,o),!this.canLoad||!o.live)return;var c,p=void 0,g=void 0;if(o.canBlockReload&&o.endSN&&o.advanced){var y=this.hls.config.lowLatencyMode,b=o.lastPartSn,E=o.endSN,C=o.lastPartIndex,w=C!==-1,k=b===E,D=y?0:C;w?(p=k?E+1:b,g=k?D:C+1):p=E+1;var _=o.age,F=_+o.ageHeader,O=Math.min(F-o.partTarget,o.targetduration*1.5);if(O>0){if(n&&O>n.tuneInGoal)this.warn("CDN Tune-in goal increased from: "+n.tuneInGoal+" to: "+O+" with playlist age: "+o.age),O=0;else{var M=Math.floor(O/o.targetduration);if(p+=M,g!==void 0){var z=Math.round(O%o.targetduration/o.partTarget);g+=z}this.log("CDN Tune-in age: "+o.ageHeader+"s last advanced "+_.toFixed(2)+"s goal: "+O+" skip sn "+M+" to part "+g)}o.tuneInGoal=O}if(c=this.getDeliveryDirectives(o,t.deliveryDirectives,p,g),y||!k){this.loadPlaylist(c);return}}else o.canBlockReload&&(c=this.getDeliveryDirectives(o,t.deliveryDirectives,p,g));var H=this.hls.mainForwardBufferInfo,G=H?H.end-H.len:0,te=(o.edge-G)*1e3,ye=sa(o,te);o.updated&&d>this.requestScheduled+ye&&(this.requestScheduled=l.loading.start),p!==void 0&&o.canBlockReload?this.requestScheduled=l.loading.first+ye-(o.partTarget*1e3||1e3):this.requestScheduled===-1||this.requestScheduled+ye<d?this.requestScheduled=d:this.requestScheduled-d<=0&&(this.requestScheduled+=ye);var Ae=this.requestScheduled-d;Ae=Math.max(0,Ae),this.log("reload live playlist "+e+" in "+Math.round(Ae)+" ms"),this.timer=self.setTimeout(function(){return i.loadPlaylist(c)},Ae)}else this.clearTimer()},a.getDeliveryDirectives=function(e,t,n,i){var o=na(e,n);return t!=null&&t.skip&&e.deltaUpdateFailed&&(n=t.msn,i=t.part,o=vn.No),new di(n,i,o)},a.checkRetry=function(e){var t=this,n=e.details,i=zn(e),o=e.errorAction,l=o||{},d=l.action,u=l.retryCount,c=u===void 0?0:u,p=l.retryConfig,g=d===ze.RetryRequest&&!!o&&!!p;if(g){var y;if(this.requestScheduled=-1,i&&(y=e.context)!=null&&y.deliveryDirectives)this.warn("Retrying playlist loading "+(c+1)+"/"+p.maxNumRetry+' after "'+n+'" without delivery-directives'),this.loadPlaylist();else{var b=Lr(p,c);this.timer=self.setTimeout(function(){return t.loadPlaylist()},b),this.warn("Retrying playlist loading "+(c+1)+"/"+p.maxNumRetry+' after "'+n+'" in '+b+"ms")}e.levelRetry=!0,o.resolved=!0}return g},s}(),jn,ma=function(s){j(a,s);function a(e,t){var n;return n=s.call(this,e,"[level-controller]")||this,n._levels=[],n._firstLevel=-1,n._startLevel=void 0,n.currentLevel=null,n.currentLevelIndex=-1,n.manualLevelIndex=-1,n.steering=void 0,n.onParsedComplete=void 0,n.steering=t,n._registerListeners(),n}var r=a.prototype;return r._registerListeners=function(){var t=this.hls;t.on(S.MANIFEST_LOADING,this.onManifestLoading,this),t.on(S.MANIFEST_LOADED,this.onManifestLoaded,this),t.on(S.LEVEL_LOADED,this.onLevelLoaded,this),t.on(S.LEVELS_UPDATED,this.onLevelsUpdated,this),t.on(S.AUDIO_TRACK_SWITCHED,this.onAudioTrackSwitched,this),t.on(S.FRAG_LOADED,this.onFragLoaded,this),t.on(S.ERROR,this.onError,this)},r._unregisterListeners=function(){var t=this.hls;t.off(S.MANIFEST_LOADING,this.onManifestLoading,this),t.off(S.MANIFEST_LOADED,this.onManifestLoaded,this),t.off(S.LEVEL_LOADED,this.onLevelLoaded,this),t.off(S.LEVELS_UPDATED,this.onLevelsUpdated,this),t.off(S.AUDIO_TRACK_SWITCHED,this.onAudioTrackSwitched,this),t.off(S.FRAG_LOADED,this.onFragLoaded,this),t.off(S.ERROR,this.onError,this)},r.destroy=function(){this._unregisterListeners(),this.steering=null,this.resetLevels(),s.prototype.destroy.call(this)},r.startLoad=function(){var t=this._levels;t.forEach(function(n){n.loadError=0,n.fragmentError=0}),s.prototype.startLoad.call(this)},r.resetLevels=function(){this._startLevel=void 0,this.manualLevelIndex=-1,this.currentLevelIndex=-1,this.currentLevel=null,this._levels=[]},r.onManifestLoading=function(t,n){this.resetLevels()},r.onManifestLoaded=function(t,n){var i=[],o={},l;n.levels.forEach(function(d){var u,c=d.attrs;((u=d.audioCodec)==null?void 0:u.indexOf("mp4a.40.34"))!==-1&&(jn||(jn=/chrome|firefox/i.test(navigator.userAgent)),jn&&(d.audioCodec=void 0));var p=c.AUDIO,g=c.CODECS,y=c["FRAME-RATE"],b=c["PATHWAY-ID"],E=c.RESOLUTION,C=c.SUBTITLES,w=(b||".")+"-",k=""+w+d.bitrate+"-"+E+"-"+y+"-"+g;l=o[k],l?l.addFallback(d):(l=new mn(d),o[k]=l,i.push(l)),yn(l,"audio",p),yn(l,"text",C)}),this.filterAndSortMediaOptions(i,n)},r.filterAndSortMediaOptions=function(t,n){var i=this,o=[],l=[],d=!1,u=!1,c=!1,p=t.filter(function(k){var D=k.audioCodec,_=k.videoCodec,F=k.width,O=k.height,M=k.unknownCodecs;return d||(d=!!(F&&O)),u||(u=!!_),c||(c=!!D),!(M!=null&&M.length)&&(!D||gr(D,"audio"))&&(!_||gr(_,"video"))});if((d||u)&&c&&(p=p.filter(function(k){var D=k.videoCodec,_=k.width,F=k.height;return!!D||!!(_&&F)})),p.length===0){Promise.resolve().then(function(){if(i.hls){var k=new Error("no level with compatible codecs found in manifest");i.hls.trigger(S.ERROR,{type:fe.MEDIA_ERROR,details:U.MANIFEST_INCOMPATIBLE_CODECS_ERROR,fatal:!0,url:n.url,error:k,reason:k.message})}});return}n.audioTracks&&(o=n.audioTracks.filter(function(k){return!k.audioCodec||gr(k.audioCodec,"audio")}),Lt(o)),n.subtitles&&(l=n.subtitles,Lt(l));var g=p.slice(0);p.sort(function(k,D){return k.attrs["HDCP-LEVEL"]!==D.attrs["HDCP-LEVEL"]?(k.attrs["HDCP-LEVEL"]||"")>(D.attrs["HDCP-LEVEL"]||"")?1:-1:k.bitrate!==D.bitrate?k.bitrate-D.bitrate:k.attrs["FRAME-RATE"]!==D.attrs["FRAME-RATE"]?k.attrs.decimalFloatingPoint("FRAME-RATE")-D.attrs.decimalFloatingPoint("FRAME-RATE"):k.attrs.SCORE!==D.attrs.SCORE?k.attrs.decimalFloatingPoint("SCORE")-D.attrs.decimalFloatingPoint("SCORE"):d&&k.height!==D.height?k.height-D.height:0});var y=g[0];if(this.steering&&(p=this.steering.filterParsedLevels(p),p.length!==g.length)){for(var b=0;b<g.length;b++)if(g[b].pathwayId===p[0].pathwayId){y=g[b];break}}this._levels=p;for(var E=0;E<p.length;E++)if(p[E]===y){this._firstLevel=E,this.log("manifest loaded, "+p.length+" level(s) found, first bitrate: "+y.bitrate);break}var C=c&&!u,w={levels:p,audioTracks:o,subtitleTracks:l,sessionData:n.sessionData,sessionKeys:n.sessionKeys,firstLevel:this._firstLevel,stats:n.stats,audio:c,video:u,altAudio:!C&&o.some(function(k){return!!k.url})};this.hls.trigger(S.MANIFEST_PARSED,w),(this.hls.config.autoStartLoad||this.hls.forceStartLoad)&&this.hls.startLoad(this.hls.config.startPosition)},r.onError=function(t,n){n.fatal||!n.context||n.context.type===be.LEVEL&&n.context.level===this.level&&this.checkRetry(n)},r.onFragLoaded=function(t,n){var i=n.frag;if(i!==void 0&&i.type===he.MAIN){var o=this._levels[i.level];o!==void 0&&(o.loadError=0)}},r.onLevelLoaded=function(t,n){var i,o=n.level,l=n.details,d=this._levels[o];if(!d){var u;this.warn("Invalid level index "+o),(u=n.deliveryDirectives)!=null&&u.skip&&(l.deltaUpdateFailed=!0);return}o===this.currentLevelIndex?(d.fragmentError===0&&(d.loadError=0),this.playlistLoaded(o,n,d.details)):(i=n.deliveryDirectives)!=null&&i.skip&&(l.deltaUpdateFailed=!0)},r.onAudioTrackSwitched=function(t,n){var i=this.currentLevel;if(!!i){var o=this.hls.audioTracks[n.id].groupId;if(i.audioGroupIds&&i.audioGroupId!==o){for(var l=-1,d=0;d<i.audioGroupIds.length;d++)if(i.audioGroupIds[d]===o){l=d;break}l!==-1&&l!==i.urlId&&(i.urlId=l,this.canLoad&&this.startLoad())}}},r.loadPlaylist=function(t){s.prototype.loadPlaylist.call(this);var n=this.currentLevelIndex,i=this.currentLevel;if(i&&this.shouldLoadPlaylist(i)){var o=i.urlId,l=i.uri;if(t)try{l=t.addDirectives(l)}catch(u){this.warn("Could not construct new URL with HLS Delivery Directives: "+u)}var d=i.attrs["PATHWAY-ID"];this.log("Loading level index "+n+((t==null?void 0:t.msn)!==void 0?" at sn "+t.msn+" part "+t.part:"")+" with"+(d?" Pathway "+d:"")+" URI "+(o+1)+"/"+i.url.length+" "+l),this.clearTimer(),this.hls.trigger(S.LEVEL_LOADING,{url:l,level:n,id:o,deliveryDirectives:t||null})}},r.removeLevel=function(t,n){var i=this,o=function(u,c){return c!==n},l=this._levels.filter(function(d,u){return u!==t?!0:d.url.length>1&&n!==void 0?(d.url=d.url.filter(o),d.audioGroupIds&&(d.audioGroupIds=d.audioGroupIds.filter(o)),d.textGroupIds&&(d.textGroupIds=d.textGroupIds.filter(o)),d.urlId=0,!0):(i.steering&&i.steering.removeLevel(d),!1)});this.hls.trigger(S.LEVELS_UPDATED,{levels:l})},r.onLevelsUpdated=function(t,n){var i=n.levels;i.forEach(function(o,l){var d=o.details;d!=null&&d.fragments&&d.fragments.forEach(function(u){u.level=l})}),this._levels=i},Q(a,[{key:"levels",get:function(){return this._levels.length===0?null:this._levels}},{key:"level",get:function(){return this.currentLevelIndex},set:function(t){var n=this._levels;if(n.length!==0){if(t<0||t>=n.length){var i=new Error("invalid level idx"),o=t<0;if(this.hls.trigger(S.ERROR,{type:fe.OTHER_ERROR,details:U.LEVEL_SWITCH_ERROR,level:t,fatal:o,error:i,reason:i.message}),o)return;t=Math.min(t,n.length-1)}var l=this.currentLevelIndex,d=this.currentLevel,u=d?d.attrs["PATHWAY-ID"]:void 0,c=n[t],p=c.attrs["PATHWAY-ID"];if(this.currentLevelIndex=t,this.currentLevel=c,!(l===t&&c.details&&d&&u===p)){this.log("Switching to level "+t+(p?" with Pathway "+p:"")+" from level "+l+(u?" with Pathway "+u:""));var g=q({},c,{level:t,maxBitrate:c.maxBitrate,attrs:c.attrs,uri:c.uri,urlId:c.urlId});delete g._attrs,delete g._urlId,this.hls.trigger(S.LEVEL_SWITCHING,g);var y=c.details;if(!y||y.live){var b=this.switchParams(c.uri,d==null?void 0:d.details);this.loadPlaylist(b)}}}}},{key:"manualLevel",get:function(){return this.manualLevelIndex},set:function(t){this.manualLevelIndex=t,this._startLevel===void 0&&(this._startLevel=t),t!==-1&&(this.level=t)}},{key:"firstLevel",get:function(){return this._firstLevel},set:function(t){this._firstLevel=t}},{key:"startLevel",get:function(){if(this._startLevel===void 0){var t=this.hls.config.startLevel;return t!==void 0?t:this._firstLevel}else return this._startLevel},set:function(t){this._startLevel=t}},{key:"nextLoadLevel",get:function(){return this.manualLevelIndex!==-1?this.manualLevelIndex:this.hls.nextAutoLevel},set:function(t){this.level=t,this.manualLevelIndex===-1&&(this.hls.nextAutoLevel=t)}}]),a}(wt);function yn(s,a,r){!r||(a==="audio"?(s.audioGroupIds||(s.audioGroupIds=[]),s.audioGroupIds[s.url.length-1]=r):a==="text"&&(s.textGroupIds||(s.textGroupIds=[]),s.textGroupIds[s.url.length-1]=r))}function Lt(s){var a={};s.forEach(function(r){var e=r.groupId||"";r.id=a[e]=a[e]||0,a[e]++})}var Ye={NOT_LOADED:"NOT_LOADED",APPENDING:"APPENDING",PARTIAL:"PARTIAL",OK:"OK"},ga=function(){function s(r){this.mainFragEntity=null,this.activeParts=null,this.endListFragments=Object.create(null),this.fragments=Object.create(null),this.timeRanges=Object.create(null),this.bufferPadding=.2,this.hls=void 0,this.hasGaps=!1,this.hls=r,this._registerListeners()}var a=s.prototype;return a._registerListeners=function(){var e=this.hls;e.on(S.BUFFER_APPENDED,this.onBufferAppended,this),e.on(S.FRAG_BUFFERED,this.onFragBuffered,this),e.on(S.FRAG_LOADED,this.onFragLoaded,this)},a._unregisterListeners=function(){var e=this.hls;e.off(S.BUFFER_APPENDED,this.onBufferAppended,this),e.off(S.FRAG_BUFFERED,this.onFragBuffered,this),e.off(S.FRAG_LOADED,this.onFragLoaded,this)},a.destroy=function(){this._unregisterListeners(),this.fragments=this.endListFragments=this.timeRanges=this.mainFragEntity=this.activeParts=null},a.getAppendedFrag=function(e,t){if(t===he.MAIN){var n=this.mainFragEntity,i=this.activeParts;if(n){if(n&&i)for(var o=i.length;o--;){var l=i[o],d=l?l.end:n.appendedPTS;if(l.start<=e&&d!==null&&e<=d)return o>9&&(this.activeParts=i.slice(o-9)),l}else if(n.body.start<=e&&n.appendedPTS!==null&&e<=n.appendedPTS)return n.body}}return this.getBufferedFrag(e,t)},a.getBufferedFrag=function(e,t){for(var n=this.fragments,i=Object.keys(n),o=i.length;o--;){var l=n[i[o]];if((l==null?void 0:l.body.type)===t&&l.buffered){var d=l.body;if(d.start<=e&&e<=d.end)return d}}return null},a.detectEvictedFragments=function(e,t,n){var i=this;this.timeRanges&&(this.timeRanges[e]=t),Object.keys(this.fragments).forEach(function(o){var l=i.fragments[o];if(!!l){if(!l.buffered&&!l.loaded){l.body.type===n&&i.removeFragment(l.body);return}var d=l.range[e];!d||d.time.some(function(u){var c=!i.isTimeBuffered(u.startPTS,u.endPTS,t);return c&&i.removeFragment(l.body),c})}})},a.detectPartialFragments=function(e){var t=this,n=this.timeRanges,i=e.frag,o=e.part;if(!(!n||i.sn==="initSegment")){var l=$e(i),d=this.fragments[l];!d||(Object.keys(n).forEach(function(u){var c=i.elementaryStreams[u];if(!!c){var p=n[u],g=o!==null||c.partial===!0;d.range[u]=t.getBufferedTimes(i,o,g,p)}}),d.loaded=null,Object.keys(d.range).length?(d.buffered=!0,d.body.endList&&(this.endListFragments[d.body.type]=d)):this.removeFragment(d.body))}},a.fragBuffered=function(e,t){var n=$e(e),i=this.fragments[n];!i&&t&&(i=this.fragments[n]={body:e,appendedPTS:null,loaded:null,buffered:!1,range:Object.create(null)},e.gap&&(this.hasGaps=!0)),i&&(i.loaded=null,i.buffered=!0)},a.getBufferedTimes=function(e,t,n,i){for(var o={time:[],partial:n},l=t?t.start:e.start,d=t?t.end:e.end,u=e.minEndPTS||d,c=e.maxStartPTS||l,p=0;p<i.length;p++){var g=i.start(p)-this.bufferPadding,y=i.end(p)+this.bufferPadding;if(c>=g&&u<=y){o.time.push({startPTS:Math.max(l,i.start(p)),endPTS:Math.min(d,i.end(p))});break}else if(l<y&&d>g)o.partial=!0,o.time.push({startPTS:Math.max(l,i.start(p)),endPTS:Math.min(d,i.end(p))});else if(d<=g)break}return o},a.getPartialFragment=function(e){var t=null,n,i,o,l=0,d=this.bufferPadding,u=this.fragments;return Object.keys(u).forEach(function(c){var p=u[c];!p||Rr(p)&&(i=p.body.start-d,o=p.body.end+d,e>=i&&e<=o&&(n=Math.min(e-i,o-e),l<=n&&(t=p.body,l=n)))}),t},a.isEndListAppended=function(e){var t=this.endListFragments[e];return t!==void 0&&(t.buffered||Rr(t))},a.getState=function(e){var t=$e(e),n=this.fragments[t];return n?n.buffered?Rr(n)?Ye.PARTIAL:Ye.OK:Ye.APPENDING:Ye.NOT_LOADED},a.isTimeBuffered=function(e,t,n){for(var i,o,l=0;l<n.length;l++){if(i=n.start(l)-this.bufferPadding,o=n.end(l)+this.bufferPadding,e>=i&&t<=o)return!0;if(t<=i)return!1}return!1},a.onFragLoaded=function(e,t){var n=t.frag,i=t.part;if(!(n.sn==="initSegment"||n.bitrateTest||i)){var o=$e(n);this.fragments[o]={body:n,appendedPTS:null,loaded:t,buffered:!1,range:Object.create(null)}}},a.onBufferAppended=function(e,t){var n=this,i=t.frag,o=t.part,l=t.timeRanges,d=this.mainFragEntity;if(i.type===he.MAIN){var u=d?d.body:null;if(u!==i){d&&u&&u.sn!==i.sn&&(d.buffered=!0,this.fragments[$e(u)]=d);var c=$e(i);d=this.mainFragEntity=this.fragments[c]||{body:i,appendedPTS:null,loaded:null,buffered:!1,range:Object.create(null)}}if(o){var p=this.activeParts;p||(this.activeParts=p=[]),p.push(o)}else this.activeParts=null}this.timeRanges=l,Object.keys(l).forEach(function(g){var y=l[g];if(n.detectEvictedFragments(g,y),!o&&d){var b=i.elementaryStreams[g];if(!b)return;for(var E=0;E<y.length;E++){var C=y.end(E);C<=b.endPTS&&C>b.startPTS?d.appendedPTS=Math.max(C,d.appendedPTS||0):d.appendedPTS=b.endPTS}}})},a.onFragBuffered=function(e,t){this.detectPartialFragments(t)},a.hasFragment=function(e){var t=$e(e);return!!this.fragments[t]},a.removeFragmentsInRange=function(e,t,n,i,o){var l=this;i&&!this.hasGaps||Object.keys(this.fragments).forEach(function(d){var u=l.fragments[d];if(!!u){var c=u.body;c.type!==n||i&&!c.gap||c.start<t&&c.end>e&&(u.buffered||o)&&l.removeFragment(c)}})},a.removeFragment=function(e){var t=$e(e);e.stats.loaded=0,e.clearElementaryStreamInfo(),this.mainFragEntity===this.fragments[t]&&(this.mainFragEntity=null),delete this.fragments[t],e.endList&&delete this.endListFragments[e.type]},a.removeAllFragments=function(){this.fragments=Object.create(null),this.endListFragments=Object.create(null),this.mainFragEntity=null,this.activeParts=null,this.hasGaps=!1},s}();function Rr(s){var a,r;return s.buffered&&(s.body.gap||((a=s.range.video)==null?void 0:a.partial)||((r=s.range.audio)==null?void 0:r.partial))}function $e(s){return s.type+"_"+s.level+"_"+s.urlId+"_"+s.sn}var gi=Math.pow(2,17),ya=function(){function s(r){this.config=void 0,this.loader=null,this.partLoadTimeout=-1,this.config=r}var a=s.prototype;return a.destroy=function(){this.loader&&(this.loader.destroy(),this.loader=null)},a.abort=function(){this.loader&&this.loader.abort()},a.load=function(e,t){var n=this,i=e.url;if(!i)return Promise.reject(new ht({type:fe.NETWORK_ERROR,details:U.FRAG_LOAD_ERROR,fatal:!1,frag:e,error:new Error("Fragment does not have a "+(i?"part list":"url")),networkDetails:null}));this.abort();var o=this.config,l=o.fLoader,d=o.loader;return new Promise(function(u,c){if(n.loader&&n.loader.destroy(),e.gap){c(yi(e));return}var p=n.loader=e.loader=l?new l(o):new d(o),g=Wt(e),y=vi(o.fragLoadPolicy.default),b={loadPolicy:y,timeout:y.maxLoadTimeMs,maxRetry:0,retryDelay:0,maxRetryDelay:0,highWaterMark:e.sn==="initSegment"?Infinity:gi};e.stats=p.stats,p.load(g,b,{onSuccess:function(C,w,k,D){n.resetLoader(e,p);var _=C.data;k.resetIV&&e.decryptdata&&(e.decryptdata.iv=new Uint8Array(_.slice(0,16)),_=_.slice(16)),u({frag:e,part:null,payload:_,networkDetails:D})},onError:function(C,w,k,D){n.resetLoader(e,p),c(new ht({type:fe.NETWORK_ERROR,details:U.FRAG_LOAD_ERROR,fatal:!1,frag:e,response:Ke({url:i,data:void 0},C),error:new Error("HTTP Error "+C.code+" "+C.text),networkDetails:k,stats:D}))},onAbort:function(C,w,k){n.resetLoader(e,p),c(new ht({type:fe.NETWORK_ERROR,details:U.INTERNAL_ABORTED,fatal:!1,frag:e,error:new Error("Aborted"),networkDetails:k,stats:C}))},onTimeout:function(C,w,k){n.resetLoader(e,p),c(new ht({type:fe.NETWORK_ERROR,details:U.FRAG_LOAD_TIMEOUT,fatal:!1,frag:e,error:new Error("Timeout after "+b.timeout+"ms"),networkDetails:k,stats:C}))},onProgress:function(C,w,k,D){t&&t({frag:e,part:null,payload:k,networkDetails:D})}})})},a.loadPart=function(e,t,n){var i=this;this.abort();var o=this.config,l=o.fLoader,d=o.loader;return new Promise(function(u,c){if(i.loader&&i.loader.destroy(),e.gap||t.gap){c(yi(e,t));return}var p=i.loader=e.loader=l?new l(o):new d(o),g=Wt(e,t),y=vi(o.fragLoadPolicy.default),b={loadPolicy:y,timeout:y.maxLoadTimeMs,maxRetry:0,retryDelay:0,maxRetryDelay:0,highWaterMark:gi};t.stats=p.stats,p.load(g,b,{onSuccess:function(C,w,k,D){i.resetLoader(e,p),i.updateStatsFromPart(e,t);var _={frag:e,part:t,payload:C.data,networkDetails:D};n(_),u(_)},onError:function(C,w,k,D){i.resetLoader(e,p),c(new ht({type:fe.NETWORK_ERROR,details:U.FRAG_LOAD_ERROR,fatal:!1,frag:e,part:t,response:Ke({url:g.url,data:void 0},C),error:new Error("HTTP Error "+C.code+" "+C.text),networkDetails:k,stats:D}))},onAbort:function(C,w,k){e.stats.aborted=t.stats.aborted,i.resetLoader(e,p),c(new ht({type:fe.NETWORK_ERROR,details:U.INTERNAL_ABORTED,fatal:!1,frag:e,part:t,error:new Error("Aborted"),networkDetails:k,stats:C}))},onTimeout:function(C,w,k){i.resetLoader(e,p),c(new ht({type:fe.NETWORK_ERROR,details:U.FRAG_LOAD_TIMEOUT,fatal:!1,frag:e,part:t,error:new Error("Timeout after "+b.timeout+"ms"),networkDetails:k,stats:C}))}})})},a.updateStatsFromPart=function(e,t){var n=e.stats,i=t.stats,o=i.total;if(n.loaded+=i.loaded,o){var l=Math.round(e.duration/t.duration),d=Math.min(Math.round(n.loaded/o),l),u=l-d,c=u*Math.round(n.loaded/d);n.total=n.loaded+c}else n.total=Math.max(n.loaded,n.total);var p=n.loading,g=i.loading;p.start?p.first+=g.first-g.start:(p.start=g.start,p.first=g.first),p.end=g.end},a.resetLoader=function(e,t){e.loader=null,this.loader===t&&(self.clearTimeout(this.partLoadTimeout),this.loader=null),t.destroy()},s}();function Wt(s,a){a===void 0&&(a=null);var r=a||s,e={frag:s,part:a,responseType:"arraybuffer",url:r.url,headers:{},rangeStart:0,rangeEnd:0},t=r.byteRangeStartOffset,n=r.byteRangeEndOffset;if(oe(t)&&oe(n)){var i,o=t,l=n;if(s.sn==="initSegment"&&((i=s.decryptdata)==null?void 0:i.method)==="AES-128"){var d=n-t;d%16&&(l=n+(16-d%16)),t!==0&&(e.resetIV=!0,o=t-16)}e.rangeStart=o,e.rangeEnd=l}return e}function yi(s,a){var r=new Error("GAP "+(s.gap?"tag":"attribute")+" found"),e={type:fe.MEDIA_ERROR,details:U.FRAG_GAP,fatal:!1,frag:s,error:r,networkDetails:null};return a&&(e.part=a),(a||s).stats.aborted=!0,new ht(e)}var ht=function(s){j(a,s);function a(r){var e;return e=s.call(this,r.error.message)||this,e.data=void 0,e.data=r,e}return a}(ie(Error)),Aa=function(){function s(r){this.config=void 0,this.keyUriToKeyInfo={},this.emeController=null,this.config=r}var a=s.prototype;return a.abort=function(){for(var e in this.keyUriToKeyInfo){var t=this.keyUriToKeyInfo[e].loader;t&&t.abort()}},a.detach=function(){for(var e in this.keyUriToKeyInfo){var t=this.keyUriToKeyInfo[e];(t.mediaKeySessionContext||t.decryptdata.isCommonEncryption)&&delete this.keyUriToKeyInfo[e]}},a.destroy=function(){this.detach();for(var e in this.keyUriToKeyInfo){var t=this.keyUriToKeyInfo[e].loader;t&&t.destroy()}this.keyUriToKeyInfo={}},a.createKeyLoadError=function(e,t,n,i,o){return t===void 0&&(t=U.KEY_LOAD_ERROR),new ht({type:fe.NETWORK_ERROR,details:t,fatal:!1,frag:e,response:o,error:n,networkDetails:i})},a.loadClear=function(e,t){var n=this;if(this.emeController&&this.config.emeEnabled)for(var i=e.sn,o=e.cc,l=function(){var p=t[d];if(o<=p.cc&&(i==="initSegment"||p.sn==="initSegment"||i<p.sn))return n.emeController.selectKeySystemFormat(p).then(function(g){p.setKeyFormat(g)}),"break"},d=0;d<t.length;d++){var u=l();if(u==="break")break}},a.load=function(e){var t=this;return!e.decryptdata&&e.encrypted&&this.emeController?this.emeController.selectKeySystemFormat(e).then(function(n){return t.loadInternal(e,n)}):this.loadInternal(e)},a.loadInternal=function(e,t){var n,i;t&&e.setKeyFormat(t);var o=e.decryptdata;if(!o){var l=new Error(t?"Expected frag.decryptdata to be defined after setting format "+t:"Missing decryption data on fragment in onKeyLoading");return Promise.reject(this.createKeyLoadError(e,U.KEY_LOAD_ERROR,l))}var d=o.uri;if(!d)return Promise.reject(this.createKeyLoadError(e,U.KEY_LOAD_ERROR,new Error('Invalid key URI: "'+d+'"')));var u=this.keyUriToKeyInfo[d];if((n=u)!=null&&n.decryptdata.key)return o.key=u.decryptdata.key,Promise.resolve({frag:e,keyInfo:u});if((i=u)!=null&&i.keyLoadPromise){var c;switch((c=u.mediaKeySessionContext)==null?void 0:c.keyStatus){case void 0:case"status-pending":case"usable":case"usable-in-future":return u.keyLoadPromise.then(function(p){return o.key=p.keyInfo.decryptdata.key,{frag:e,keyInfo:u}})}}switch(u=this.keyUriToKeyInfo[d]={decryptdata:o,keyLoadPromise:null,loader:null,mediaKeySessionContext:null},o.method){case"ISO-23001-7":case"SAMPLE-AES":case"SAMPLE-AES-CENC":case"SAMPLE-AES-CTR":return o.keyFormat==="identity"?this.loadKeyHTTP(u,e):this.loadKeyEME(u,e);case"AES-128":return this.loadKeyHTTP(u,e);default:return Promise.reject(this.createKeyLoadError(e,U.KEY_LOAD_ERROR,new Error('Key supplied with unsupported METHOD: "'+o.method+'"')))}},a.loadKeyEME=function(e,t){var n={frag:t,keyInfo:e};if(this.emeController&&this.config.emeEnabled){var i=this.emeController.loadKey(n);if(i)return(e.keyLoadPromise=i.then(function(o){return e.mediaKeySessionContext=o,n})).catch(function(o){throw e.keyLoadPromise=null,o})}return Promise.resolve(n)},a.loadKeyHTTP=function(e,t){var n=this,i=this.config,o=i.loader,l=new o(i);return t.keyLoader=e.loader=l,e.keyLoadPromise=new Promise(function(d,u){var c={keyInfo:e,frag:t,responseType:"arraybuffer",url:e.decryptdata.uri},p=i.keyLoadPolicy.default,g={loadPolicy:p,timeout:p.maxLoadTimeMs,maxRetry:0,retryDelay:0,maxRetryDelay:0},y={onSuccess:function(E,C,w,k){var D=w.frag,_=w.keyInfo,F=w.url;if(!D.decryptdata||_!==n.keyUriToKeyInfo[F])return u(n.createKeyLoadError(D,U.KEY_LOAD_ERROR,new Error("after key load, decryptdata unset or changed"),k));_.decryptdata.key=D.decryptdata.key=new Uint8Array(E.data),D.keyLoader=null,_.loader=null,d({frag:D,keyInfo:_})},onError:function(E,C,w,k){n.resetLoader(C),u(n.createKeyLoadError(t,U.KEY_LOAD_ERROR,new Error("HTTP Error "+E.code+" loading key "+E.text),w,Ke({url:c.url,data:void 0},E)))},onTimeout:function(E,C,w){n.resetLoader(C),u(n.createKeyLoadError(t,U.KEY_LOAD_TIMEOUT,new Error("key loading timed out"),w))},onAbort:function(E,C,w){n.resetLoader(C),u(n.createKeyLoadError(t,U.INTERNAL_ABORTED,new Error("key loading aborted"),w))}};l.load(c,g,y)})},a.resetLoader=function(e){var t=e.frag,n=e.keyInfo,i=e.url,o=n.loader;t.keyLoader===o&&(t.keyLoader=null,n.loader=null),delete this.keyUriToKeyInfo[i],o&&o.destroy()},s}(),An=function(){function s(){this._boundTick=void 0,this._tickTimer=null,this._tickInterval=null,this._tickCallCount=0,this._boundTick=this.tick.bind(this)}var a=s.prototype;return a.destroy=function(){this.onHandlerDestroying(),this.onHandlerDestroyed()},a.onHandlerDestroying=function(){this.clearNextTick(),this.clearInterval()},a.onHandlerDestroyed=function(){},a.hasInterval=function(){return!!this._tickInterval},a.hasNextTick=function(){return!!this._tickTimer},a.setInterval=function(e){return this._tickInterval?!1:(this._tickCallCount=0,this._tickInterval=self.setInterval(this._boundTick,e),!0)},a.clearInterval=function(){return this._tickInterval?(self.clearInterval(this._tickInterval),this._tickInterval=null,!0):!1},a.clearNextTick=function(){return this._tickTimer?(self.clearTimeout(this._tickTimer),this._tickTimer=null,!0):!1},a.tick=function(){this._tickCallCount++,this._tickCallCount===1&&(this.doTick(),this._tickCallCount>1&&this.tickImmediate(),this._tickCallCount=0)},a.tickImmediate=function(){this.clearNextTick(),this._tickTimer=self.setTimeout(this._boundTick,0)},a.doTick=function(){},s}(),ba={length:0,start:function(){return 0},end:function(){return 0}},Fe=function(){function s(){}return s.isBuffered=function(r,e){try{if(r){for(var t=s.getBuffered(r),n=0;n<t.length;n++)if(e>=t.start(n)&&e<=t.end(n))return!0}}catch(i){}return!1},s.bufferInfo=function(r,e,t){try{if(r){var n=s.getBuffered(r),i=[],o;for(o=0;o<n.length;o++)i.push({start:n.start(o),end:n.end(o)});return this.bufferedInfo(i,e,t)}}catch(l){}return{len:0,start:e,end:e,nextStart:void 0}},s.bufferedInfo=function(r,e,t){e=Math.max(0,e),r.sort(function(E,C){var w=E.start-C.start;return w||C.end-E.end});var n=[];if(t)for(var i=0;i<r.length;i++){var o=n.length;if(o){var l=n[o-1].end;r[i].start-l<t?r[i].end>l&&(n[o-1].end=r[i].end):n.push(r[i])}else n.push(r[i])}else n=r;for(var d=0,u,c=e,p=e,g=0;g<n.length;g++){var y=n[g].start,b=n[g].end;if(e+t>=y&&e<b)c=y,p=b,d=p-e;else if(e+t<y){u=y;break}}return{len:d,start:c||0,end:p||0,nextStart:u}},s.getBuffered=function(r){try{return r.buffered}catch(e){return P.log("failed to get media.buffered",e),ba}},s}(),Dr=function(a,r,e,t,n,i){t===void 0&&(t=0),n===void 0&&(n=-1),i===void 0&&(i=!1),this.level=void 0,this.sn=void 0,this.part=void 0,this.id=void 0,this.size=void 0,this.partial=void 0,this.transmuxing=At(),this.buffering={audio:At(),video:At(),audiovideo:At()},this.level=a,this.sn=r,this.id=e,this.size=t,this.part=n,this.partial=i};function At(){return{start:0,executeStart:0,executeEnd:0,end:0}}function Ai(s,a){for(var r=null,e=0,t=s.length;e<t;e++){var n=s[e];if(n&&n.cc===a){r=n;break}}return r}function Ea(s,a,r){return!!(a.details&&(r.endCC>r.startCC||s&&s.cc<r.startCC))}function xa(s,a,r){var e=s.fragments,t=a.fragments;if(!t.length||!e.length){P.log("No fragments to align");return}var n=Ai(e,t[0].cc);if(!n||n&&!n.startPTS){P.log("No frag in previous level to align on");return}return n}function zt(s,a){if(s){var r=s.start+a;s.start=s.startPTS=r,s.endPTS=r+s.duration}}function _r(s,a){for(var r=a.fragments,e=0,t=r.length;e<t;e++)zt(r[e],s);a.fragmentHint&&zt(a.fragmentHint,s),a.alignedSliding=!0}function Ta(s,a,r){!a||(Sa(s,r,a),!r.alignedSliding&&a.details&&bn(r,a.details),!r.alignedSliding&&a.details&&!r.skippedSegments&&ci(a.details,r))}function Sa(s,a,r){if(Ea(s,r,a)){var e=xa(r.details,a);e&&oe(e.start)&&(P.log("Adjusting PTS using last level due to CC increase within current level "+a.url),_r(e.start,a))}}function bn(s,a){if(!(!a.fragments.length||!s.hasProgramDateTime||!a.hasProgramDateTime)){var r=a.fragments[0].programDateTime,e=s.fragments[0].programDateTime,t=(e-r)/1e3+a.fragments[0].start;t&&oe(t)&&(P.log("Adjusting PTS using programDateTime delta "+(e-r)+"ms, sliding:"+t.toFixed(3)+" "+s.url+" "),_r(t,s))}}function bi(s,a){if(!(!s.hasProgramDateTime||!a.hasProgramDateTime)){var r=s.fragments,e=a.fragments;if(!(!r.length||!e.length)){var t=Math.round(e.length/2)-1,n=e[t],i=Ai(r,n.cc)||r[Math.round(r.length/2)-1],o=n.programDateTime,l=i.programDateTime;if(!(o===null||l===null)){var d=(l-o)/1e3-(i.start-n.start);_r(d,s)}}}}var Ca=function(){function s(r,e){this.subtle=void 0,this.aesIV=void 0,this.subtle=r,this.aesIV=e}var a=s.prototype;return a.decrypt=function(e,t){return this.subtle.decrypt({name:"AES-CBC",iv:this.aesIV},t,e)},s}(),ka=function(){function s(r,e){this.subtle=void 0,this.key=void 0,this.subtle=r,this.key=e}var a=s.prototype;return a.expandKey=function(){return this.subtle.importKey("raw",this.key,{name:"AES-CBC"},!1,["encrypt","decrypt"])},s}();function En(s){var a=s.byteLength,r=a&&new DataView(s.buffer).getUint8(a-1);return r?mt(s,0,a-r):s}var wa=function(){function s(){this.rcon=[0,1,2,4,8,16,32,64,128,27,54],this.subMix=[new Uint32Array(256),new Uint32Array(256),new Uint32Array(256),new Uint32Array(256)],this.invSubMix=[new Uint32Array(256),new Uint32Array(256),new Uint32Array(256),new Uint32Array(256)],this.sBox=new Uint32Array(256),this.invSBox=new Uint32Array(256),this.key=new Uint32Array(0),this.ksRows=0,this.keySize=0,this.keySchedule=void 0,this.invKeySchedule=void 0,this.initTable()}var a=s.prototype;return a.uint8ArrayToUint32Array_=function(e){for(var t=new DataView(e),n=new Uint32Array(4),i=0;i<4;i++)n[i]=t.getUint32(i*4);return n},a.initTable=function(){var e=this.sBox,t=this.invSBox,n=this.subMix,i=n[0],o=n[1],l=n[2],d=n[3],u=this.invSubMix,c=u[0],p=u[1],g=u[2],y=u[3],b=new Uint32Array(256),E=0,C=0,w=0;for(w=0;w<256;w++)w<128?b[w]=w<<1:b[w]=w<<1^283;for(w=0;w<256;w++){var k=C^C<<1^C<<2^C<<3^C<<4;k=k>>>8^k&255^99,e[E]=k,t[k]=E;var D=b[E],_=b[D],F=b[_],O=b[k]*257^k*16843008;i[E]=O<<24|O>>>8,o[E]=O<<16|O>>>16,l[E]=O<<8|O>>>24,d[E]=O,O=F*16843009^_*65537^D*257^E*16843008,c[k]=O<<24|O>>>8,p[k]=O<<16|O>>>16,g[k]=O<<8|O>>>24,y[k]=O,E?(E=D^b[b[b[F^D]]],C^=b[b[C]]):E=C=1}},a.expandKey=function(e){for(var t=this.uint8ArrayToUint32Array_(e),n=!0,i=0;i<t.length&&n;)n=t[i]===this.key[i],i++;if(!n){this.key=t;var o=this.keySize=t.length;if(o!==4&&o!==6&&o!==8)throw new Error("Invalid aes key size="+o);var l=this.ksRows=(o+6+1)*4,d,u,c=this.keySchedule=new Uint32Array(l),p=this.invKeySchedule=new Uint32Array(l),g=this.sBox,y=this.rcon,b=this.invSubMix,E=b[0],C=b[1],w=b[2],k=b[3],D,_;for(d=0;d<l;d++){if(d<o){D=c[d]=t[d];continue}_=D,d%o==0?(_=_<<8|_>>>24,_=g[_>>>24]<<24|g[_>>>16&255]<<16|g[_>>>8&255]<<8|g[_&255],_^=y[d/o|0]<<24):o>6&&d%o==4&&(_=g[_>>>24]<<24|g[_>>>16&255]<<16|g[_>>>8&255]<<8|g[_&255]),c[d]=D=(c[d-o]^_)>>>0}for(u=0;u<l;u++)d=l-u,u&3?_=c[d]:_=c[d-4],u<4||d<=4?p[u]=_:p[u]=E[g[_>>>24]]^C[g[_>>>16&255]]^w[g[_>>>8&255]]^k[g[_&255]],p[u]=p[u]>>>0}},a.networkToHostOrderSwap=function(e){return e<<24|(e&65280)<<8|(e&16711680)>>8|e>>>24},a.decrypt=function(e,t,n){for(var i=this.keySize+6,o=this.invKeySchedule,l=this.invSBox,d=this.invSubMix,u=d[0],c=d[1],p=d[2],g=d[3],y=this.uint8ArrayToUint32Array_(n),b=y[0],E=y[1],C=y[2],w=y[3],k=new Int32Array(e),D=new Int32Array(k.length),_,F,O,M,z,H,G,te,ye,Ae,Ee,we,xe,_e,Re=this.networkToHostOrderSwap;t<k.length;){for(ye=Re(k[t]),Ae=Re(k[t+1]),Ee=Re(k[t+2]),we=Re(k[t+3]),z=ye^o[0],H=we^o[1],G=Ee^o[2],te=Ae^o[3],xe=4,_e=1;_e<i;_e++)_=u[z>>>24]^c[H>>16&255]^p[G>>8&255]^g[te&255]^o[xe],F=u[H>>>24]^c[G>>16&255]^p[te>>8&255]^g[z&255]^o[xe+1],O=u[G>>>24]^c[te>>16&255]^p[z>>8&255]^g[H&255]^o[xe+2],M=u[te>>>24]^c[z>>16&255]^p[H>>8&255]^g[G&255]^o[xe+3],z=_,H=F,G=O,te=M,xe=xe+4;_=l[z>>>24]<<24^l[H>>16&255]<<16^l[G>>8&255]<<8^l[te&255]^o[xe],F=l[H>>>24]<<24^l[G>>16&255]<<16^l[te>>8&255]<<8^l[z&255]^o[xe+1],O=l[G>>>24]<<24^l[te>>16&255]<<16^l[z>>8&255]<<8^l[H&255]^o[xe+2],M=l[te>>>24]<<24^l[z>>16&255]<<16^l[H>>8&255]<<8^l[G&255]^o[xe+3],D[t]=Re(_^b),D[t+1]=Re(M^E),D[t+2]=Re(O^C),D[t+3]=Re(F^w),b=ye,E=Ae,C=Ee,w=we,t=t+4}return D.buffer},s}(),La=16,Pr=function(){function s(r,e){var t=e===void 0?{}:e,n=t.removePKCS7Padding,i=n===void 0?!0:n;if(this.logEnabled=!0,this.removePKCS7Padding=void 0,this.subtle=null,this.softwareDecrypter=null,this.key=null,this.fastAesKey=null,this.remainderData=null,this.currentIV=null,this.currentResult=null,this.useSoftware=void 0,this.useSoftware=r.enableSoftwareAES,this.removePKCS7Padding=i,i)try{var o=self.crypto;o&&(this.subtle=o.subtle||o.webkitSubtle)}catch(l){}this.subtle===null&&(this.useSoftware=!0)}var a=s.prototype;return a.destroy=function(){this.subtle=null,this.softwareDecrypter=null,this.key=null,this.fastAesKey=null,this.remainderData=null,this.currentIV=null,this.currentResult=null},a.isSync=function(){return this.useSoftware},a.flush=function(){var e=this.currentResult,t=this.remainderData;if(!e||t)return this.reset(),null;var n=new Uint8Array(e);return this.reset(),this.removePKCS7Padding?En(n):n},a.reset=function(){this.currentResult=null,this.currentIV=null,this.remainderData=null,this.softwareDecrypter&&(this.softwareDecrypter=null)},a.decrypt=function(e,t,n){var i=this;return this.useSoftware?new Promise(function(o,l){i.softwareDecrypt(new Uint8Array(e),t,n);var d=i.flush();d?o(d.buffer):l(new Error("[softwareDecrypt] Failed to decrypt data"))}):this.webCryptoDecrypt(new Uint8Array(e),t,n)},a.softwareDecrypt=function(e,t,n){var i=this.currentIV,o=this.currentResult,l=this.remainderData;this.logOnce("JS AES decrypt"),l&&(e=kt(l,e),this.remainderData=null);var d=this.getValidChunk(e);if(!d.length)return null;i&&(n=i);var u=this.softwareDecrypter;u||(u=this.softwareDecrypter=new wa),u.expandKey(t);var c=o;return this.currentResult=u.decrypt(d.buffer,0,n),this.currentIV=mt(d,-16).buffer,c||null},a.webCryptoDecrypt=function(e,t,n){var i=this,o=this.subtle;return(this.key!==t||!this.fastAesKey)&&(this.key=t,this.fastAesKey=new ka(o,t)),this.fastAesKey.expandKey().then(function(l){if(!o)return Promise.reject(new Error("web crypto not initialized"));i.logOnce("WebCrypto AES decrypt");var d=new Ca(o,new Uint8Array(n));return d.decrypt(e.buffer,l)}).catch(function(l){return P.warn("[decrypter]: WebCrypto Error, disable WebCrypto API, "+l.name+": "+l.message),i.onWebCryptoError(e,t,n)})},a.onWebCryptoError=function(e,t,n){this.useSoftware=!0,this.logEnabled=!0,this.softwareDecrypt(e,t,n);var i=this.flush();if(i)return i.buffer;throw new Error("WebCrypto and softwareDecrypt: failed to decrypt data")},a.getValidChunk=function(e){var t=e,n=e.length-e.length%La;return n!==e.length&&(t=mt(e,0,n),this.remainderData=mt(e,n)),t},a.logOnce=function(e){!this.logEnabled||(P.log("[decrypter]: "+e),this.logEnabled=!1)},s}(),xn={toString:function(a){for(var r="",e=a.length,t=0;t<e;t++)r+="["+a.start(t).toFixed(3)+"-"+a.end(t).toFixed(3)+"]";return r}},Y={STOPPED:"STOPPED",IDLE:"IDLE",KEY_LOADING:"KEY_LOADING",FRAG_LOADING:"FRAG_LOADING",FRAG_LOADING_WAITING_RETRY:"FRAG_LOADING_WAITING_RETRY",WAITING_TRACK:"WAITING_TRACK",PARSING:"PARSING",PARSED:"PARSED",ENDED:"ENDED",ERROR:"ERROR",WAITING_INIT_PTS:"WAITING_INIT_PTS",WAITING_LEVEL:"WAITING_LEVEL"},Br=function(s){j(a,s);function a(e,t,n,i,o){var l;return l=s.call(this)||this,l.hls=void 0,l.fragPrevious=null,l.fragCurrent=null,l.fragmentTracker=void 0,l.transmuxer=null,l._state=Y.STOPPED,l.playlistType=void 0,l.media=null,l.mediaBuffer=null,l.config=void 0,l.bitrateTest=!1,l.lastCurrentTime=0,l.nextLoadPosition=0,l.startPosition=0,l.startTimeOffset=null,l.loadedmetadata=!1,l.retryDate=0,l.levels=null,l.fragmentLoader=void 0,l.keyLoader=void 0,l.levelLastLoaded=null,l.startFragRequested=!1,l.decrypter=void 0,l.initPTS=[],l.onvseeking=null,l.onvended=null,l.logPrefix="",l.log=void 0,l.warn=void 0,l.playlistType=o,l.logPrefix=i,l.log=P.log.bind(P,i+":"),l.warn=P.warn.bind(P,i+":"),l.hls=e,l.fragmentLoader=new ya(e.config),l.keyLoader=n,l.fragmentTracker=t,l.config=e.config,l.decrypter=new Pr(e.config),e.on(S.MANIFEST_LOADED,l.onManifestLoaded,se(l)),l}var r=a.prototype;return r.doTick=function(){this.onTickEnd()},r.onTickEnd=function(){},r.startLoad=function(t){},r.stopLoad=function(){this.fragmentLoader.abort(),this.keyLoader.abort();var t=this.fragCurrent;t!=null&&t.loader&&(t.abortRequests(),this.fragmentTracker.removeFragment(t)),this.resetTransmuxer(),this.fragCurrent=null,this.fragPrevious=null,this.clearInterval(),this.clearNextTick(),this.state=Y.STOPPED},r._streamEnded=function(t,n){if(n.live||t.nextStart||!t.end||!this.media)return!1;var i=n.partList;if(i!=null&&i.length){var o=i[i.length-1],l=Fe.isBuffered(this.media,o.start+o.duration/2);return l}var d=n.fragments[n.fragments.length-1].type;return this.fragmentTracker.isEndListAppended(d)},r.getLevelDetails=function(){if(this.levels&&this.levelLastLoaded!==null){var t;return(t=this.levels[this.levelLastLoaded])==null?void 0:t.details}},r.onMediaAttached=function(t,n){var i=this.media=this.mediaBuffer=n.media;this.onvseeking=this.onMediaSeeking.bind(this),this.onvended=this.onMediaEnded.bind(this),i.addEventListener("seeking",this.onvseeking),i.addEventListener("ended",this.onvended);var o=this.config;this.levels&&o.autoStartLoad&&this.state===Y.STOPPED&&this.startLoad(o.startPosition)},r.onMediaDetaching=function(){var t=this.media;t!=null&&t.ended&&(this.log("MSE detaching and video ended, reset startPosition"),this.startPosition=this.lastCurrentTime=0),t&&this.onvseeking&&this.onvended&&(t.removeEventListener("seeking",this.onvseeking),t.removeEventListener("ended",this.onvended),this.onvseeking=this.onvended=null),this.keyLoader&&this.keyLoader.detach(),this.media=this.mediaBuffer=null,this.loadedmetadata=!1,this.fragmentTracker.removeAllFragments(),this.stopLoad()},r.onMediaSeeking=function(){var t=this.config,n=this.fragCurrent,i=this.media,o=this.mediaBuffer,l=this.state,d=i?i.currentTime:0,u=Fe.bufferInfo(o||i,d,t.maxBufferHole);if(this.log("media seeking to "+(oe(d)?d.toFixed(3):d)+", state: "+l),this.state===Y.ENDED)this.resetLoadingState();else if(n){var c=t.maxFragLookUpTolerance,p=n.start-c,g=n.start+n.duration+c;if(!u.len||g<u.start||p>u.end){var y=d>g;(d<p||y)&&(y&&n.loader&&(this.log("seeking outside of buffer while fragment load in progress, cancel fragment load"),n.abortRequests()),this.resetLoadingState())}}i&&(this.fragmentTracker.removeFragmentsInRange(d,Infinity,this.playlistType,!0),this.lastCurrentTime=d),!this.loadedmetadata&&!u.len&&(this.nextLoadPosition=this.startPosition=d),this.tickImmediate()},r.onMediaEnded=function(){this.startPosition=this.lastCurrentTime=0},r.onManifestLoaded=function(t,n){this.startTimeOffset=n.startTimeOffset,this.initPTS=[]},r.onHandlerDestroying=function(){this.stopLoad(),s.prototype.onHandlerDestroying.call(this)},r.onHandlerDestroyed=function(){this.state=Y.STOPPED,this.fragmentLoader&&this.fragmentLoader.destroy(),this.keyLoader&&this.keyLoader.destroy(),this.decrypter&&this.decrypter.destroy(),this.hls=this.log=this.warn=this.decrypter=this.keyLoader=this.fragmentLoader=this.fragmentTracker=null,s.prototype.onHandlerDestroyed.call(this)},r.loadFragment=function(t,n,i){this._loadFragForPlayback(t,n,i)},r._loadFragForPlayback=function(t,n,i){var o=this,l=function(u){if(o.fragContextChanged(t)){o.warn("Fragment "+t.sn+(u.part?" p: "+u.part.index:"")+" of level "+t.level+" was dropped during download."),o.fragmentTracker.removeFragment(t);return}t.stats.chunkCount++,o._handleFragmentLoadProgress(u)};this._doFragLoad(t,n,i,l).then(function(d){if(!!d){var u=o.state;if(o.fragContextChanged(t)){(u===Y.FRAG_LOADING||!o.fragCurrent&&u===Y.PARSING)&&(o.fragmentTracker.removeFragment(t),o.state=Y.IDLE);return}"payload"in d&&(o.log("Loaded fragment "+t.sn+" of level "+t.level),o.hls.trigger(S.FRAG_LOADED,d)),o._handleFragmentLoadComplete(d)}}).catch(function(d){o.state===Y.STOPPED||o.state===Y.ERROR||(o.warn(d),o.resetFragmentLoading(t))})},r.clearTrackerIfNeeded=function(t){var n,i=this.fragmentTracker.getState(t);if(i===Ye.APPENDING){var o=t.type,l=this.getFwdBufferInfo(this.mediaBuffer,o),d=Math.max(t.duration,l?l.len:this.config.maxBufferLength);this.reduceMaxBufferLength(d)&&this.fragmentTracker.removeFragment(t)}else((n=this.mediaBuffer)==null?void 0:n.buffered.length)===0&&this.fragmentTracker.removeAllFragments()},r.flushMainBuffer=function(t,n,i){if(i===void 0&&(i=null),!!(t-n)){var o={startOffset:t,endOffset:n,type:i};this.hls.trigger(S.BUFFER_FLUSHING,o)}},r._loadInitSegment=function(t,n){var i=this;this._doFragLoad(t,n).then(function(o){if(!o||i.fragContextChanged(t)||!i.levels)throw new Error("init load aborted");return o}).then(function(o){var l=i.hls,d=o.payload,u=t.decryptdata;if(d&&d.byteLength>0&&u&&u.key&&u.iv&&u.method==="AES-128"){var c=self.performance.now();return i.decrypter.decrypt(new Uint8Array(d),u.key.buffer,u.iv.buffer).catch(function(p){throw l.trigger(S.ERROR,{type:fe.MEDIA_ERROR,details:U.FRAG_DECRYPT_ERROR,fatal:!1,error:p,reason:p.message,frag:t}),p}).then(function(p){var g=self.performance.now();return l.trigger(S.FRAG_DECRYPTED,{frag:t,payload:p,stats:{tstart:c,tdecrypt:g}}),o.payload=p,o})}return o}).then(function(o){var l=i.fragCurrent,d=i.hls,u=i.levels;if(!u)throw new Error("init load aborted, missing levels");var c=t.stats;i.state=Y.IDLE,n.fragmentError=0,t.data=new Uint8Array(o.payload),c.parsing.start=c.buffering.start=self.performance.now(),c.parsing.end=c.buffering.end=self.performance.now(),o.frag===l&&d.trigger(S.FRAG_BUFFERED,{stats:c,frag:l,part:null,id:t.type}),i.tick()}).catch(function(o){i.state===Y.STOPPED||i.state===Y.ERROR||(i.warn(o),i.resetFragmentLoading(t))})},r.fragContextChanged=function(t){var n=this.fragCurrent;return!t||!n||t.level!==n.level||t.sn!==n.sn||t.urlId!==n.urlId},r.fragBufferedComplete=function(t,n){var i,o,l,d,u=this.mediaBuffer?this.mediaBuffer:this.media;this.log("Buffered "+t.type+" sn: "+t.sn+(n?" part: "+n.index:"")+" of "+(this.logPrefix==="[stream-controller]"?"level":"track")+" "+t.level+" (frag:["+((i=t.startPTS)!=null?i:NaN).toFixed(3)+"-"+((o=t.endPTS)!=null?o:NaN).toFixed(3)+"] > buffer:"+(u?xn.toString(Fe.getBuffered(u)):"(detached)")+")"),this.state=Y.IDLE,!!u&&(!this.loadedmetadata&&t.type==he.MAIN&&u.buffered.length&&((l=this.fragCurrent)==null?void 0:l.sn)===((d=this.fragPrevious)==null?void 0:d.sn)&&(this.loadedmetadata=!0,this.seekToStartPos()),this.tick())},r.seekToStartPos=function(){},r._handleFragmentLoadComplete=function(t){var n=this.transmuxer;if(!!n){var i=t.frag,o=t.part,l=t.partsLoaded,d=!l||l.length===0||l.some(function(c){return!c}),u=new Dr(i.level,i.sn,i.stats.chunkCount+1,0,o?o.index:-1,!d);n.flush(u)}},r._handleFragmentLoadProgress=function(t){},r._doFragLoad=function(t,n,i,o){var l,d=this;i===void 0&&(i=null);var u=n==null?void 0:n.details;if(!this.levels||!u)throw new Error("frag load aborted, missing level"+(u?"":" detail")+"s");var c=null;if(t.encrypted&&!((l=t.decryptdata)!=null&&l.key)?(this.log("Loading key for "+t.sn+" of ["+u.startSN+"-"+u.endSN+"], "+(this.logPrefix==="[stream-controller]"?"level":"track")+" "+t.level),this.state=Y.KEY_LOADING,this.fragCurrent=t,c=this.keyLoader.load(t).then(function(w){if(!d.fragContextChanged(w.frag))return d.hls.trigger(S.KEY_LOADED,w),d.state===Y.KEY_LOADING&&(d.state=Y.IDLE),w}),this.hls.trigger(S.KEY_LOADING,{frag:t}),this.fragCurrent===null&&(c=Promise.reject(new Error("frag load aborted, context changed in KEY_LOADING")))):!t.encrypted&&u.encryptedFragments.length&&this.keyLoader.loadClear(t,u.encryptedFragments),i=Math.max(t.start,i||0),this.config.lowLatencyMode){var p=u.partList;if(p&&o){i>t.end&&u.fragmentHint&&(t=u.fragmentHint);var g=this.getNextPart(p,t,i);if(g>-1){var y=p[g];this.log("Loading part sn: "+t.sn+" p: "+y.index+" cc: "+t.cc+" of playlist ["+u.startSN+"-"+u.endSN+"] parts [0-"+g+"-"+(p.length-1)+"] "+(this.logPrefix==="[stream-controller]"?"level":"track")+": "+t.level+", target: "+parseFloat(i.toFixed(3))),this.nextLoadPosition=y.start+y.duration,this.state=Y.FRAG_LOADING;var b;return c?b=c.then(function(w){return!w||d.fragContextChanged(w.frag)?null:d.doFragPartsLoad(t,y,n,o)}).catch(function(w){return d.handleFragLoadError(w)}):b=this.doFragPartsLoad(t,y,n,o).catch(function(w){return d.handleFragLoadError(w)}),this.hls.trigger(S.FRAG_LOADING,{frag:t,part:y,targetBufferTime:i}),this.fragCurrent===null?Promise.reject(new Error("frag load aborted, context changed in FRAG_LOADING parts")):b}else if(!t.url||this.loadedEndOfParts(p,i))return Promise.resolve(null)}}this.log("Loading fragment "+t.sn+" cc: "+t.cc+" "+(u?"of ["+u.startSN+"-"+u.endSN+"] ":"")+(this.logPrefix==="[stream-controller]"?"level":"track")+": "+t.level+", target: "+parseFloat(i.toFixed(3))),oe(t.sn)&&!this.bitrateTest&&(this.nextLoadPosition=t.start+t.duration),this.state=Y.FRAG_LOADING;var E=this.config.progressive,C;return E&&c?C=c.then(function(w){return!w||d.fragContextChanged(w==null?void 0:w.frag)?null:d.fragmentLoader.load(t,o)}).catch(function(w){return d.handleFragLoadError(w)}):C=Promise.all([this.fragmentLoader.load(t,E?o:void 0),c]).then(function(w){var k=w[0];return!E&&k&&o&&o(k),k}).catch(function(w){return d.handleFragLoadError(w)}),this.hls.trigger(S.FRAG_LOADING,{frag:t,targetBufferTime:i}),this.fragCurrent===null?Promise.reject(new Error("frag load aborted, context changed in FRAG_LOADING")):C},r.doFragPartsLoad=function(t,n,i,o){var l=this;return new Promise(function(d,u){var c,p=[],g=(c=i.details)==null?void 0:c.partList,y=function b(E){l.fragmentLoader.loadPart(t,E,o).then(function(C){p[E.index]=C;var w=C.part;l.hls.trigger(S.FRAG_LOADED,C);var k=fi(i,t.sn,E.index+1)||hi(g,t.sn,E.index+1);if(k)b(k);else return d({frag:t,part:w,partsLoaded:p})}).catch(u)};y(n)})},r.handleFragLoadError=function(t){if("data"in t){var n=t.data;t.data&&n.details===U.INTERNAL_ABORTED?this.handleFragLoadAborted(n.frag,n.part):this.hls.trigger(S.ERROR,n)}else this.hls.trigger(S.ERROR,{type:fe.OTHER_ERROR,details:U.INTERNAL_EXCEPTION,err:t,error:t,fatal:!0});return null},r._handleTransmuxerFlush=function(t){var n=this.getCurrentContext(t);if(!n||this.state!==Y.PARSING){!this.fragCurrent&&this.state!==Y.STOPPED&&this.state!==Y.ERROR&&(this.state=Y.IDLE);return}var i=n.frag,o=n.part,l=n.level,d=self.performance.now();i.stats.parsing.end=d,o&&(o.stats.parsing.end=d),this.updateLevelTiming(i,o,l,t.partial)},r.getCurrentContext=function(t){var n=this.levels,i=this.fragCurrent,o=t.level,l=t.sn,d=t.part;if(!(n!=null&&n[o]))return this.warn("Levels object was unset while buffering fragment "+l+" of level "+o+". The current chunk will not be buffered."),null;var u=n[o],c=d>-1?fi(u,l,d):null,p=c?c.fragment:la(u,l,i);return p?(i&&i!==p&&(p.stats=i.stats),{frag:p,part:c,level:u}):null},r.bufferFragmentData=function(t,n,i,o){var l;if(!(!t||this.state!==Y.PARSING)){var d=t.data1,u=t.data2,c=d;if(d&&u&&(c=kt(d,u)),!!((l=c)!=null&&l.length)){var p={type:t.type,frag:n,part:i,chunkMeta:o,parent:n.type,data:c};this.hls.trigger(S.BUFFER_APPENDING,p),t.dropped&&t.independent&&!i&&this.flushBufferGap(n)}}},r.flushBufferGap=function(t){var n=this.media;if(!!n){if(!Fe.isBuffered(n,n.currentTime)){this.flushMainBuffer(0,t.start);return}var i=n.currentTime,o=Fe.bufferInfo(n,i,0),l=t.duration,d=Math.min(this.config.maxFragLookUpTolerance*2,l*.25),u=Math.max(Math.min(t.start-d,o.end-d),i+d);t.start-u>d&&this.flushMainBuffer(u,t.start)}},r.getFwdBufferInfo=function(t,n){var i=this.getLoadPosition();return oe(i)?this.getFwdBufferInfoAtPos(t,i,n):null},r.getFwdBufferInfoAtPos=function(t,n,i){var o=this.config.maxBufferHole,l=Fe.bufferInfo(t,n,o);if(l.len===0&&l.nextStart!==void 0){var d=this.fragmentTracker.getBufferedFrag(n,i);if(d&&l.nextStart<d.end)return Fe.bufferInfo(t,n,Math.max(l.nextStart,o))}return l},r.getMaxBufferLength=function(t){var n=this.config,i;return t?i=Math.max(8*n.maxBufferSize/t,n.maxBufferLength):i=n.maxBufferLength,Math.min(i,n.maxMaxBufferLength)},r.reduceMaxBufferLength=function(t){var n=this.config,i=t||n.maxBufferLength;return n.maxMaxBufferLength>=i?(n.maxMaxBufferLength/=2,this.warn("Reduce max buffer length to "+n.maxMaxBufferLength+"s"),!0):!1},r.getNextFragment=function(t,n){var i=n.fragments,o=i.length;if(!o)return null;var l=this.config,d=i[0].start,u;if(n.live){var c=l.initialLiveManifestSize;if(o<c)return this.warn("Not enough fragments to start playback (have: "+o+", need: "+c+")"),null;!n.PTSKnown&&!this.startFragRequested&&this.startPosition===-1&&(u=this.getInitialLiveFragment(n,i),this.startPosition=u?this.hls.liveSyncPosition||u.start:t)}else t<=d&&(u=i[0]);if(!u){var p=l.lowLatencyMode?n.partEnd:n.fragmentEnd;u=this.getFragmentAtPosition(t,p,n)}return this.mapToInitFragWhenRequired(u)},r.isLoopLoading=function(t,n){var i=this.fragmentTracker.getState(t);return(i===Ye.OK||i===Ye.PARTIAL&&!!t.gap)&&this.nextLoadPosition>n},r.getNextFragmentLoopLoading=function(t,n,i,o,l){var d=t.gap,u=this.getNextFragment(this.nextLoadPosition,n);if(u===null)return u;if(t=u,d&&t&&!t.gap&&i.nextStart){var c=this.getFwdBufferInfoAtPos(this.mediaBuffer?this.mediaBuffer:this.media,i.nextStart,o);if(c!==null&&i.len+c.len>=l)return this.log('buffer full after gaps in "'+o+'" playlist starting at sn: '+t.sn),null}return t},r.mapToInitFragWhenRequired=function(t){return t!=null&&t.initSegment&&!(t!=null&&t.initSegment.data)&&!this.bitrateTest?t.initSegment:t},r.getNextPart=function(t,n,i){for(var o=-1,l=!1,d=!0,u=0,c=t.length;u<c;u++){var p=t[u];if(d=d&&!p.independent,o>-1&&i<p.start)break;var g=p.loaded;g?o=-1:(l||p.independent||d)&&p.fragment===n&&(o=u),l=g}return o},r.loadedEndOfParts=function(t,n){var i=t[t.length-1];return i&&n>i.start&&i.loaded},r.getInitialLiveFragment=function(t,n){var i=this.fragPrevious,o=null;if(i){if(t.hasProgramDateTime&&(this.log("Live playlist, switching playlist, load frag with same PDT: "+i.programDateTime),o=ua(n,i.endProgramDateTime,this.config.maxFragLookUpTolerance)),!o){var l=i.sn+1;if(l>=t.startSN&&l<=t.endSN){var d=n[l-t.startSN];i.cc===d.cc&&(o=d,this.log("Live playlist, switching playlist, load frag with next SN: "+o.sn))}o||(o=fa(n,i.cc),o&&this.log("Live playlist, switching playlist, load frag with same CC: "+o.sn))}}else{var u=this.hls.liveSyncPosition;u!==null&&(o=this.getFragmentAtPosition(u,this.bitrateTest?t.fragmentEnd:t.edge,t))}return o},r.getFragmentAtPosition=function(t,n,i){var o=this.config,l=this.fragPrevious,d=i.fragments,u=i.endSN,c=i.fragmentHint,p=o.maxFragLookUpTolerance,g=!!(o.lowLatencyMode&&i.partList&&c);g&&c&&!this.bitrateTest&&(d=d.concat(c),u=c.sn);var y;if(t<n){var b=t>n-p?0:p;y=gn(l,d,t,b)}else y=d[d.length-1];if(y){var E=y.sn-i.startSN,C=this.fragmentTracker.getState(y);if((C===Ye.OK||C===Ye.PARTIAL&&y.gap)&&(l=y),l&&y.sn===l.sn&&!g){var w=l&&y.level===l.level;if(w){var k=d[E+1];y.sn<u&&this.fragmentTracker.getState(k)!==Ye.OK?y=k:y=null}}}return y},r.synchronizeToLiveEdge=function(t){var n=this.config,i=this.media;if(!!i){var o=this.hls.liveSyncPosition,l=i.currentTime,d=t.fragments[0].start,u=t.edge,c=l>=d-n.maxFragLookUpTolerance&&l<=u;if(o!==null&&i.duration>o&&(l<o||!c)){var p=n.liveMaxLatencyDuration!==void 0?n.liveMaxLatencyDuration:n.liveMaxLatencyDurationCount*t.targetduration;(!c&&i.readyState<4||l<u-p)&&(this.loadedmetadata||(this.nextLoadPosition=o),i.readyState&&(this.warn("Playback: "+l.toFixed(3)+" is located too far from the end of live sliding playlist: "+u+", reset currentTime to : "+o.toFixed(3)),i.currentTime=o))}}},r.alignPlaylists=function(t,n){var i=this.levels,o=this.levelLastLoaded,l=this.fragPrevious,d=o!==null?i[o]:null,u=t.fragments.length;if(!u)return this.warn("No fragments in live playlist"),0;var c=t.fragments[0].start,p=!n,g=t.alignedSliding&&oe(c);if(p||!g&&!c){Ta(l,d,t);var y=t.fragments[0].start;return this.log("Live playlist sliding: "+y.toFixed(2)+" start-sn: "+(n?n.startSN:"na")+"->"+t.startSN+" prev-sn: "+(l?l.sn:"na")+" fragments: "+u),y}return c},r.waitForCdnTuneIn=function(t){var n=3;return t.live&&t.canBlockReload&&t.partTarget&&t.tuneInGoal>Math.max(t.partHoldBack,t.partTarget*n)},r.setStartPosition=function(t,n){var i=this.startPosition;if(i<n&&(i=-1),i===-1||this.lastCurrentTime===-1){var o=this.startTimeOffset!==null,l=o?this.startTimeOffset:t.startTimeOffset;l!==null&&oe(l)?(i=n+l,l<0&&(i+=t.totalduration),i=Math.min(Math.max(n,i),n+t.totalduration),this.log("Start time offset "+l+" found in "+(o?"multivariant":"media")+" playlist, adjust startPosition to "+i),this.startPosition=i):t.live?i=this.hls.liveSyncPosition||n:this.startPosition=i=0,this.lastCurrentTime=i}this.nextLoadPosition=i},r.getLoadPosition=function(){var t=this.media,n=0;return this.loadedmetadata&&t?n=t.currentTime:this.nextLoadPosition&&(n=this.nextLoadPosition),n},r.handleFragLoadAborted=function(t,n){this.transmuxer&&t.sn!=="initSegment"&&t.stats.aborted&&(this.warn("Fragment "+t.sn+(n?" part"+n.index:"")+" of level "+t.level+" was aborted"),this.resetFragmentLoading(t))},r.resetFragmentLoading=function(t){(!this.fragCurrent||!this.fragContextChanged(t)&&this.state!==Y.FRAG_LOADING_WAITING_RETRY)&&(this.state=Y.IDLE)},r.onFragmentOrKeyLoadError=function(t,n){if(n.chunkMeta&&!n.frag){var i=this.getCurrentContext(n.chunkMeta);i&&(n.frag=i.frag)}var o=n.frag;if(!(!o||o.type!==t||!this.levels)){if(this.fragContextChanged(o)){var l;this.warn("Frag load error must match current frag to retry "+o.url+" > "+((l=this.fragCurrent)==null?void 0:l.url));return}var d=n.details===U.FRAG_GAP;d&&this.fragmentTracker.fragBuffered(o,!0);var u=n.errorAction,c=u||{},p=c.action,g=c.retryCount,y=g===void 0?0:g,b=c.retryConfig;if(u&&p===ze.RetryRequest&&b){this.loadedmetadata||(this.startFragRequested=!1,this.nextLoadPosition=this.startPosition);var E=Lr(b,y);this.warn("Fragment "+o.sn+" of "+t+" "+o.level+" errored with "+n.details+", retrying loading "+(y+1)+"/"+b.maxNumRetry+" in "+E+"ms"),u.resolved=!0,this.retryDate=self.performance.now()+E,this.state=Y.FRAG_LOADING_WAITING_RETRY}else b&&u?(this.resetFragmentErrors(t),y<b.maxNumRetry?d||(u.resolved=!0):P.warn(n.details+" reached or exceeded max retry ("+y+")")):this.state=Y.ERROR;this.tickImmediate()}},r.reduceLengthAndFlushBuffer=function(t){if(this.state===Y.PARSING||this.state===Y.PARSED){var n=t.parent,i=this.getFwdBufferInfo(this.mediaBuffer,n),o=i&&i.len>.5;o&&this.reduceMaxBufferLength(i.len);var l=!o;return l&&this.warn("Buffer full error while media.currentTime is not buffered, flush "+n+" buffer"),t.frag&&(this.fragmentTracker.removeFragment(t.frag),this.nextLoadPosition=t.frag.start),this.resetLoadingState(),l}return!1},r.resetFragmentErrors=function(t){t===he.AUDIO&&(this.fragCurrent=null),this.loadedmetadata||(this.startFragRequested=!1),this.state!==Y.STOPPED&&(this.state=Y.IDLE)},r.afterBufferFlushed=function(t,n,i){if(!!t){var o=Fe.getBuffered(t);this.fragmentTracker.detectEvictedFragments(n,o,i),this.state===Y.ENDED&&this.resetLoadingState()}},r.resetLoadingState=function(){this.log("Reset loading state"),this.fragCurrent=null,this.fragPrevious=null,this.state=Y.IDLE},r.resetStartWhenNotLoaded=function(t){if(!this.loadedmetadata){this.startFragRequested=!1;var n=this.levels?this.levels[t].details:null;n!=null&&n.live?(this.startPosition=-1,this.setStartPosition(n,0),this.resetLoadingState()):this.nextLoadPosition=this.startPosition}},r.resetWhenMissingContext=function(t){this.warn("The loading context changed while buffering fragment "+t.sn+" of level "+t.level+". This chunk will not be buffered."),this.removeUnbufferedFrags(),this.resetStartWhenNotLoaded(t.level),this.resetLoadingState()},r.removeUnbufferedFrags=function(t){t===void 0&&(t=0),this.fragmentTracker.removeFragmentsInRange(t,Infinity,this.playlistType,!1,!0)},r.updateLevelTiming=function(t,n,i,o){var l=this,d,u=i.details;if(!u){this.warn("level.details undefined");return}var c=Object.keys(t.elementaryStreams).reduce(function(g,y){var b=t.elementaryStreams[y];if(b){var E=b.endPTS-b.startPTS;if(E<=0)return l.warn("Could not parse fragment "+t.sn+" "+y+" duration reliably ("+E+")"),g||!1;var C=o?0:ui(u,t,b.startPTS,b.endPTS,b.startDTS,b.endDTS);return l.hls.trigger(S.LEVEL_PTS_UPDATED,{details:u,level:i,drift:C,type:y,frag:t,start:b.startPTS,end:b.endPTS}),!0}return g},!1);if(c)i.fragmentError=0;else if(((d=this.transmuxer)==null?void 0:d.error)===null){var p=new Error("Found no media in fragment "+t.sn+" of level "+i.id+" resetting transmuxer to fallback to playlist timing");if(this.warn(p.message),this.hls.trigger(S.ERROR,{type:fe.MEDIA_ERROR,details:U.FRAG_PARSING_ERROR,fatal:!1,error:p,frag:t,reason:"Found no media in msn "+t.sn+' of level "'+i.url+'"'}),!this.hls)return;this.resetTransmuxer()}this.state=Y.PARSED,this.hls.trigger(S.FRAG_PARSED,{frag:t,part:n})},r.resetTransmuxer=function(){this.transmuxer&&(this.transmuxer.destroy(),this.transmuxer=null)},r.recoverWorkerError=function(t){t.event==="demuxerWorker"&&(this.resetTransmuxer(),this.resetLoadingState())},Q(a,[{key:"state",get:function(){return this._state},set:function(t){var n=this._state;n!==t&&(this._state=t,this.log(n+"->"+t))}}]),a}(An);function It(){if(typeof self!="undefined")return self.MediaSource||self.WebKitMediaSource}function Ei(){return self.SourceBuffer||self.WebKitSourceBuffer}function Ia(){var s=It();if(!s)return!1;var a=Ei(),r=s&&typeof s.isTypeSupported=="function"&&s.isTypeSupported('video/mp4; codecs="avc1.42E01E,mp4a.40.2"'),e=!a||a.prototype&&typeof a.prototype.appendBuffer=="function"&&typeof a.prototype.remove=="function";return!!r&&!!e}function Ra(){var s,a=Ei();return typeof(a==null||(s=a.prototype)==null?void 0:s.changeType)=="function"}function Ve(s,a){return s===void 0&&(s=""),a===void 0&&(a=9e4),{type:s,id:-1,pid:-1,inputTimeScale:a,sequenceNumber:-1,samples:[],dropped:0}}var xi=function(){function s(){this._audioTrack=void 0,this._id3Track=void 0,this.frameIndex=0,this.cachedData=null,this.basePTS=null,this.initPTS=null,this.lastPTS=null}var a=s.prototype;return a.resetInitSegment=function(e,t,n,i){this._id3Track={type:"id3",id:3,pid:-1,inputTimeScale:9e4,sequenceNumber:0,samples:[],dropped:0}},a.resetTimeStamp=function(e){this.initPTS=e,this.resetContiguity()},a.resetContiguity=function(){this.basePTS=null,this.lastPTS=null,this.frameIndex=0},a.canParse=function(e,t){return!1},a.appendFrame=function(e,t,n){},a.demux=function(e,t){this.cachedData&&(e=kt(this.cachedData,e),this.cachedData=null);var n=Mn(e,0),i=n?n.length:0,o,l=this._audioTrack,d=this._id3Track,u=n?Vr(n):void 0,c=e.length;for((this.basePTS===null||this.frameIndex===0&&oe(u))&&(this.basePTS=bt(u,t,this.initPTS),this.lastPTS=this.basePTS),this.lastPTS===null&&(this.lastPTS=this.basePTS),n&&n.length>0&&d.samples.push({pts:this.lastPTS,dts:this.lastPTS,data:n,type:it.audioId3,duration:Number.POSITIVE_INFINITY});i<c;){if(this.canParse(e,i)){var p=this.appendFrame(l,e,i);p?(this.frameIndex++,this.lastPTS=p.sample.pts,i+=p.length,o=i):i=c}else qi(e,i)?(n=Mn(e,i),d.samples.push({pts:this.lastPTS,dts:this.lastPTS,data:n,type:it.audioId3,duration:Number.POSITIVE_INFINITY}),i+=n.length,o=i):i++;if(i===c&&o!==c){var g=mt(e,o);this.cachedData?this.cachedData=kt(this.cachedData,g):this.cachedData=g}}return{audioTrack:l,videoTrack:Ve(),id3Track:d,textTrack:Ve()}},a.demuxSampleAes=function(e,t,n){return Promise.reject(new Error("["+this+"] This demuxer does not support Sample-AES decryption"))},a.flush=function(e){var t=this.cachedData;return t&&(this.cachedData=null,this.demux(t,0)),{audioTrack:this._audioTrack,videoTrack:Ve(),id3Track:this._id3Track,textTrack:Ve()}},a.destroy=function(){},s}(),bt=function(a,r,e){if(oe(a))return a*90;var t=e?e.baseTime*9e4/e.timescale:0;return r*9e4+t};function Ti(s,a,r,e){var t,n,i,o,l=navigator.userAgent.toLowerCase(),d=e,u=[96e3,88200,64e3,48e3,44100,32e3,24e3,22050,16e3,12e3,11025,8e3,7350];t=((a[r+2]&192)>>>6)+1;var c=(a[r+2]&60)>>>2;if(c>u.length-1){s.trigger(S.ERROR,{type:fe.MEDIA_ERROR,details:U.FRAG_PARSING_ERROR,fatal:!0,reason:"invalid ADTS sampling index:"+c});return}return i=(a[r+2]&1)<<2,i|=(a[r+3]&192)>>>6,P.log("manifest codec:"+e+", ADTS type:"+t+", samplingIndex:"+c),/firefox/i.test(l)?c>=6?(t=5,o=new Array(4),n=c-3):(t=2,o=new Array(2),n=c):l.indexOf("android")!==-1?(t=2,o=new Array(2),n=c):(t=5,o=new Array(4),e&&(e.indexOf("mp4a.40.29")!==-1||e.indexOf("mp4a.40.5")!==-1)||!e&&c>=6?n=c-3:((e&&e.indexOf("mp4a.40.2")!==-1&&(c>=6&&i===1||/vivaldi/i.test(l))||!e&&i===1)&&(t=2,o=new Array(2)),n=c)),o[0]=t<<3,o[0]|=(c&14)>>1,o[1]|=(c&1)<<7,o[1]|=i<<3,t===5&&(o[1]|=(n&14)>>1,o[2]=(n&1)<<7,o[2]|=2<<2,o[3]=0),{config:o,samplerate:u[c],channelCount:i,codec:"mp4a.40."+t,manifestCodec:d}}function Qn(s,a){return s[a]===255&&(s[a+1]&246)==240}function Si(s,a){return s[a+1]&1?7:9}function Or(s,a){return(s[a+3]&3)<<11|s[a+4]<<3|(s[a+5]&224)>>>5}function Tn(s,a){return a+5<s.length}function Xn(s,a){return a+1<s.length&&Qn(s,a)}function Da(s,a){return Tn(s,a)&&Qn(s,a)&&Or(s,a)<=s.length-a}function _a(s,a){if(Xn(s,a)){var r=Si(s,a);if(a+r>=s.length)return!1;var e=Or(s,a);if(e<=r)return!1;var t=a+e;return t===s.length||Xn(s,t)}return!1}function Vt(s,a,r,e,t){if(!s.samplerate){var n=Ti(a,r,e,t);if(!n)return;s.config=n.config,s.samplerate=n.samplerate,s.channelCount=n.channelCount,s.codec=n.codec,s.manifestCodec=n.manifestCodec,P.log("parsed codec:"+s.codec+", rate:"+n.samplerate+", channels:"+n.channelCount)}}function Ci(s){return 1024*9e4/s}function Pa(s,a){var r=Si(s,a);if(a+r<=s.length){var e=Or(s,a)-r;if(e>0)return{headerLength:r,frameLength:e}}}function ki(s,a,r,e,t){var n=Ci(s.samplerate),i=e+t*n,o=Pa(a,r),l;if(o){var d=o.frameLength,u=o.headerLength,c=u+d,p=Math.max(0,r+c-a.length);p?(l=new Uint8Array(c-u),l.set(a.subarray(r+u,a.length),0)):l=a.subarray(r+u,r+c);var g={unit:l,pts:i};return p||s.samples.push(g),{sample:g,length:c,missing:p}}var y=a.length-r;l=new Uint8Array(y),l.set(a.subarray(r,a.length),0);var b={unit:l,pts:i};return{sample:b,length:y,missing:-1}}var Sn=function(s){j(a,s);function a(e,t){var n;return n=s.call(this)||this,n.observer=void 0,n.config=void 0,n.observer=e,n.config=t,n}var r=a.prototype;return r.resetInitSegment=function(t,n,i,o){s.prototype.resetInitSegment.call(this,t,n,i,o),this._audioTrack={container:"audio/adts",type:"audio",id:2,pid:-1,sequenceNumber:0,segmentCodec:"aac",samples:[],manifestCodec:n,duration:o,inputTimeScale:9e4,dropped:0}},a.probe=function(t){if(!t)return!1;for(var n=Mn(t,0)||[],i=n.length,o=t.length;i<o;i++)if(_a(t,i))return P.log("ADTS sync word found !"),!0;return!1},r.canParse=function(t,n){return Da(t,n)},r.appendFrame=function(t,n,i){Vt(t,this.observer,n,i,t.manifestCodec);var o=ki(t,n,i,this.basePTS,this.frameIndex);if(o&&o.missing===0)return o},a}(xi),Ba=/\/emsg[-/]ID3/i,Oa=function(){function s(r,e){this.remainderData=null,this.timeOffset=0,this.config=void 0,this.videoTrack=void 0,this.audioTrack=void 0,this.id3Track=void 0,this.txtTrack=void 0,this.config=e}var a=s.prototype;return a.resetTimeStamp=function(){},a.resetInitSegment=function(e,t,n,i){var o=this.videoTrack=Ve("video",1),l=this.audioTrack=Ve("audio",1),d=this.txtTrack=Ve("text",1);if(this.id3Track=Ve("id3",1),this.timeOffset=0,!!(e!=null&&e.byteLength)){var u=fr(e);if(u.video){var c=u.video,p=c.id,g=c.timescale,y=c.codec;o.id=p,o.timescale=d.timescale=g,o.codec=y}if(u.audio){var b=u.audio,E=b.id,C=b.timescale,w=b.codec;l.id=E,l.timescale=C,l.codec=w}d.id=ur.text,o.sampleDuration=0,o.duration=l.duration=i}},a.resetContiguity=function(){},s.probe=function(e){return e=e.length>16384?e.subarray(0,16384):e,Te(e,["moof"]).length>0},a.demux=function(e,t){this.timeOffset=t;var n=e,i=this.videoTrack,o=this.txtTrack;if(this.config.progressive){this.remainderData&&(n=kt(this.remainderData,e));var l=$r(n);this.remainderData=l.remainder,i.samples=l.valid||new Uint8Array}else i.samples=n;var d=this.extractID3Track(i,t);return o.samples=un(t,i),{videoTrack:i,audioTrack:this.audioTrack,id3Track:d,textTrack:this.txtTrack}},a.flush=function(){var e=this.timeOffset,t=this.videoTrack,n=this.txtTrack;t.samples=this.remainderData||new Uint8Array,this.remainderData=null;var i=this.extractID3Track(t,this.timeOffset);return n.samples=un(e,t),{videoTrack:t,audioTrack:Ve(),id3Track:i,textTrack:Ve()}},a.extractID3Track=function(e,t){var n=this.id3Track;if(e.samples.length){var i=Te(e.samples,["emsg"]);i&&i.forEach(function(o){var l=ei(o);if(Ba.test(l.schemeIdUri)){var d=oe(l.presentationTime)?l.presentationTime/l.timeScale:t+l.presentationTimeDelta/l.timeScale,u=l.eventDuration===4294967295?Number.POSITIVE_INFINITY:l.eventDuration/l.timeScale;u<=.001&&(u=Number.POSITIVE_INFINITY);var c=l.payload;n.samples.push({data:c,len:c.byteLength,dts:d,pts:d,type:it.emsg,duration:u})}})}return n},a.demuxSampleAes=function(e,t,n){return Promise.reject(new Error("The MP4 demuxer does not support SAMPLE-AES decryption"))},a.destroy=function(){},s}(),Zn=null,Cn=[32,64,96,128,160,192,224,256,288,320,352,384,416,448,32,48,56,64,80,96,112,128,160,192,224,256,320,384,32,40,48,56,64,80,96,112,128,160,192,224,256,320,32,48,56,64,80,96,112,128,144,160,176,192,224,256,8,16,24,32,40,48,56,64,80,96,112,128,144,160],Ma=[44100,48e3,32e3,22050,24e3,16e3,11025,12e3,8e3],Fa=[[0,72,144,12],[0,0,0,0],[0,72,144,12],[0,144,144,12]],Na=[0,1,1,4];function jt(s,a,r,e,t){if(!(r+24>a.length)){var n=wi(a,r);if(n&&r+n.frameLength<=a.length){var i=n.samplesPerFrame*9e4/n.sampleRate,o=e+t*i,l={unit:a.subarray(r,r+n.frameLength),pts:o,dts:o};return s.config=[],s.channelCount=n.channelCount,s.samplerate=n.sampleRate,s.samples.push(l),{sample:l,length:n.frameLength,missing:0}}}}function wi(s,a){var r=s[a+1]>>3&3,e=s[a+1]>>1&3,t=s[a+2]>>4&15,n=s[a+2]>>2&3;if(r!==1&&t!==0&&t!==15&&n!==3){var i=s[a+2]>>1&1,o=s[a+3]>>6,l=r===3?3-e:e===3?3:4,d=Cn[l*14+t-1]*1e3,u=r===3?0:r===2?1:2,c=Ma[u*3+n],p=o===3?1:2,g=Fa[r][e],y=Na[e],b=g*8*y,E=Math.floor(g*d/c+i)*y;if(Zn===null){var C=navigator.userAgent||"",w=C.match(/Chrome\/(\d+)/i);Zn=w?parseInt(w[1]):0}var k=!!Zn&&Zn<=87;return k&&e===2&&d>=224e3&&o===0&&(s[a+3]=s[a+3]|128),{sampleRate:c,channelCount:p,frameLength:E,samplesPerFrame:b}}}function Mr(s,a){return s[a]===255&&(s[a+1]&224)==224&&(s[a+1]&6)!=0}function Li(s,a){return a+1<s.length&&Mr(s,a)}function Ua(s,a){var r=4;return Mr(s,a)&&r<=s.length-a}function Ka(s,a){if(a+1<s.length&&Mr(s,a)){var r=4,e=wi(s,a),t=r;e!=null&&e.frameLength&&(t=e.frameLength);var n=a+t;return n===s.length||Li(s,n)}return!1}var Qt=function(){function s(r){this.data=void 0,this.bytesAvailable=void 0,this.word=void 0,this.bitsAvailable=void 0,this.data=r,this.bytesAvailable=r.byteLength,this.word=0,this.bitsAvailable=0}var a=s.prototype;return a.loadWord=function(){var e=this.data,t=this.bytesAvailable,n=e.byteLength-t,i=new Uint8Array(4),o=Math.min(4,t);if(o===0)throw new Error("no bytes available");i.set(e.subarray(n,n+o)),this.word=new DataView(i.buffer).getUint32(0),this.bitsAvailable=o*8,this.bytesAvailable-=o},a.skipBits=function(e){var t;e=Math.min(e,this.bytesAvailable*8+this.bitsAvailable),this.bitsAvailable>e?(this.word<<=e,this.bitsAvailable-=e):(e-=this.bitsAvailable,t=e>>3,e-=t<<3,this.bytesAvailable-=t,this.loadWord(),this.word<<=e,this.bitsAvailable-=e)},a.readBits=function(e){var t=Math.min(this.bitsAvailable,e),n=this.word>>>32-t;if(e>32&&P.error("Cannot read more than 32 bits at a time"),this.bitsAvailable-=t,this.bitsAvailable>0)this.word<<=t;else if(this.bytesAvailable>0)this.loadWord();else throw new Error("no bits available");return t=e-t,t>0&&this.bitsAvailable?n<<t|this.readBits(t):n},a.skipLZ=function(){var e;for(e=0;e<this.bitsAvailable;++e)if((this.word&2147483648>>>e)!=0)return this.word<<=e,this.bitsAvailable-=e,e;return this.loadWord(),e+this.skipLZ()},a.skipUEG=function(){this.skipBits(1+this.skipLZ())},a.skipEG=function(){this.skipBits(1+this.skipLZ())},a.readUEG=function(){var e=this.skipLZ();return this.readBits(e+1)-1},a.readEG=function(){var e=this.readUEG();return 1&e?1+e>>>1:-1*(e>>>1)},a.readBoolean=function(){return this.readBits(1)===1},a.readUByte=function(){return this.readBits(8)},a.readUShort=function(){return this.readBits(16)},a.readUInt=function(){return this.readBits(32)},a.skipScalingList=function(e){for(var t=8,n=8,i,o=0;o<e;o++)n!==0&&(i=this.readEG(),n=(t+i+256)%256),t=n===0?t:n},a.readSPS=function(){var e=0,t=0,n=0,i=0,o,l,d,u=this.readUByte.bind(this),c=this.readBits.bind(this),p=this.readUEG.bind(this),g=this.readBoolean.bind(this),y=this.skipBits.bind(this),b=this.skipEG.bind(this),E=this.skipUEG.bind(this),C=this.skipScalingList.bind(this);u();var w=u();if(c(5),y(3),u(),E(),w===100||w===110||w===122||w===244||w===44||w===83||w===86||w===118||w===128){var k=p();if(k===3&&y(1),E(),E(),y(1),g())for(l=k!==3?8:12,d=0;d<l;d++)g()&&(d<6?C(16):C(64))}E();var D=p();if(D===0)p();else if(D===1)for(y(1),b(),b(),o=p(),d=0;d<o;d++)b();E(),y(1);var _=p(),F=p(),O=c(1);O===0&&y(1),y(1),g()&&(e=p(),t=p(),n=p(),i=p());var M=[1,1];if(g()&&g()){var z=u();switch(z){case 1:M=[1,1];break;case 2:M=[12,11];break;case 3:M=[10,11];break;case 4:M=[16,11];break;case 5:M=[40,33];break;case 6:M=[24,11];break;case 7:M=[20,11];break;case 8:M=[32,11];break;case 9:M=[80,33];break;case 10:M=[18,11];break;case 11:M=[15,11];break;case 12:M=[64,33];break;case 13:M=[160,99];break;case 14:M=[4,3];break;case 15:M=[3,2];break;case 16:M=[2,1];break;case 255:{M=[u()<<8|u(),u()<<8|u()];break}}}return{width:Math.ceil((_+1)*16-e*2-t*2),height:(2-O)*(F+1)*16-(O?2:4)*(n+i),pixelRatio:M}},a.readSliceType=function(){return this.readUByte(),this.readUEG(),this.readUEG()},s}(),Ii=function(){function s(r,e,t){this.keyData=void 0,this.decrypter=void 0,this.keyData=t,this.decrypter=new Pr(e,{removePKCS7Padding:!1})}var a=s.prototype;return a.decryptBuffer=function(e){return this.decrypter.decrypt(e,this.keyData.key.buffer,this.keyData.iv.buffer)},a.decryptAacSample=function(e,t,n){var i=this,o=e[t].unit;if(!(o.length<=16)){var l=o.subarray(16,o.length-o.length%16),d=l.buffer.slice(l.byteOffset,l.byteOffset+l.length);this.decryptBuffer(d).then(function(u){var c=new Uint8Array(u);o.set(c,16),i.decrypter.isSync()||i.decryptAacSamples(e,t+1,n)})}},a.decryptAacSamples=function(e,t,n){for(;;t++){if(t>=e.length){n();return}if(!(e[t].unit.length<32)&&(this.decryptAacSample(e,t,n),!this.decrypter.isSync()))return}},a.getAvcEncryptedData=function(e){for(var t=Math.floor((e.length-48)/160)*16+16,n=new Int8Array(t),i=0,o=32;o<e.length-16;o+=160,i+=16)n.set(e.subarray(o,o+16),i);return n},a.getAvcDecryptedUnit=function(e,t){for(var n=new Uint8Array(t),i=0,o=32;o<e.length-16;o+=160,i+=16)e.set(n.subarray(i,i+16),o);return e},a.decryptAvcSample=function(e,t,n,i,o){var l=this,d=vr(o.data),u=this.getAvcEncryptedData(d);this.decryptBuffer(u.buffer).then(function(c){o.data=l.getAvcDecryptedUnit(d,c),l.decrypter.isSync()||l.decryptAvcSamples(e,t,n+1,i)})},a.decryptAvcSamples=function(e,t,n,i){if(e instanceof Uint8Array)throw new Error("Cannot decrypt samples of type Uint8Array");for(;;t++,n=0){if(t>=e.length){i();return}for(var o=e[t].units;!(n>=o.length);n++){var l=o[n];if(!(l.data.length<=48||l.type!==1&&l.type!==5)&&(this.decryptAvcSample(e,t,n,i,l),!this.decrypter.isSync()))return}}},s}(),je=188,Ri=function(){function s(r,e,t){this.observer=void 0,this.config=void 0,this.typeSupported=void 0,this.sampleAes=null,this.pmtParsed=!1,this.audioCodec=void 0,this.videoCodec=void 0,this._duration=0,this._pmtId=-1,this._avcTrack=void 0,this._audioTrack=void 0,this._id3Track=void 0,this._txtTrack=void 0,this.aacOverFlow=null,this.avcSample=null,this.remainderData=null,this.observer=r,this.config=e,this.typeSupported=t}s.probe=function(e){var t=s.syncOffset(e);return t>0&&P.warn("MPEG2-TS detected but first sync word found @ offset "+t),t!==-1},s.syncOffset=function(e){for(var t=e.length,n=Math.min(je*5,e.length-je)+1,i=0;i<n;){for(var o=!1,l=i;l<t&&e[l]===71;l+=je)if(!o&&Jn(e,l)===0&&(o=!0),o&&l+je>n)return i;i++}return-1},s.createTrack=function(e,t){return{container:e==="video"||e==="audio"?"video/mp2t":void 0,type:e,id:ur[e],pid:-1,inputTimeScale:9e4,sequenceNumber:0,samples:[],dropped:0,duration:e==="audio"?t:void 0}};var a=s.prototype;return a.resetInitSegment=function(e,t,n,i){this.pmtParsed=!1,this._pmtId=-1,this._avcTrack=s.createTrack("video"),this._audioTrack=s.createTrack("audio",i),this._id3Track=s.createTrack("id3"),this._txtTrack=s.createTrack("text"),this._audioTrack.segmentCodec="aac",this.aacOverFlow=null,this.avcSample=null,this.remainderData=null,this.audioCodec=t,this.videoCodec=n,this._duration=i},a.resetTimeStamp=function(){},a.resetContiguity=function(){var e=this._audioTrack,t=this._avcTrack,n=this._id3Track;e&&(e.pesData=null),t&&(t.pesData=null),n&&(n.pesData=null),this.aacOverFlow=null,this.avcSample=null,this.remainderData=null},a.demux=function(e,t,n,i){n===void 0&&(n=!1),i===void 0&&(i=!1),n||(this.sampleAes=null);var o,l=this._avcTrack,d=this._audioTrack,u=this._id3Track,c=this._txtTrack,p=l.pid,g=l.pesData,y=d.pid,b=u.pid,E=d.pesData,C=u.pesData,w=null,k=this.pmtParsed,D=this._pmtId,_=e.length;if(this.remainderData&&(e=kt(this.remainderData,e),_=e.length,this.remainderData=null),_<je&&!i)return this.remainderData=e,{audioTrack:d,videoTrack:l,id3Track:u,textTrack:c};var F=Math.max(0,s.syncOffset(e));_-=(_-F)%je,_<e.byteLength&&!i&&(this.remainderData=new Uint8Array(e.buffer,_,e.buffer.byteLength-_));for(var O=0,M=F;M<_;M+=je)if(e[M]===71){var z=!!(e[M+1]&64),H=Jn(e,M),G=(e[M+3]&48)>>4,te=void 0;if(G>1){if(te=M+5+e[M+4],te===M+je)continue}else te=M+4;switch(H){case p:z&&(g&&(o=Zt(g))&&this.parseAVCPES(l,c,o,!1),g={data:[],size:0}),g&&(g.data.push(e.subarray(te,M+je)),g.size+=M+je-te);break;case y:if(z){if(E&&(o=Zt(E)))switch(d.segmentCodec){case"aac":this.parseAACPES(d,o);break;case"mp3":this.parseMPEGPES(d,o);break}E={data:[],size:0}}E&&(E.data.push(e.subarray(te,M+je)),E.size+=M+je-te);break;case b:z&&(C&&(o=Zt(C))&&this.parseID3PES(u,o),C={data:[],size:0}),C&&(C.data.push(e.subarray(te,M+je)),C.size+=M+je-te);break;case 0:z&&(te+=e[te]+1),D=this._pmtId=Xt(e,te);break;case D:{z&&(te+=e[te]+1);var ye=Ga(e,te,this.typeSupported,n);p=ye.avc,p>0&&(l.pid=p),y=ye.audio,y>0&&(d.pid=y,d.segmentCodec=ye.segmentCodec),b=ye.id3,b>0&&(u.pid=b),w!==null&&!k&&(P.warn("MPEG-TS PMT found at "+M+" after unknown PID '"+w+"'. Backtracking to sync byte @"+F+" to parse all TS packets."),w=null,M=F-188),k=this.pmtParsed=!0;break}case 17:case 8191:break;default:w=H;break}}else O++;if(O>0){var Ae=new Error("Found "+O+" TS packet/s that do not start with 0x47");this.observer.emit(S.ERROR,S.ERROR,{type:fe.MEDIA_ERROR,details:U.FRAG_PARSING_ERROR,fatal:!1,error:Ae,reason:Ae.message})}l.pesData=g,d.pesData=E,u.pesData=C;var Ee={audioTrack:d,videoTrack:l,id3Track:u,textTrack:c};return i&&this.extractRemainingSamples(Ee),Ee},a.flush=function(){var e=this.remainderData;this.remainderData=null;var t;return e?t=this.demux(e,-1,!1,!0):t={videoTrack:this._avcTrack,audioTrack:this._audioTrack,id3Track:this._id3Track,textTrack:this._txtTrack},this.extractRemainingSamples(t),this.sampleAes?this.decrypt(t,this.sampleAes):t},a.extractRemainingSamples=function(e){var t=e.audioTrack,n=e.videoTrack,i=e.id3Track,o=e.textTrack,l=n.pesData,d=t.pesData,u=i.pesData,c;if(l&&(c=Zt(l))?(this.parseAVCPES(n,o,c,!0),n.pesData=null):n.pesData=l,d&&(c=Zt(d))){switch(t.segmentCodec){case"aac":this.parseAACPES(t,c);break;case"mp3":this.parseMPEGPES(t,c);break}t.pesData=null}else d!=null&&d.size&&P.log("last AAC PES packet truncated,might overlap between fragments"),t.pesData=d;u&&(c=Zt(u))?(this.parseID3PES(i,c),i.pesData=null):i.pesData=u},a.demuxSampleAes=function(e,t,n){var i=this.demux(e,n,!0,!this.config.progressive),o=this.sampleAes=new Ii(this.observer,this.config,t);return this.decrypt(i,o)},a.decrypt=function(e,t){return new Promise(function(n){var i=e.audioTrack,o=e.videoTrack;i.samples&&i.segmentCodec==="aac"?t.decryptAacSamples(i.samples,0,function(){o.samples?t.decryptAvcSamples(o.samples,0,0,function(){n(e)}):n(e)}):o.samples&&t.decryptAvcSamples(o.samples,0,0,function(){n(e)})})},a.destroy=function(){this._duration=0},a.parseAVCPES=function(e,t,n,i){var o=this,l=this.parseAVCNALu(e,n.data),d=this.avcSample,u,c=!1;n.data=null,d&&l.length&&!e.audFound&&(Fr(d,e),d=this.avcSample=kn(!1,n.pts,n.dts,"")),l.forEach(function(p){switch(p.type){case 1:{u=!0,d||(d=o.avcSample=kn(!0,n.pts,n.dts,"")),d.frame=!0;var g=p.data;if(c&&g.length>4){var y=new Qt(g).readSliceType();(y===2||y===4||y===7||y===9)&&(d.key=!0)}break}case 5:u=!0,d||(d=o.avcSample=kn(!0,n.pts,n.dts,"")),d.key=!0,d.frame=!0;break;case 6:{u=!0,Kt(p.data,1,n.pts,t.samples);break}case 7:if(u=!0,c=!0,!e.sps){var b=p.data,E=new Qt(b),C=E.readSPS();e.width=C.width,e.height=C.height,e.pixelRatio=C.pixelRatio,e.sps=[b],e.duration=o._duration;for(var w=b.subarray(1,4),k="avc1.",D=0;D<3;D++){var _=w[D].toString(16);_.length<2&&(_="0"+_),k+=_}e.codec=k}break;case 8:u=!0,e.pps||(e.pps=[p.data]);break;case 9:u=!1,e.audFound=!0,d&&Fr(d,e),d=o.avcSample=kn(!1,n.pts,n.dts,"");break;case 12:u=!0;break;default:u=!1,d&&(d.debug+="unknown NAL "+p.type+" ");break}if(d&&u){var F=d.units;F.push(p)}}),i&&d&&(Fr(d,e),this.avcSample=null)},a.getLastNalUnit=function(e){var t,n=this.avcSample,i;if((!n||n.units.length===0)&&(n=e[e.length-1]),(t=n)!=null&&t.units){var o=n.units;i=o[o.length-1]}return i},a.parseAVCNALu=function(e,t){var n=t.byteLength,i=e.naluState||0,o=i,l=[],d=0,u,c,p,g=-1,y=0;for(i===-1&&(g=0,y=t[0]&31,i=0,d=1);d<n;){if(u=t[d++],!i){i=u?0:1;continue}if(i===1){i=u?0:2;continue}if(!u)i=3;else if(u===1){if(g>=0){var b={data:t.subarray(g,d-i-1),type:y};l.push(b)}else{var E=this.getLastNalUnit(e.samples);if(E&&(o&&d<=4-o&&E.state&&(E.data=E.data.subarray(0,E.data.byteLength-o)),c=d-i-1,c>0)){var C=new Uint8Array(E.data.byteLength+c);C.set(E.data,0),C.set(t.subarray(0,c),E.data.byteLength),E.data=C,E.state=0}}d<n?(p=t[d]&31,g=d,y=p,i=0):i=-1}else i=0}if(g>=0&&i>=0){var w={data:t.subarray(g,n),type:y,state:i};l.push(w)}if(l.length===0){var k=this.getLastNalUnit(e.samples);if(k){var D=new Uint8Array(k.data.byteLength+t.byteLength);D.set(k.data,0),D.set(t,k.data.byteLength),k.data=D}}return e.naluState=i,l},a.parseAACPES=function(e,t){var n=0,i=this.aacOverFlow,o=t.data;if(i){this.aacOverFlow=null;var l=i.missing,d=i.sample.unit.byteLength;if(l===-1){var u=new Uint8Array(d+o.byteLength);u.set(i.sample.unit,0),u.set(o,d),o=u}else{var c=d-l;i.sample.unit.set(o.subarray(0,l),c),e.samples.push(i.sample),n=i.missing}}var p,g;for(p=n,g=o.length;p<g-1&&!Xn(o,p);p++);if(p!==n){var y,b=p<g-1;b?y="AAC PES did not start with ADTS header,offset:"+p:y="No ADTS header found in AAC PES";var E=new Error(y);if(P.warn("parsing error: "+y),this.observer.emit(S.ERROR,S.ERROR,{type:fe.MEDIA_ERROR,details:U.FRAG_PARSING_ERROR,fatal:!1,levelRetry:b,error:E,reason:y}),!b)return}Vt(e,this.observer,o,p,this.audioCodec);var C;if(t.pts!==void 0)C=t.pts;else if(i){var w=Ci(e.samplerate);C=i.sample.pts+w}else{P.warn("[tsdemuxer]: AAC PES unknown PTS");return}for(var k=0,D;p<g;)if(D=ki(e,o,p,C,k),p+=D.length,D.missing){this.aacOverFlow=D;break}else for(k++;p<g-1&&!Xn(o,p);p++);},a.parseMPEGPES=function(e,t){var n=t.data,i=n.length,o=0,l=0,d=t.pts;if(d===void 0){P.warn("[tsdemuxer]: MPEG PES unknown PTS");return}for(;l<i;)if(Li(n,l)){var u=jt(e,n,l,d,o);if(u)l+=u.length,o++;else break}else l++},a.parseID3PES=function(e,t){if(t.pts===void 0){P.warn("[tsdemuxer]: ID3 PES unknown PTS");return}var n=q({},t,{type:this._avcTrack?it.emsg:it.audioId3,duration:Number.POSITIVE_INFINITY});e.samples.push(n)},s}();function kn(s,a,r,e){return{key:s,frame:!1,pts:a,dts:r,units:[],debug:e,length:0}}function Jn(s,a){return((s[a+1]&31)<<8)+s[a+2]}function Xt(s,a){return(s[a+10]&31)<<8|s[a+11]}function Ga(s,a,r,e){var t={audio:-1,avc:-1,id3:-1,segmentCodec:"aac"},n=(s[a+1]&15)<<8|s[a+2],i=a+3+n-4,o=(s[a+10]&15)<<8|s[a+11];for(a+=12+o;a<i;){var l=Jn(s,a);switch(s[a]){case 207:if(!e){P.log("ADTS AAC with AES-128-CBC frame encryption found in unencrypted stream");break}case 15:t.audio===-1&&(t.audio=l);break;case 21:t.id3===-1&&(t.id3=l);break;case 219:if(!e){P.log("H.264 with AES-128-CBC slice encryption found in unencrypted stream");break}case 27:t.avc===-1&&(t.avc=l);break;case 3:case 4:r.mpeg!==!0&&r.mp3!==!0?P.log("MPEG audio found, not supported in this browser"):t.audio===-1&&(t.audio=l,t.segmentCodec="mp3");break;case 36:P.warn("Unsupported HEVC stream type found");break}a+=((s[a+3]&15)<<8|s[a+4])+5}return t}function Zt(s){var a=0,r,e,t,n,i,o=s.data;if(!s||s.size===0)return null;for(;o[0].length<19&&o.length>1;){var l=new Uint8Array(o[0].length+o[1].length);l.set(o[0]),l.set(o[1],o[0].length),o[0]=l,o.splice(1,1)}r=o[0];var d=(r[0]<<16)+(r[1]<<8)+r[2];if(d===1){if(e=(r[4]<<8)+r[5],e&&e>s.size-6)return null;var u=r[7];u&192&&(n=(r[9]&14)*536870912+(r[10]&255)*4194304+(r[11]&254)*16384+(r[12]&255)*128+(r[13]&254)/2,u&64?(i=(r[14]&14)*536870912+(r[15]&255)*4194304+(r[16]&254)*16384+(r[17]&255)*128+(r[18]&254)/2,n-i>60*9e4&&(P.warn(Math.round((n-i)/9e4)+"s delta between PTS and DTS, align them"),n=i)):i=n),t=r[8];var c=t+9;if(s.size<=c)return null;s.size-=c;for(var p=new Uint8Array(s.size),g=0,y=o.length;g<y;g++){r=o[g];var b=r.byteLength;if(c)if(c>b){c-=b;continue}else r=r.subarray(c),b-=c,c=0;p.set(r,a),a+=b}return e&&(e-=t+3),{data:p,pts:n,dts:i,len:e}}return null}function Fr(s,a){if(s.units.length&&s.frame){if(s.pts===void 0){var r=a.samples,e=r.length;if(e){var t=r[e-1];s.pts=t.pts,s.dts=t.dts}else{a.dropped++;return}}a.samples.push(s)}s.debug.length&&P.log(s.pts+"/"+s.dts+":"+s.debug)}var h=function(s){j(a,s);function a(){return s.apply(this,arguments)||this}var r=a.prototype;return r.resetInitSegment=function(t,n,i,o){s.prototype.resetInitSegment.call(this,t,n,i,o),this._audioTrack={container:"audio/mpeg",type:"audio",id:2,pid:-1,sequenceNumber:0,segmentCodec:"mp3",samples:[],manifestCodec:n,duration:o,inputTimeScale:9e4,dropped:0}},a.probe=function(t){if(!t)return!1;for(var n=Mn(t,0)||[],i=n.length,o=t.length;i<o;i++)if(Ka(t,i))return P.log("MPEG Audio sync word found !"),!0;return!1},r.canParse=function(t,n){return Ua(t,n)},r.appendFrame=function(t,n,i){if(this.basePTS!==null)return jt(t,n,i,this.basePTS,this.frameIndex)},a}(xi),v=function(){function s(){}return s.getSilentFrame=function(r,e){switch(r){case"mp4a.40.2":if(e===1)return new Uint8Array([0,200,0,128,35,128]);if(e===2)return new Uint8Array([33,0,73,144,2,25,0,35,128]);if(e===3)return new Uint8Array([0,200,0,128,32,132,1,38,64,8,100,0,142]);if(e===4)return new Uint8Array([0,200,0,128,32,132,1,38,64,8,100,0,128,44,128,8,2,56]);if(e===5)return new Uint8Array([0,200,0,128,32,132,1,38,64,8,100,0,130,48,4,153,0,33,144,2,56]);if(e===6)return new Uint8Array([0,200,0,128,32,132,1,38,64,8,100,0,130,48,4,153,0,33,144,2,0,178,0,32,8,224]);break;default:if(e===1)return new Uint8Array([1,64,34,128,163,78,230,128,186,8,0,0,0,28,6,241,193,10,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,94]);if(e===2)return new Uint8Array([1,64,34,128,163,94,230,128,186,8,0,0,0,0,149,0,6,241,161,10,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,94]);if(e===3)return new Uint8Array([1,64,34,128,163,94,230,128,186,8,0,0,0,0,149,0,6,241,161,10,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,94]);break}},s}(),x=Math.pow(2,32)-1,f=function(){function s(){}return s.init=function(){s.types={avc1:[],avcC:[],btrt:[],dinf:[],dref:[],esds:[],ftyp:[],hdlr:[],mdat:[],mdhd:[],mdia:[],mfhd:[],minf:[],moof:[],moov:[],mp4a:[],".mp3":[],mvex:[],mvhd:[],pasp:[],sdtp:[],stbl:[],stco:[],stsc:[],stsd:[],stsz:[],stts:[],tfdt:[],tfhd:[],traf:[],trak:[],trun:[],trex:[],tkhd:[],vmhd:[],smhd:[]};var r;for(r in s.types)s.types.hasOwnProperty(r)&&(s.types[r]=[r.charCodeAt(0),r.charCodeAt(1),r.charCodeAt(2),r.charCodeAt(3)]);var e=new Uint8Array([0,0,0,0,0,0,0,0,118,105,100,101,0,0,0,0,0,0,0,0,0,0,0,0,86,105,100,101,111,72,97,110,100,108,101,114,0]),t=new Uint8Array([0,0,0,0,0,0,0,0,115,111,117,110,0,0,0,0,0,0,0,0,0,0,0,0,83,111,117,110,100,72,97,110,100,108,101,114,0]);s.HDLR_TYPES={video:e,audio:t};var n=new Uint8Array([0,0,0,0,0,0,0,1,0,0,0,12,117,114,108,32,0,0,0,1]),i=new Uint8Array([0,0,0,0,0,0,0,0]);s.STTS=s.STSC=s.STCO=i,s.STSZ=new Uint8Array([0,0,0,0,0,0,0,0,0,0,0,0]),s.VMHD=new Uint8Array([0,0,0,1,0,0,0,0,0,0,0,0]),s.SMHD=new Uint8Array([0,0,0,0,0,0,0,0]),s.STSD=new Uint8Array([0,0,0,0,0,0,0,1]);var o=new Uint8Array([105,115,111,109]),l=new Uint8Array([97,118,99,49]),d=new Uint8Array([0,0,0,1]);s.FTYP=s.box(s.types.ftyp,o,d,o,l),s.DINF=s.box(s.types.dinf,s.box(s.types.dref,n))},s.box=function(r){for(var e=8,t=arguments.length,n=new Array(t>1?t-1:0),i=1;i<t;i++)n[i-1]=arguments[i];for(var o=n.length,l=o;o--;)e+=n[o].byteLength;var d=new Uint8Array(e);for(d[0]=e>>24&255,d[1]=e>>16&255,d[2]=e>>8&255,d[3]=e&255,d.set(r,4),o=0,e=8;o<l;o++)d.set(n[o],e),e+=n[o].byteLength;return d},s.hdlr=function(r){return s.box(s.types.hdlr,s.HDLR_TYPES[r])},s.mdat=function(r){return s.box(s.types.mdat,r)},s.mdhd=function(r,e){e*=r;var t=Math.floor(e/(x+1)),n=Math.floor(e%(x+1));return s.box(s.types.mdhd,new Uint8Array([1,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,3,r>>24&255,r>>16&255,r>>8&255,r&255,t>>24,t>>16&255,t>>8&255,t&255,n>>24,n>>16&255,n>>8&255,n&255,85,196,0,0]))},s.mdia=function(r){return s.box(s.types.mdia,s.mdhd(r.timescale,r.duration),s.hdlr(r.type),s.minf(r))},s.mfhd=function(r){return s.box(s.types.mfhd,new Uint8Array([0,0,0,0,r>>24,r>>16&255,r>>8&255,r&255]))},s.minf=function(r){return r.type==="audio"?s.box(s.types.minf,s.box(s.types.smhd,s.SMHD),s.DINF,s.stbl(r)):s.box(s.types.minf,s.box(s.types.vmhd,s.VMHD),s.DINF,s.stbl(r))},s.moof=function(r,e,t){return s.box(s.types.moof,s.mfhd(r),s.traf(t,e))},s.moov=function(r){for(var e=r.length,t=[];e--;)t[e]=s.trak(r[e]);return s.box.apply(null,[s.types.moov,s.mvhd(r[0].timescale,r[0].duration)].concat(t).concat(s.mvex(r)))},s.mvex=function(r){for(var e=r.length,t=[];e--;)t[e]=s.trex(r[e]);return s.box.apply(null,[s.types.mvex].concat(t))},s.mvhd=function(r,e){e*=r;var t=Math.floor(e/(x+1)),n=Math.floor(e%(x+1)),i=new Uint8Array([1,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,3,r>>24&255,r>>16&255,r>>8&255,r&255,t>>24,t>>16&255,t>>8&255,t&255,n>>24,n>>16&255,n>>8&255,n&255,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,64,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,255,255,255,255]);return s.box(s.types.mvhd,i)},s.sdtp=function(r){var e=r.samples||[],t=new Uint8Array(4+e.length),n,i;for(n=0;n<e.length;n++)i=e[n].flags,t[n+4]=i.dependsOn<<4|i.isDependedOn<<2|i.hasRedundancy;return s.box(s.types.sdtp,t)},s.stbl=function(r){return s.box(s.types.stbl,s.stsd(r),s.box(s.types.stts,s.STTS),s.box(s.types.stsc,s.STSC),s.box(s.types.stsz,s.STSZ),s.box(s.types.stco,s.STCO))},s.avc1=function(r){var e=[],t=[],n,i,o;for(n=0;n<r.sps.length;n++)i=r.sps[n],o=i.byteLength,e.push(o>>>8&255),e.push(o&255),e=e.concat(Array.prototype.slice.call(i));for(n=0;n<r.pps.length;n++)i=r.pps[n],o=i.byteLength,t.push(o>>>8&255),t.push(o&255),t=t.concat(Array.prototype.slice.call(i));var l=s.box(s.types.avcC,new Uint8Array([1,e[3],e[4],e[5],252|3,224|r.sps.length].concat(e).concat([r.pps.length]).concat(t))),d=r.width,u=r.height,c=r.pixelRatio[0],p=r.pixelRatio[1];return s.box(s.types.avc1,new Uint8Array([0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,d>>8&255,d&255,u>>8&255,u&255,0,72,0,0,0,72,0,0,0,0,0,0,0,1,18,100,97,105,108,121,109,111,116,105,111,110,47,104,108,115,46,106,115,0,0,0,0,0,0,0,0,0,0,0,0,0,0,24,17,17]),l,s.box(s.types.btrt,new Uint8Array([0,28,156,128,0,45,198,192,0,45,198,192])),s.box(s.types.pasp,new Uint8Array([c>>24,c>>16&255,c>>8&255,c&255,p>>24,p>>16&255,p>>8&255,p&255])))},s.esds=function(r){var e=r.config.length;return new Uint8Array([0,0,0,0,3,23+e,0,1,0,4,15+e,64,21,0,0,0,0,0,0,0,0,0,0,0,5].concat([e]).concat(r.config).concat([6,1,2]))},s.mp4a=function(r){var e=r.samplerate;return s.box(s.types.mp4a,new Uint8Array([0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,r.channelCount,0,16,0,0,0,0,e>>8&255,e&255,0,0]),s.box(s.types.esds,s.esds(r)))},s.mp3=function(r){var e=r.samplerate;return s.box(s.types[".mp3"],new Uint8Array([0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,r.channelCount,0,16,0,0,0,0,e>>8&255,e&255,0,0]))},s.stsd=function(r){return r.type==="audio"?r.segmentCodec==="mp3"&&r.codec==="mp3"?s.box(s.types.stsd,s.STSD,s.mp3(r)):s.box(s.types.stsd,s.STSD,s.mp4a(r)):s.box(s.types.stsd,s.STSD,s.avc1(r))},s.tkhd=function(r){var e=r.id,t=r.duration*r.timescale,n=r.width,i=r.height,o=Math.floor(t/(x+1)),l=Math.floor(t%(x+1));return s.box(s.types.tkhd,new Uint8Array([1,0,0,7,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,3,e>>24&255,e>>16&255,e>>8&255,e&255,0,0,0,0,o>>24,o>>16&255,o>>8&255,o&255,l>>24,l>>16&255,l>>8&255,l&255,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,64,0,0,0,n>>8&255,n&255,0,0,i>>8&255,i&255,0,0]))},s.traf=function(r,e){var t=s.sdtp(r),n=r.id,i=Math.floor(e/(x+1)),o=Math.floor(e%(x+1));return s.box(s.types.traf,s.box(s.types.tfhd,new Uint8Array([0,0,0,0,n>>24,n>>16&255,n>>8&255,n&255])),s.box(s.types.tfdt,new Uint8Array([1,0,0,0,i>>24,i>>16&255,i>>8&255,i&255,o>>24,o>>16&255,o>>8&255,o&255])),s.trun(r,t.length+16+20+8+16+8+8),t)},s.trak=function(r){return r.duration=r.duration||4294967295,s.box(s.types.trak,s.tkhd(r),s.mdia(r))},s.trex=function(r){var e=r.id;return s.box(s.types.trex,new Uint8Array([0,0,0,0,e>>24,e>>16&255,e>>8&255,e&255,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,1]))},s.trun=function(r,e){var t=r.samples||[],n=t.length,i=12+16*n,o=new Uint8Array(i),l,d,u,c,p,g;for(e+=8+i,o.set([r.type==="video"?1:0,0,15,1,n>>>24&255,n>>>16&255,n>>>8&255,n&255,e>>>24&255,e>>>16&255,e>>>8&255,e&255],0),l=0;l<n;l++)d=t[l],u=d.duration,c=d.size,p=d.flags,g=d.cts,o.set([u>>>24&255,u>>>16&255,u>>>8&255,u&255,c>>>24&255,c>>>16&255,c>>>8&255,c&255,p.isLeading<<2|p.dependsOn,p.isDependedOn<<6|p.hasRedundancy<<4|p.paddingValue<<1|p.isNonSync,p.degradPrio&240<<8,p.degradPrio&15,g>>>24&255,g>>>16&255,g>>>8&255,g&255],12+16*l);return s.box(s.types.trun,o)},s.initSegment=function(r){s.types||s.init();var e=s.moov(r),t=new Uint8Array(s.FTYP.byteLength+e.byteLength);return t.set(s.FTYP),t.set(e,s.FTYP.byteLength),t},s}();f.types=void 0,f.HDLR_TYPES=void 0,f.STTS=void 0,f.STSC=void 0,f.STCO=void 0,f.STSZ=void 0,f.VMHD=void 0,f.SMHD=void 0,f.STSD=void 0,f.FTYP=void 0,f.DINF=void 0;var m=9e4;function A(s,a,r,e){r===void 0&&(r=1),e===void 0&&(e=!1);var t=s*a*r;return e?Math.round(t):t}function T(s,a,r,e){return r===void 0&&(r=1),e===void 0&&(e=!1),A(s,a,1/r,e)}function L(s,a){return a===void 0&&(a=!1),A(s,1e3,1/m,a)}function I(s,a){return a===void 0&&(a=1),A(s,m,1/a)}var B=10*1e3,N=1024,X=1152,J=null,re=null,$=function(){function s(r,e,t,n){if(this.observer=void 0,this.config=void 0,this.typeSupported=void 0,this.ISGenerated=!1,this._initPTS=null,this._initDTS=null,this.nextAvcDts=null,this.nextAudioPts=null,this.videoSampleDuration=null,this.isAudioContiguous=!1,this.isVideoContiguous=!1,this.observer=r,this.config=e,this.typeSupported=t,this.ISGenerated=!1,J===null){var i=navigator.userAgent||"",o=i.match(/Chrome\/(\d+)/i);J=o?parseInt(o[1]):0}if(re===null){var l=navigator.userAgent.match(/Safari\/(\d+)/i);re=l?parseInt(l[1]):0}}var a=s.prototype;return a.destroy=function(){},a.resetTimeStamp=function(e){P.log("[mp4-remuxer]: initPTS & initDTS reset"),this._initPTS=this._initDTS=e},a.resetNextTimestamp=function(){P.log("[mp4-remuxer]: reset next timestamp"),this.isVideoContiguous=!1,this.isAudioContiguous=!1},a.resetInitSegment=function(){P.log("[mp4-remuxer]: ISGenerated flag reset"),this.ISGenerated=!1},a.getVideoStartPts=function(e){var t=!1,n=e.reduce(function(i,o){var l=o.pts-i;return l<-4294967296?(t=!0,W(i,o.pts)):l>0?i:o.pts},e[0].pts);return t&&P.debug("PTS rollover detected"),n},a.remux=function(e,t,n,i,o,l,d,u){var c,p,g,y,b,E,C=o,w=o,k=e.pid>-1,D=t.pid>-1,_=t.samples.length,F=e.samples.length>0,O=d&&_>0||_>1,M=(!k||F)&&(!D||O)||this.ISGenerated||d;if(M){this.ISGenerated||(g=this.generateIS(e,t,o,l));var z=this.isVideoContiguous,H=-1,G;if(O&&(H=ae(t.samples),!z&&this.config.forceKeyFrameOnDiscontinuity))if(E=!0,H>0){P.warn("[mp4-remuxer]: Dropped "+H+" out of "+_+" video samples due to a missing keyframe");var te=this.getVideoStartPts(t.samples);t.samples=t.samples.slice(H),t.dropped+=H,w+=(t.samples[0].pts-te)/t.inputTimeScale,G=w}else H===-1&&(P.warn("[mp4-remuxer]: No keyframe found out of "+_+" video samples"),E=!1);if(this.ISGenerated){if(F&&O){var ye=this.getVideoStartPts(t.samples),Ae=W(e.samples[0].pts,ye)-ye,Ee=Ae/t.inputTimeScale;C+=Math.max(0,Ee),w+=Math.max(0,-Ee)}if(F){if(e.samplerate||(P.warn("[mp4-remuxer]: regenerate InitSegment as audio detected"),g=this.generateIS(e,t,o,l)),p=this.remuxAudio(e,C,this.isAudioContiguous,l,D||O||u===he.AUDIO?w:void 0),O){var we=p?p.endPTS-p.startPTS:0;t.inputTimeScale||(P.warn("[mp4-remuxer]: regenerate InitSegment as video detected"),g=this.generateIS(e,t,o,l)),c=this.remuxVideo(t,w,z,we)}}else O&&(c=this.remuxVideo(t,w,z,0));c&&(c.firstKeyFrame=H,c.independent=H!==-1,c.firstKeyFramePTS=G)}}return this.ISGenerated&&this._initPTS&&this._initDTS&&(n.samples.length&&(b=pe(n,o,this._initPTS,this._initDTS)),i.samples.length&&(y=Ue(i,o,this._initPTS))),{audio:p,video:c,initSegment:g,independent:E,text:y,id3:b}},a.generateIS=function(e,t,n,i){var o=e.samples,l=t.samples,d=this.typeSupported,u={},c=this._initPTS,p=!c||i,g="audio/mp4",y,b,E;if(p&&(y=b=Infinity),e.config&&o.length){switch(e.timescale=e.samplerate,e.segmentCodec){case"mp3":d.mpeg?(g="audio/mpeg",e.codec=""):d.mp3&&(e.codec="mp3");break}u.audio={id:"audio",container:g,codec:e.codec,initSegment:e.segmentCodec==="mp3"&&d.mpeg?new Uint8Array(0):f.initSegment([e]),metadata:{channelCount:e.channelCount}},p&&(E=e.inputTimeScale,!c||E!==c.timescale?y=b=o[0].pts-Math.round(E*n):p=!1)}if(t.sps&&t.pps&&l.length&&(t.timescale=t.inputTimeScale,u.video={id:"main",container:"video/mp4",codec:t.codec,initSegment:f.initSegment([t]),metadata:{width:t.width,height:t.height}},p))if(E=t.inputTimeScale,!c||E!==c.timescale){var C=this.getVideoStartPts(l),w=Math.round(E*n);b=Math.min(b,W(l[0].dts,C)-w),y=Math.min(y,C-w)}else p=!1;if(Object.keys(u).length)return this.ISGenerated=!0,p?(this._initPTS={baseTime:y,timescale:E},this._initDTS={baseTime:b,timescale:E}):y=E=void 0,{tracks:u,initPTS:y,timescale:E}},a.remuxVideo=function(e,t,n,i){var o=e.inputTimeScale,l=e.samples,d=[],u=l.length,c=this._initPTS,p=this.nextAvcDts,g=8,y=this.videoSampleDuration,b,E,C=Number.POSITIVE_INFINITY,w=Number.NEGATIVE_INFINITY,k=!1;if(!n||p===null){var D=t*o,_=l[0].pts-W(l[0].dts,l[0].pts);p=D-_}for(var F=c.baseTime*o/c.timescale,O=0;O<u;O++){var M=l[O];M.pts=W(M.pts-F,p),M.dts=W(M.dts-F,p),M.dts<l[O>0?O-1:O].dts&&(k=!0)}k&&l.sort(function(Fi,Vo){var bl=Fi.dts-Vo.dts,El=Fi.pts-Vo.pts;return bl||El}),b=l[0].dts,E=l[l.length-1].dts;var z=E-b,H=z?Math.round(z/(u-1)):y||e.inputTimeScale/30;if(n){var G=b-p,te=G>H,ye=G<-1;if((te||ye)&&(te?P.warn("AVC: "+L(G,!0)+" ms ("+G+"dts) hole between fragments detected, filling it"):P.warn("AVC: "+L(-G,!0)+" ms ("+G+"dts) overlapping between fragments detected"),!ye||p>l[0].pts)){b=p;var Ae=l[0].pts-G;l[0].dts=b,l[0].pts=Ae,P.log("Video: First PTS/DTS adjusted: "+L(Ae,!0)+"/"+L(b,!0)+", delta: "+L(G,!0)+" ms")}}b=Math.max(0,b);for(var Ee=0,we=0,xe=0;xe<u;xe++){for(var _e=l[xe],Re=_e.units,Be=Re.length,ut=0,Ne=0;Ne<Be;Ne++)ut+=Re[Ne].data.length;we+=ut,Ee+=Be,_e.length=ut,_e.dts=Math.max(_e.dts,b),C=Math.min(_e.pts,C),w=Math.max(_e.pts,w)}E=l[u-1].dts;var He=we+4*Ee+8,at;try{at=new Uint8Array(He)}catch(Fi){this.observer.emit(S.ERROR,S.ERROR,{type:fe.MUX_ERROR,details:U.REMUX_ALLOC_ERROR,fatal:!1,error:Fi,bytes:He,reason:"fail allocating video mdat "+He});return}var tt=new DataView(at.buffer);tt.setUint32(0,He),at.set(f.types.mdat,4);for(var ot=!1,Pt=Number.POSITIVE_INFINITY,Jt=Number.POSITIVE_INFINITY,xt=Number.NEGATIVE_INFINITY,Bt=Number.NEGATIVE_INFINITY,Ze=0;Ze<u;Ze++){for(var Je=l[Ze],$t=Je.units,Ot=0,en=0,oo=$t.length;en<oo;en++){var Mt=$t[en],fl=Mt.data,so=Mt.data.byteLength;tt.setUint32(g,so),g+=4,at.set(fl,g),g+=so,Ot+=4+so}var Oi=void 0;if(Ze<u-1)y=l[Ze+1].dts-Je.dts,Oi=l[Ze+1].pts-Je.pts;else{var qo=this.config,Mi=Ze>0?Je.dts-l[Ze-1].dts:H;if(Oi=Ze>0?Je.pts-l[Ze-1].pts:H,qo.stretchShortVideoTrack&&this.nextAudioPts!==null){var hl=Math.floor(qo.maxBufferHole*o),lo=(i?C+i*o:this.nextAudioPts)-Je.pts;lo>hl?(y=lo-Mi,y<0?y=Mi:ot=!0,P.log("[mp4-remuxer]: It is approximately "+lo/90+" ms to the next segment; using duration "+y/90+" ms for the last video frame.")):y=Mi}else y=Mi}var pl=Math.round(Je.pts-Je.dts);Pt=Math.min(Pt,y),xt=Math.max(xt,y),Jt=Math.min(Jt,Oi),Bt=Math.max(Bt,Oi),d.push(new Me(Je.key,y,Ot,pl))}if(d.length){if(J){if(J<70){var Yo=d[0].flags;Yo.dependsOn=2,Yo.isNonSync=0}}else if(re&&Bt-Jt<xt-Pt&&H/xt<.025&&d[0].cts===0){P.warn("Found irregular gaps in sample duration. Using PTS instead of DTS to determine MP4 sample duration.");for(var uo=b,vt=0,Wo=d.length;vt<Wo;vt++){var zo=uo+d[vt].duration,vl=uo+d[vt].cts;if(vt<Wo-1){var ml=zo+d[vt+1].cts;d[vt].duration=ml-vl}else d[vt].duration=vt?d[vt-1].duration:H;d[vt].cts=0,uo=zo}}}y=ot||!y?H:y,this.nextAvcDts=p=E+y,this.videoSampleDuration=y,this.isVideoContiguous=!0;var gl=f.moof(e.sequenceNumber++,b,q({},e,{samples:d})),yl="video",Al={data1:gl,data2:at,startPTS:C/o,endPTS:(w+y)/o,startDTS:b/o,endDTS:p/o,type:yl,hasAudio:!1,hasVideo:!0,nb:d.length,dropped:e.dropped};return e.samples=[],e.dropped=0,Al},a.remuxAudio=function(e,t,n,i,o){var l=e.inputTimeScale,d=e.samplerate?e.samplerate:l,u=l/d,c=e.segmentCodec==="aac"?N:X,p=c*u,g=this._initPTS,y=e.segmentCodec==="mp3"&&this.typeSupported.mpeg,b=[],E=o!==void 0,C=e.samples,w=y?0:8,k=this.nextAudioPts||-1,D=t*l,_=g.baseTime*l/g.timescale;if(this.isAudioContiguous=n=n||C.length&&k>0&&(i&&Math.abs(D-k)<9e3||Math.abs(W(C[0].pts-_,D)-k)<20*p),C.forEach(function(Mt){Mt.pts=W(Mt.pts-_,D)}),!n||k<0){if(C=C.filter(function(Mt){return Mt.pts>=0}),!C.length)return;o===0?k=0:i&&!E?k=Math.max(0,D):k=C[0].pts}if(e.segmentCodec==="aac")for(var F=this.config.maxAudioFramesDrift,O=0,M=k;O<C.length;O++){var z=C[O],H=z.pts,G=H-M,te=Math.abs(1e3*G/l);if(G<=-F*p&&E)O===0&&(P.warn("Audio frame @ "+(H/l).toFixed(3)+"s overlaps nextAudioPts by "+Math.round(1e3*G/l)+" ms."),this.nextAudioPts=k=M=H);else if(G>=F*p&&te<B&&E){var ye=Math.round(G/p);M=H-ye*p,M<0&&(ye--,M+=p),O===0&&(this.nextAudioPts=k=M),P.warn("[mp4-remuxer]: Injecting "+ye+" audio frame @ "+(M/l).toFixed(3)+"s due to "+Math.round(1e3*G/l)+" ms gap.");for(var Ae=0;Ae<ye;Ae++){var Ee=Math.max(M,0),we=v.getSilentFrame(e.manifestCodec||e.codec,e.channelCount);we||(P.log("[mp4-remuxer]: Unable to get silent frame for given audio codec; duplicating last frame instead."),we=z.unit.subarray()),C.splice(O,0,{unit:we,pts:Ee}),M+=p,O++}}z.pts=M,M+=p}for(var xe=null,_e=null,Re,Be=0,ut=C.length;ut--;)Be+=C[ut].unit.byteLength;for(var Ne=0,He=C.length;Ne<He;Ne++){var at=C[Ne],tt=at.unit,ot=at.pts;if(_e!==null){var Pt=b[Ne-1];Pt.duration=Math.round((ot-_e)/u)}else if(n&&e.segmentCodec==="aac"&&(ot=k),xe=ot,Be>0){Be+=w;try{Re=new Uint8Array(Be)}catch(Mt){this.observer.emit(S.ERROR,S.ERROR,{type:fe.MUX_ERROR,details:U.REMUX_ALLOC_ERROR,fatal:!1,error:Mt,bytes:Be,reason:"fail allocating audio mdat "+Be});return}if(!y){var Jt=new DataView(Re.buffer);Jt.setUint32(0,Be),Re.set(f.types.mdat,4)}}else return;Re.set(tt,w);var xt=tt.byteLength;w+=xt,b.push(new Me(!0,c,xt,0)),_e=ot}var Bt=b.length;if(!!Bt){var Ze=b[b.length-1];this.nextAudioPts=k=_e+u*Ze.duration;var Je=y?new Uint8Array(0):f.moof(e.sequenceNumber++,xe/u,q({},e,{samples:b}));e.samples=[];var $t=xe/l,Ot=k/l,en="audio",oo={data1:Je,data2:Re,startPTS:$t,endPTS:Ot,startDTS:$t,endDTS:Ot,type:en,hasAudio:!0,hasVideo:!1,nb:Bt};return this.isAudioContiguous=!0,oo}},a.remuxEmptyAudio=function(e,t,n,i){var o=e.inputTimeScale,l=e.samplerate?e.samplerate:o,d=o/l,u=this.nextAudioPts,c=this._initDTS,p=c.baseTime*9e4/c.timescale,g=(u!==null?u:i.startDTS*o)+p,y=i.endDTS*o+p,b=d*N,E=Math.ceil((y-g)/b),C=v.getSilentFrame(e.manifestCodec||e.codec,e.channelCount);if(P.warn("[mp4-remuxer]: remux empty Audio"),!C){P.trace("[mp4-remuxer]: Unable to remuxEmptyAudio since we were unable to get a silent frame for given audio codec");return}for(var w=[],k=0;k<E;k++){var D=g+k*b;w.push({unit:C,pts:D,dts:D})}return e.samples=w,this.remuxAudio(e,t,n,!1)},s}();function W(s,a){var r;if(a===null)return s;for(a<s?r=-8589934592:r=8589934592;Math.abs(s-a)>4294967296;)s+=r;return s}function ae(s){for(var a=0;a<s.length;a++)if(s[a].key)return a;return-1}function pe(s,a,r,e){var t=s.samples.length;if(!!t){for(var n=s.inputTimeScale,i=0;i<t;i++){var o=s.samples[i];o.pts=W(o.pts-r.baseTime*9e4/r.timescale,a*n)/n,o.dts=W(o.dts-e.baseTime*9e4/e.timescale,a*n)/n}var l=s.samples;return s.samples=[],{samples:l}}}function Ue(s,a,r){var e=s.samples.length;if(!!e){for(var t=s.inputTimeScale,n=0;n<e;n++){var i=s.samples[n];i.pts=W(i.pts-r.baseTime*9e4/r.timescale,a*t)/t}s.samples.sort(function(l,d){return l.pts-d.pts});var o=s.samples;return s.samples=[],{samples:o}}}var Me=function(a,r,e,t){this.size=void 0,this.duration=void 0,this.cts=void 0,this.flags=void 0,this.duration=r,this.size=e,this.cts=t,this.flags=new Ie(a)},Ie=function(a){this.isLeading=0,this.isDependedOn=0,this.hasRedundancy=0,this.degradPrio=0,this.dependsOn=1,this.isNonSync=1,this.dependsOn=a?2:1,this.isNonSync=a?0:1},Et=function(){function s(){this.emitInitSegment=!1,this.audioCodec=void 0,this.videoCodec=void 0,this.initData=void 0,this.initPTS=null,this.initTracks=void 0,this.lastEndTime=null}var a=s.prototype;return a.destroy=function(){},a.resetTimeStamp=function(e){this.initPTS=e,this.lastEndTime=null},a.resetNextTimestamp=function(){this.lastEndTime=null},a.resetInitSegment=function(e,t,n,i){this.audioCodec=t,this.videoCodec=n,this.generateInitSegment(zi(e,i)),this.emitInitSegment=!0},a.generateInitSegment=function(e){var t=this.audioCodec,n=this.videoCodec;if(!(e!=null&&e.byteLength)){this.initTracks=void 0,this.initData=void 0;return}var i=this.initData=fr(e);t||(t=Dt(i.audio,Ce.AUDIO)),n||(n=Dt(i.video,Ce.VIDEO));var o={};i.audio&&i.video?o.audiovideo={container:"video/mp4",codec:t+","+n,initSegment:e,id:"main"}:i.audio?o.audio={container:"audio/mp4",codec:t,initSegment:e,id:"audio"}:i.video?o.video={container:"video/mp4",codec:n,initSegment:e,id:"main"}:P.warn("[passthrough-remuxer.ts]: initSegment does not contain moov or trak boxes."),this.initTracks=o},a.remux=function(e,t,n,i,o,l){var d,u,c=this.initPTS,p=this.lastEndTime,g={audio:void 0,video:void 0,text:i,id3:n,initSegment:void 0};oe(p)||(p=this.lastEndTime=o||0);var y=t.samples;if(!(y!=null&&y.length))return g;var b={initPTS:void 0,timescale:1},E=this.initData;if((d=E)!=null&&d.length||(this.generateInitSegment(y),E=this.initData),!((u=E)!=null&&u.length))return P.warn("[passthrough-remuxer.ts]: Failed to generate initSegment."),g;this.emitInitSegment&&(b.tracks=this.initTracks,this.emitInitSegment=!1);var C=hr(E,y),w=C===null?o:C;(Rt(c,w,o)||b.timescale!==c.timescale&&l)&&(b.initPTS=w-o,this.initPTS=c={baseTime:b.initPTS,timescale:1});var k=Vi(y,E),D=e?w-c.baseTime/c.timescale:p,_=D+k;lt(E,y,c.baseTime/c.timescale),k>0?this.lastEndTime=_:(P.warn("Duration parsed from mp4 should be greater than zero"),this.resetNextTimestamp());var F=!!E.audio,O=!!E.video,M="";F&&(M+="audio"),O&&(M+="video");var z={data1:y,startPTS:D,startDTS:D,endPTS:_,endDTS:_,type:M,hasAudio:F,hasVideo:O,nb:1,dropped:0};return g.audio=z.type==="audio"?z:void 0,g.video=z.type!=="audio"?z:void 0,g.initSegment=b,g.id3=pe(n,o,c,c),i.samples.length&&(g.text=Ue(i,o,c)),g},s}();function Rt(s,a,r){if(s===null)return!0;var e=a-s.baseTime/s.timescale;return e<0&&Math.abs(e-r)>1}function Dt(s,a){var r=s==null?void 0:s.codec;return r&&r.length>4?r:r==="hvc1"||r==="hev1"?"hvc1.1.c.L120.90":r==="av01"?"av01.0.04M.08":r==="avc1"||a===Ce.VIDEO?"avc1.42e01e":"mp4a.40.5"}var Xe;try{Xe=self.performance.now.bind(self.performance)}catch(s){P.debug("Unable to use Performance API on this environment"),Xe=typeof self!="undefined"&&self.Date.now}var $n=[{demux:Oa,remux:Et},{demux:Ri,remux:$},{demux:Sn,remux:$},{demux:h,remux:$}],Ha=function(){function s(r,e,t,n,i){this.async=!1,this.observer=void 0,this.typeSupported=void 0,this.config=void 0,this.vendor=void 0,this.id=void 0,this.demuxer=void 0,this.remuxer=void 0,this.decrypter=void 0,this.probe=void 0,this.decryptionPromise=null,this.transmuxConfig=void 0,this.currentTransmuxState=void 0,this.observer=r,this.typeSupported=e,this.config=t,this.vendor=n,this.id=i}var a=s.prototype;return a.configure=function(e){this.transmuxConfig=e,this.decrypter&&this.decrypter.reset()},a.push=function(e,t,n,i){var o=this,l=n.transmuxing;l.executeStart=Xe();var d=new Uint8Array(e),u=this.currentTransmuxState,c=this.transmuxConfig;i&&(this.currentTransmuxState=i);var p=i||u,g=p.contiguous,y=p.discontinuity,b=p.trackSwitch,E=p.accurateTimeOffset,C=p.timeOffset,w=p.initSegmentChange,k=c.audioCodec,D=c.videoCodec,_=c.defaultInitPts,F=c.duration,O=c.initSegmentData,M=jo(d,t);if(M&&M.method==="AES-128"){var z=this.getDecrypter();if(z.isSync()){var H=z.softwareDecrypt(d,M.key.buffer,M.iv.buffer),G=n.part>-1;if(G&&(H=z.flush()),!H)return l.executeEnd=Xe(),qa(n);d=new Uint8Array(H)}else return this.decryptionPromise=z.webCryptoDecrypt(d,M.key.buffer,M.iv.buffer).then(function(we){var xe=o.push(we,null,n);return o.decryptionPromise=null,xe}),this.decryptionPromise}var te=this.needsProbing(y,b);if(te){var ye=this.configureTransmuxer(d);if(ye)return P.warn("[transmuxer] "+ye.message),this.observer.emit(S.ERROR,S.ERROR,{type:fe.MEDIA_ERROR,details:U.FRAG_PARSING_ERROR,fatal:!1,error:ye,reason:ye.message}),l.executeEnd=Xe(),qa(n)}(y||b||w||te)&&this.resetInitSegment(O,k,D,F,t),(y||w||te)&&this.resetInitialTimestamp(_),g||this.resetContiguity();var Ae=this.transmux(d,M,C,E,n),Ee=this.currentTransmuxState;return Ee.contiguous=!0,Ee.discontinuity=!1,Ee.trackSwitch=!1,l.executeEnd=Xe(),Ae},a.flush=function(e){var t=this,n=e.transmuxing;n.executeStart=Xe();var i=this.decrypter,o=this.currentTransmuxState,l=this.decryptionPromise;if(l)return l.then(function(){return t.flush(e)});var d=[],u=o.timeOffset;if(i){var c=i.flush();c&&d.push(this.push(c,null,e))}var p=this.demuxer,g=this.remuxer;if(!p||!g)return n.executeEnd=Xe(),[qa(e)];var y=p.flush(u);return wn(y)?y.then(function(b){return t.flushRemux(d,b,e),d}):(this.flushRemux(d,y,e),d)},a.flushRemux=function(e,t,n){var i=t.audioTrack,o=t.videoTrack,l=t.id3Track,d=t.textTrack,u=this.currentTransmuxState,c=u.accurateTimeOffset,p=u.timeOffset;P.log("[transmuxer.ts]: Flushed fragment "+n.sn+(n.part>-1?" p: "+n.part:"")+" of level "+n.level);var g=this.remuxer.remux(i,o,l,d,p,c,!0,this.id);e.push({remuxResult:g,chunkMeta:n}),n.transmuxing.executeEnd=Xe()},a.resetInitialTimestamp=function(e){var t=this.demuxer,n=this.remuxer;!t||!n||(t.resetTimeStamp(e),n.resetTimeStamp(e))},a.resetContiguity=function(){var e=this.demuxer,t=this.remuxer;!e||!t||(e.resetContiguity(),t.resetNextTimestamp())},a.resetInitSegment=function(e,t,n,i,o){var l=this.demuxer,d=this.remuxer;!l||!d||(l.resetInitSegment(e,t,n,i),d.resetInitSegment(e,t,n,o))},a.destroy=function(){this.demuxer&&(this.demuxer.destroy(),this.demuxer=void 0),this.remuxer&&(this.remuxer.destroy(),this.remuxer=void 0)},a.transmux=function(e,t,n,i,o){var l;return t&&t.method==="SAMPLE-AES"?l=this.transmuxSampleAes(e,t,n,i,o):l=this.transmuxUnencrypted(e,n,i,o),l},a.transmuxUnencrypted=function(e,t,n,i){var o=this.demuxer.demux(e,t,!1,!this.config.progressive),l=o.audioTrack,d=o.videoTrack,u=o.id3Track,c=o.textTrack,p=this.remuxer.remux(l,d,u,c,t,n,!1,this.id);return{remuxResult:p,chunkMeta:i}},a.transmuxSampleAes=function(e,t,n,i,o){var l=this;return this.demuxer.demuxSampleAes(e,t,n).then(function(d){var u=l.remuxer.remux(d.audioTrack,d.videoTrack,d.id3Track,d.textTrack,n,i,!1,l.id);return{remuxResult:u,chunkMeta:o}})},a.configureTransmuxer=function(e){for(var t=this.config,n=this.observer,i=this.typeSupported,o=this.vendor,l,d=0,u=$n.length;d<u;d++)if($n[d].demux.probe(e)){l=$n[d];break}if(!l)return new Error("Failed to find demuxer by probing fragment data");var c=this.demuxer,p=this.remuxer,g=l.remux,y=l.demux;(!p||!(p instanceof g))&&(this.remuxer=new g(n,t,i,o)),(!c||!(c instanceof y))&&(this.demuxer=new y(n,t,i),this.probe=y.probe)},a.needsProbing=function(e,t){return!this.demuxer||!this.remuxer||e||t},a.getDecrypter=function(){var e=this.decrypter;return e||(e=this.decrypter=new Pr(this.config)),e},s}();function jo(s,a){var r=null;return s.byteLength>0&&a!=null&&a.key!=null&&a.iv!==null&&a.method!=null&&(r=a),r}var qa=function(a){return{remuxResult:{},chunkMeta:a}};function wn(s){return"then"in s&&s.then instanceof Function}var Qo=function(a,r,e,t,n){this.audioCodec=void 0,this.videoCodec=void 0,this.initSegmentData=void 0,this.duration=void 0,this.defaultInitPts=void 0,this.audioCodec=a,this.videoCodec=r,this.initSegmentData=e,this.duration=t,this.defaultInitPts=n||null},Xo=function(a,r,e,t,n,i){this.discontinuity=void 0,this.contiguous=void 0,this.accurateTimeOffset=void 0,this.trackSwitch=void 0,this.timeOffset=void 0,this.initSegmentChange=void 0,this.discontinuity=a,this.contiguous=r,this.accurateTimeOffset=e,this.trackSwitch=t,this.timeOffset=n,this.initSegmentChange=i},Ya={},Zo={get exports(){return Ya},set exports(s){Ya=s}};(function(s){var a=Object.prototype.hasOwnProperty,r="~";function e(){}Object.create&&(e.prototype=Object.create(null),new e().__proto__||(r=!1));function t(l,d,u){this.fn=l,this.context=d,this.once=u||!1}function n(l,d,u,c,p){if(typeof u!="function")throw new TypeError("The listener must be a function");var g=new t(u,c||l,p),y=r?r+d:d;return l._events[y]?l._events[y].fn?l._events[y]=[l._events[y],g]:l._events[y].push(g):(l._events[y]=g,l._eventsCount++),l}function i(l,d){--l._eventsCount==0?l._events=new e:delete l._events[d]}function o(){this._events=new e,this._eventsCount=0}o.prototype.eventNames=function(){var d=[],u,c;if(this._eventsCount===0)return d;for(c in u=this._events)a.call(u,c)&&d.push(r?c.slice(1):c);return Object.getOwnPropertySymbols?d.concat(Object.getOwnPropertySymbols(u)):d},o.prototype.listeners=function(d){var u=r?r+d:d,c=this._events[u];if(!c)return[];if(c.fn)return[c.fn];for(var p=0,g=c.length,y=new Array(g);p<g;p++)y[p]=c[p].fn;return y},o.prototype.listenerCount=function(d){var u=r?r+d:d,c=this._events[u];return c?c.fn?1:c.length:0},o.prototype.emit=function(d,u,c,p,g,y){var b=r?r+d:d;if(!this._events[b])return!1;var E=this._events[b],C=arguments.length,w,k;if(E.fn){switch(E.once&&this.removeListener(d,E.fn,void 0,!0),C){case 1:return E.fn.call(E.context),!0;case 2:return E.fn.call(E.context,u),!0;case 3:return E.fn.call(E.context,u,c),!0;case 4:return E.fn.call(E.context,u,c,p),!0;case 5:return E.fn.call(E.context,u,c,p,g),!0;case 6:return E.fn.call(E.context,u,c,p,g,y),!0}for(k=1,w=new Array(C-1);k<C;k++)w[k-1]=arguments[k];E.fn.apply(E.context,w)}else{var D=E.length,_;for(k=0;k<D;k++)switch(E[k].once&&this.removeListener(d,E[k].fn,void 0,!0),C){case 1:E[k].fn.call(E[k].context);break;case 2:E[k].fn.call(E[k].context,u);break;case 3:E[k].fn.call(E[k].context,u,c);break;case 4:E[k].fn.call(E[k].context,u,c,p);break;default:if(!w)for(_=1,w=new Array(C-1);_<C;_++)w[_-1]=arguments[_];E[k].fn.apply(E[k].context,w)}}return!0},o.prototype.on=function(d,u,c){return n(this,d,u,c,!1)},o.prototype.once=function(d,u,c){return n(this,d,u,c,!0)},o.prototype.removeListener=function(d,u,c,p){var g=r?r+d:d;if(!this._events[g])return this;if(!u)return i(this,g),this;var y=this._events[g];if(y.fn)y.fn===u&&(!p||y.once)&&(!c||y.context===c)&&i(this,g);else{for(var b=0,E=[],C=y.length;b<C;b++)(y[b].fn!==u||p&&!y[b].once||c&&y[b].context!==c)&&E.push(y[b]);E.length?this._events[g]=E.length===1?E[0]:E:i(this,g)}return this},o.prototype.removeAllListeners=function(d){var u;return d?(u=r?r+d:d,this._events[u]&&i(this,u)):(this._events=new e,this._eventsCount=0),this},o.prototype.off=o.prototype.removeListener,o.prototype.addListener=o.prototype.on,o.prefixed=r,o.EventEmitter=o,s.exports=o})(Zo);var Di=Ya;typeof nr!="undefined"&&nr&&Jo(self);function Jo(s){var a=new Di,r=function(n,i){s.postMessage({event:n,data:i})};a.on(S.FRAG_DECRYPTED,r),a.on(S.ERROR,r);var e=function(){var n=function(l){var d=function(c){r("workerLog",{logType:l,message:c})};P[l]=d};for(var i in P)n(i)};s.addEventListener("message",function(t){var n=t.data;switch(n.cmd){case"init":{var i=JSON.parse(n.config);s.transmuxer=new Ha(a,n.typeSupported,i,n.vendor,n.id),Ur(i.debug,n.id),e(),r("init",null);break}case"configure":{s.transmuxer.configure(n.config);break}case"demux":{var o=s.transmuxer.push(n.data,n.decryptdata,n.chunkMeta,n.state);wn(o)?(s.transmuxer.async=!0,o.then(function(c){Wa(s,c)}).catch(function(c){r(S.ERROR,{type:fe.MEDIA_ERROR,details:U.FRAG_PARSING_ERROR,chunkMeta:n.chunkMeta,fatal:!1,error:c,err:c,reason:"transmuxer-worker push error"})})):(s.transmuxer.async=!1,Wa(s,o));break}case"flush":{var l=n.chunkMeta,d=s.transmuxer.flush(l),u=wn(d);u||s.transmuxer.async?(wn(d)||(d=Promise.resolve(d)),d.then(function(c){fo(s,c,l)}).catch(function(c){r(S.ERROR,{type:fe.MEDIA_ERROR,details:U.FRAG_PARSING_ERROR,chunkMeta:n.chunkMeta,fatal:!1,error:c,err:c,reason:"transmuxer-worker flush error"})})):fo(s,d,l);break}}})}function Wa(s,a){if($o(a.remuxResult))return!1;var r=[],e=a.remuxResult,t=e.audio,n=e.video;return t&&co(r,t),n&&co(r,n),s.postMessage({event:"transmuxComplete",data:a},r),!0}function co(s,a){a.data1&&s.push(a.data1.buffer),a.data2&&s.push(a.data2.buffer)}function fo(s,a,r){var e=a.reduce(function(t,n){return Wa(s,n)||t},!1);e||s.postMessage({event:"transmuxComplete",data:a[0]}),s.postMessage({event:"flush",data:r})}function $o(s){return!s.audio&&!s.video&&!s.text&&!s.id3&&!s.initSegment}function es(){return typeof Ui=="function"}function ts(){var s=new self.Blob(["var exports={};var module={exports:exports};function define(f){f()};define.amd=true;("+Ui.toString()+")(true);"],{type:"text/javascript"}),a=self.URL.createObjectURL(s),r=new self.Worker(a);return{worker:r,objectURL:a}}function ns(s){var a=new self.URL(s,self.location.href).href,r=new self.Worker(a);return{worker:r,scriptURL:a}}var za=It()||{isTypeSupported:function(){return!1}},ho=function(){function s(r,e,t,n){var i=this;this.error=null,this.hls=void 0,this.id=void 0,this.observer=void 0,this.frag=null,this.part=null,this.useWorker=void 0,this.workerContext=null,this.onwmsg=void 0,this.transmuxer=null,this.onTransmuxComplete=void 0,this.onFlush=void 0;var o=r.config;this.hls=r,this.id=e,this.useWorker=!!o.enableWorker,this.onTransmuxComplete=t,this.onFlush=n;var l=function(y,b){b=b||{},b.frag=i.frag,b.id=i.id,y===S.ERROR&&(i.error=b.error),i.hls.trigger(y,b)};this.observer=new Di,this.observer.on(S.FRAG_DECRYPTED,l),this.observer.on(S.ERROR,l);var d={mp4:za.isTypeSupported("video/mp4"),mpeg:za.isTypeSupported("audio/mpeg"),mp3:za.isTypeSupported('audio/mp4; codecs="mp3"')},u=navigator.vendor;if(this.useWorker&&typeof Worker!="undefined"){var c=o.workerPath||es();if(c){try{o.workerPath?(P.log("loading Web Worker "+o.workerPath+' for "'+e+'"'),this.workerContext=ns(o.workerPath)):(P.log('injecting Web Worker for "'+e+'"'),this.workerContext=ts()),this.onwmsg=function(g){return i.onWorkerMessage(g)};var p=this.workerContext.worker;p.addEventListener("message",this.onwmsg),p.onerror=function(g){var y=new Error(g.message+"  ("+g.filename+":"+g.lineno+")");o.enableWorker=!1,P.warn('Error in "'+e+'" Web Worker, fallback to inline'),i.hls.trigger(S.ERROR,{type:fe.OTHER_ERROR,details:U.INTERNAL_EXCEPTION,fatal:!1,event:"demuxerWorker",error:y})},p.postMessage({cmd:"init",typeSupported:d,vendor:u,id:e,config:JSON.stringify(o)})}catch(g){P.warn('Error setting up "'+e+'" Web Worker, fallback to inline',g),this.resetWorker(),this.error=null,this.transmuxer=new Ha(this.observer,d,o,u,e)}return}}this.transmuxer=new Ha(this.observer,d,o,u,e)}var a=s.prototype;return a.resetWorker=function(){if(this.workerContext){var e=this.workerContext,t=e.worker,n=e.objectURL;n&&self.URL.revokeObjectURL(n),t.removeEventListener("message",this.onwmsg),t.onerror=null,t.terminate(),this.workerContext=null}},a.destroy=function(){if(this.workerContext)this.resetWorker(),this.onwmsg=void 0;else{var e=this.transmuxer;e&&(e.destroy(),this.transmuxer=null)}var t=this.observer;t&&t.removeAllListeners(),this.frag=null,this.observer=null,this.hls=null},a.push=function(e,t,n,i,o,l,d,u,c,p){var g,y,b=this;c.transmuxing.start=self.performance.now();var E=this.transmuxer,C=l?l.start:o.start,w=o.decryptdata,k=this.frag,D=!(k&&o.cc===k.cc),_=!(k&&c.level===k.level),F=k?c.sn-k.sn:-1,O=this.part?c.part-this.part.index:-1,M=F===0&&c.id>1&&c.id===(k==null?void 0:k.stats.chunkCount),z=!_&&(F===1||F===0&&(O===1||M&&O<=0)),H=self.performance.now();(_||F||o.stats.parsing.start===0)&&(o.stats.parsing.start=H),l&&(O||!z)&&(l.stats.parsing.start=H);var G=!(k&&((g=o.initSegment)==null?void 0:g.url)===((y=k.initSegment)==null?void 0:y.url)),te=new Xo(D,z,u,_,C,G);if(!z||D||G){P.log("[transmuxer-interface, "+o.type+"]: Starting new transmux session for sn: "+c.sn+" p: "+c.part+" level: "+c.level+" id: "+c.id+`
        discontinuity: `+D+`
        trackSwitch: `+_+`
        contiguous: `+z+`
        accurateTimeOffset: `+u+`
        timeOffset: `+C+`
        initSegmentChange: `+G);var ye=new Qo(n,i,t,d,p);this.configureTransmuxer(ye)}if(this.frag=o,this.part=l,this.workerContext)this.workerContext.worker.postMessage({cmd:"demux",data:e,decryptdata:w,chunkMeta:c,state:te},e instanceof ArrayBuffer?[e]:[]);else if(E){var Ae=E.push(e,w,c,te);wn(Ae)?(E.async=!0,Ae.then(function(Ee){b.handleTransmuxComplete(Ee)}).catch(function(Ee){b.transmuxerError(Ee,c,"transmuxer-interface push error")})):(E.async=!1,this.handleTransmuxComplete(Ae))}},a.flush=function(e){var t=this;e.transmuxing.start=self.performance.now();var n=this.transmuxer;if(this.workerContext)this.workerContext.worker.postMessage({cmd:"flush",chunkMeta:e});else if(n){var i=n.flush(e),o=wn(i);o||n.async?(wn(i)||(i=Promise.resolve(i)),i.then(function(l){t.handleFlushResult(l,e)}).catch(function(l){t.transmuxerError(l,e,"transmuxer-interface flush error")})):this.handleFlushResult(i,e)}},a.transmuxerError=function(e,t,n){!this.hls||(this.error=e,this.hls.trigger(S.ERROR,{type:fe.MEDIA_ERROR,details:U.FRAG_PARSING_ERROR,chunkMeta:t,fatal:!1,error:e,err:e,reason:n}))},a.handleFlushResult=function(e,t){var n=this;e.forEach(function(i){n.handleTransmuxComplete(i)}),this.onFlush(t)},a.onWorkerMessage=function(e){var t=e.data,n=this.hls;switch(t.event){case"init":{var i,o=(i=this.workerContext)==null?void 0:i.objectURL;o&&self.URL.revokeObjectURL(o);break}case"transmuxComplete":{this.handleTransmuxComplete(t.data);break}case"flush":{this.onFlush(t.data);break}case"workerLog":P[t.data.logType]&&P[t.data.logType](t.data.message);break;default:{t.data=t.data||{},t.data.frag=this.frag,t.data.id=this.id,n.trigger(t.event,t.data);break}}},a.configureTransmuxer=function(e){var t=this.transmuxer;this.workerContext?this.workerContext.worker.postMessage({cmd:"configure",config:e}):t&&t.configure(e)},a.handleTransmuxComplete=function(e){e.chunkMeta.transmuxing.end=self.performance.now(),this.onTransmuxComplete(e)},s}(),rs=250,Va=2,is=.1,as=.05,os=function(){function s(r,e,t,n){this.config=void 0,this.media=null,this.fragmentTracker=void 0,this.hls=void 0,this.nudgeRetry=0,this.stallReported=!1,this.stalled=null,this.moved=!1,this.seeking=!1,this.config=r,this.media=e,this.fragmentTracker=t,this.hls=n}var a=s.prototype;return a.destroy=function(){this.media=null,this.hls=this.fragmentTracker=null},a.poll=function(e,t){var n=this.config,i=this.media,o=this.stalled;if(i!==null){var l=i.currentTime,d=i.seeking,u=this.seeking&&!d,c=!this.seeking&&d;if(this.seeking=d,l!==e){if(this.moved=!0,o!==null){if(this.stallReported){var p=self.performance.now()-o;P.warn("playback not stuck anymore @"+l+", after "+Math.round(p)+"ms"),this.stallReported=!1}this.stalled=null,this.nudgeRetry=0}return}if(c||u){this.stalled=null;return}if(!(i.paused&&!d||i.ended||i.playbackRate===0||!Fe.getBuffered(i).length)){var g=Fe.bufferInfo(i,l,0),y=g.len>0,b=g.nextStart||0;if(!(!y&&!b)){if(d){var E=g.len>Va,C=!b||t&&t.start<=l||b-l>Va&&!this.fragmentTracker.getPartialFragment(l);if(E||C)return;this.moved=!1}if(!this.moved&&this.stalled!==null){var w,k=Math.max(b,g.start||0)-l,D=this.hls.levels?this.hls.levels[this.hls.currentLevel]:null,_=D==null||(w=D.details)==null?void 0:w.live,F=_?D.details.targetduration*2:Va,O=this.fragmentTracker.getPartialFragment(l);if(k>0&&(k<=F||O)){this._trySkipBufferHole(O);return}}var M=self.performance.now();if(o===null){this.stalled=M;return}var z=M-o;if(!(!d&&z>=rs&&(this._reportStall(g),!this.media))){var H=Fe.bufferInfo(i,l,n.maxBufferHole);this._tryFixBufferStall(H,z)}}}}},a._tryFixBufferStall=function(e,t){var n=this.config,i=this.fragmentTracker,o=this.media;if(o!==null){var l=o.currentTime,d=i.getPartialFragment(l);if(d){var u=this._trySkipBufferHole(d);if(u||!this.media)return}(e.len>n.maxBufferHole||e.nextStart&&e.nextStart-l<n.maxBufferHole)&&t>n.highBufferWatchdogPeriod*1e3&&(P.warn("Trying to nudge playhead over buffer-hole"),this.stalled=null,this._tryNudgeBuffer())}},a._reportStall=function(e){var t=this.hls,n=this.media,i=this.stallReported;if(!i&&n){this.stallReported=!0;var o=new Error("Playback stalling at @"+n.currentTime+" due to low buffer ("+JSON.stringify(e)+")");P.warn(o.message),t.trigger(S.ERROR,{type:fe.MEDIA_ERROR,details:U.BUFFER_STALLED_ERROR,fatal:!1,error:o,buffer:e.len})}},a._trySkipBufferHole=function(e){var t=this.config,n=this.hls,i=this.media;if(i===null)return 0;var o=i.currentTime,l=Fe.bufferInfo(i,o,0),d=o<l.start?l.start:l.nextStart;if(d){var u=l.len<=t.maxBufferHole,c=l.len>0&&l.len<1&&i.readyState<3,p=d-o;if(p>0&&(u||c)){if(p>t.maxBufferHole){var g=this.fragmentTracker,y=!1;if(o===0){var b=g.getAppendedFrag(0,he.MAIN);b&&d<b.end&&(y=!0)}if(!y){var E=e||g.getAppendedFrag(o,he.MAIN);if(E){for(var C=!1,w=E.end;w<d;){var k=g.getPartialFragment(w);if(k)w+=k.duration;else{C=!0;break}}if(C)return 0}}}var D=Math.max(d+as,o+is);if(P.warn("skipping hole, adjusting currentTime from "+o+" to "+D),this.moved=!0,this.stalled=null,i.currentTime=D,e&&!e.gap){var _=new Error("fragment loaded with buffer holes, seeking from "+o+" to "+D);n.trigger(S.ERROR,{type:fe.MEDIA_ERROR,details:U.BUFFER_SEEK_OVER_HOLE,fatal:!1,error:_,reason:_.message,frag:e})}return D}}return 0},a._tryNudgeBuffer=function(){var e=this.config,t=this.hls,n=this.media,i=this.nudgeRetry;if(n!==null){var o=n.currentTime;if(this.nudgeRetry++,i<e.nudgeMaxRetry){var l=o+(i+1)*e.nudgeOffset,d=new Error("Nudging 'currentTime' from "+o+" to "+l);P.warn(d.message),n.currentTime=l,t.trigger(S.ERROR,{type:fe.MEDIA_ERROR,details:U.BUFFER_NUDGE_ON_STALL,error:d,fatal:!1})}else{var u=new Error("Playhead still not moving while enough data buffered @"+o+" after "+e.nudgeMaxRetry+" nudges");P.error(u.message),t.trigger(S.ERROR,{type:fe.MEDIA_ERROR,details:U.BUFFER_STALLED_ERROR,error:u,fatal:!0})}}},s}(),ss=100,ls=function(s){j(a,s);function a(e,t,n){var i;return i=s.call(this,e,t,n,"[stream-controller]",he.MAIN)||this,i.audioCodecSwap=!1,i.gapController=null,i.level=-1,i._forceStartLoad=!1,i.altAudio=!1,i.audioOnly=!1,i.fragPlaying=null,i.onvplaying=null,i.onvseeked=null,i.fragLastKbps=0,i.couldBacktrack=!1,i.backtrackFragment=null,i.audioCodecSwitch=!1,i.videoBuffer=null,i._registerListeners(),i}var r=a.prototype;return r._registerListeners=function(){var t=this.hls;t.on(S.MEDIA_ATTACHED,this.onMediaAttached,this),t.on(S.MEDIA_DETACHING,this.onMediaDetaching,this),t.on(S.MANIFEST_LOADING,this.onManifestLoading,this),t.on(S.MANIFEST_PARSED,this.onManifestParsed,this),t.on(S.LEVEL_LOADING,this.onLevelLoading,this),t.on(S.LEVEL_LOADED,this.onLevelLoaded,this),t.on(S.FRAG_LOAD_EMERGENCY_ABORTED,this.onFragLoadEmergencyAborted,this),t.on(S.ERROR,this.onError,this),t.on(S.AUDIO_TRACK_SWITCHING,this.onAudioTrackSwitching,this),t.on(S.AUDIO_TRACK_SWITCHED,this.onAudioTrackSwitched,this),t.on(S.BUFFER_CREATED,this.onBufferCreated,this),t.on(S.BUFFER_FLUSHED,this.onBufferFlushed,this),t.on(S.LEVELS_UPDATED,this.onLevelsUpdated,this),t.on(S.FRAG_BUFFERED,this.onFragBuffered,this)},r._unregisterListeners=function(){var t=this.hls;t.off(S.MEDIA_ATTACHED,this.onMediaAttached,this),t.off(S.MEDIA_DETACHING,this.onMediaDetaching,this),t.off(S.MANIFEST_LOADING,this.onManifestLoading,this),t.off(S.MANIFEST_PARSED,this.onManifestParsed,this),t.off(S.LEVEL_LOADED,this.onLevelLoaded,this),t.off(S.FRAG_LOAD_EMERGENCY_ABORTED,this.onFragLoadEmergencyAborted,this),t.off(S.ERROR,this.onError,this),t.off(S.AUDIO_TRACK_SWITCHING,this.onAudioTrackSwitching,this),t.off(S.AUDIO_TRACK_SWITCHED,this.onAudioTrackSwitched,this),t.off(S.BUFFER_CREATED,this.onBufferCreated,this),t.off(S.BUFFER_FLUSHED,this.onBufferFlushed,this),t.off(S.LEVELS_UPDATED,this.onLevelsUpdated,this),t.off(S.FRAG_BUFFERED,this.onFragBuffered,this)},r.onHandlerDestroying=function(){this._unregisterListeners(),this.onMediaDetaching()},r.startLoad=function(t){if(this.levels){var n=this.lastCurrentTime,i=this.hls;if(this.stopLoad(),this.setInterval(ss),this.level=-1,!this.startFragRequested){var o=i.startLevel;o===-1&&(i.config.testBandwidth&&this.levels.length>1?(o=0,this.bitrateTest=!0):o=i.nextAutoLevel),this.level=i.nextLoadLevel=o,this.loadedmetadata=!1}n>0&&t===-1&&(this.log("Override startPosition with lastCurrentTime @"+n.toFixed(3)),t=n),this.state=Y.IDLE,this.nextLoadPosition=this.startPosition=this.lastCurrentTime=t,this.tick()}else this._forceStartLoad=!0,this.state=Y.STOPPED},r.stopLoad=function(){this._forceStartLoad=!1,s.prototype.stopLoad.call(this)},r.doTick=function(){switch(this.state){case Y.WAITING_LEVEL:{var t,n=this.levels,i=this.level,o=n==null||(t=n[i])==null?void 0:t.details;if(o&&(!o.live||this.levelLastLoaded===this.level)){if(this.waitForCdnTuneIn(o))break;this.state=Y.IDLE;break}break}case Y.FRAG_LOADING_WAITING_RETRY:{var l,d=self.performance.now(),u=this.retryDate;(!u||d>=u||(l=this.media)!=null&&l.seeking)&&(this.resetStartWhenNotLoaded(this.level),this.state=Y.IDLE)}break}this.state===Y.IDLE&&this.doTickIdle(),this.onTickEnd()},r.onTickEnd=function(){s.prototype.onTickEnd.call(this),this.checkBuffer(),this.checkFragmentChanged()},r.doTickIdle=function(){var t=this.hls,n=this.levelLastLoaded,i=this.levels,o=this.media,l=t.config,d=t.nextLoadLevel;if(!(n===null||!o&&(this.startFragRequested||!l.startFragPrefetch))&&!(this.altAudio&&this.audioOnly)&&!!(i!=null&&i[d])){var u=i[d],c=this.getMainFwdBufferInfo();if(c!==null){var p=this.getLevelDetails();if(p&&this._streamEnded(c,p)){var g={};this.altAudio&&(g.type="video"),this.hls.trigger(S.BUFFER_EOS,g),this.state=Y.ENDED;return}t.loadLevel!==d&&t.manualLevel===-1&&this.log("Adapting to level "+d+" from level "+this.level),this.level=t.nextLoadLevel=d;var y=u.details;if(!y||this.state===Y.WAITING_LEVEL||y.live&&this.levelLastLoaded!==d){this.level=d,this.state=Y.WAITING_LEVEL;return}var b=c.len,E=this.getMaxBufferLength(u.maxBitrate);if(!(b>=E)){this.backtrackFragment&&this.backtrackFragment.start>c.end&&(this.backtrackFragment=null);var C=this.backtrackFragment?this.backtrackFragment.start:c.end,w=this.getNextFragment(C,y);if(this.couldBacktrack&&!this.fragPrevious&&w&&w.sn!=="initSegment"&&this.fragmentTracker.getState(w)!==Ye.OK){var k,D=((k=this.backtrackFragment)!=null?k:w).sn,_=D-y.startSN,F=y.fragments[_-1];F&&w.cc===F.cc&&(w=F,this.fragmentTracker.removeFragment(F))}else this.backtrackFragment&&c.len&&(this.backtrackFragment=null);if(w&&this.isLoopLoading(w,C)){var O=w.gap;if(!O){var M=this.audioOnly&&!this.altAudio?Ce.AUDIO:Ce.VIDEO,z=(M===Ce.VIDEO?this.videoBuffer:this.mediaBuffer)||this.media;z&&this.afterBufferFlushed(z,M,he.MAIN)}w=this.getNextFragmentLoopLoading(w,y,c,he.MAIN,E)}!w||(w.initSegment&&!w.initSegment.data&&!this.bitrateTest&&(w=w.initSegment),this.loadFragment(w,u,C))}}}},r.loadFragment=function(t,n,i){var o=this.fragmentTracker.getState(t);this.fragCurrent=t,o===Ye.NOT_LOADED?t.sn==="initSegment"?this._loadInitSegment(t,n):this.bitrateTest?(this.log("Fragment "+t.sn+" of level "+t.level+" is being downloaded to test bitrate and will not be buffered"),this._loadBitrateTestFrag(t,n)):(this.startFragRequested=!0,s.prototype.loadFragment.call(this,t,n,i)):this.clearTrackerIfNeeded(t)},r.getAppendedFrag=function(t){var n=this.fragmentTracker.getAppendedFrag(t,he.MAIN);return n&&"fragment"in n?n.fragment:n},r.getBufferedFrag=function(t){return this.fragmentTracker.getBufferedFrag(t,he.MAIN)},r.followingBufferedFrag=function(t){return t?this.getBufferedFrag(t.end+.5):null},r.immediateLevelSwitch=function(){this.abortCurrentFrag(),this.flushMainBuffer(0,Number.POSITIVE_INFINITY)},r.nextLevelSwitch=function(){var t=this.levels,n=this.media;if(n!=null&&n.readyState){var i,o=this.getAppendedFrag(n.currentTime);if(o&&o.start>1&&this.flushMainBuffer(0,o.start-1),!n.paused&&t){var l=this.hls.nextLoadLevel,d=t[l],u=this.fragLastKbps;u&&this.fragCurrent?i=this.fragCurrent.duration*d.maxBitrate/(1e3*u)+1:i=0}else i=0;var c=this.getBufferedFrag(n.currentTime+i);if(c){var p=this.followingBufferedFrag(c);if(p){this.abortCurrentFrag();var g=p.maxStartPTS?p.maxStartPTS:p.start,y=p.duration,b=Math.max(c.end,g+Math.min(Math.max(y-this.config.maxFragLookUpTolerance,y*.5),y*.75));this.flushMainBuffer(b,Number.POSITIVE_INFINITY)}}}},r.abortCurrentFrag=function(){var t=this.fragCurrent;switch(this.fragCurrent=null,this.backtrackFragment=null,t&&(t.abortRequests(),this.fragmentTracker.removeFragment(t)),this.state){case Y.KEY_LOADING:case Y.FRAG_LOADING:case Y.FRAG_LOADING_WAITING_RETRY:case Y.PARSING:case Y.PARSED:this.state=Y.IDLE;break}this.nextLoadPosition=this.getLoadPosition()},r.flushMainBuffer=function(t,n){s.prototype.flushMainBuffer.call(this,t,n,this.altAudio?"video":null)},r.onMediaAttached=function(t,n){s.prototype.onMediaAttached.call(this,t,n);var i=n.media;this.onvplaying=this.onMediaPlaying.bind(this),this.onvseeked=this.onMediaSeeked.bind(this),i.addEventListener("playing",this.onvplaying),i.addEventListener("seeked",this.onvseeked),this.gapController=new os(this.config,i,this.fragmentTracker,this.hls)},r.onMediaDetaching=function(){var t=this.media;t&&this.onvplaying&&this.onvseeked&&(t.removeEventListener("playing",this.onvplaying),t.removeEventListener("seeked",this.onvseeked),this.onvplaying=this.onvseeked=null,this.videoBuffer=null),this.fragPlaying=null,this.gapController&&(this.gapController.destroy(),this.gapController=null),s.prototype.onMediaDetaching.call(this)},r.onMediaPlaying=function(){this.tick()},r.onMediaSeeked=function(){var t=this.media,n=t?t.currentTime:null;oe(n)&&this.log("Media seeked to "+n.toFixed(3));var i=this.getMainFwdBufferInfo();if(i===null||i.len===0){this.warn('Main forward buffer length on "seeked" event '+(i?i.len:"empty")+")");return}this.tick()},r.onManifestLoading=function(){this.log("Trigger BUFFER_RESET"),this.hls.trigger(S.BUFFER_RESET,void 0),this.fragmentTracker.removeAllFragments(),this.couldBacktrack=!1,this.startPosition=this.lastCurrentTime=0,this.fragPlaying=null,this.backtrackFragment=null},r.onManifestParsed=function(t,n){var i=!1,o=!1,l;n.levels.forEach(function(d){l=d.audioCodec,l&&(l.indexOf("mp4a.40.2")!==-1&&(i=!0),l.indexOf("mp4a.40.5")!==-1&&(o=!0))}),this.audioCodecSwitch=i&&o&&!Ra(),this.audioCodecSwitch&&this.log("Both AAC/HE-AAC audio found in levels; declaring level codec as HE-AAC"),this.levels=n.levels,this.startFragRequested=!1},r.onLevelLoading=function(t,n){var i=this.levels;if(!(!i||this.state!==Y.IDLE)){var o=i[n.level];(!o.details||o.details.live&&this.levelLastLoaded!==n.level||this.waitForCdnTuneIn(o.details))&&(this.state=Y.WAITING_LEVEL)}},r.onLevelLoaded=function(t,n){var i,o=this.levels,l=n.level,d=n.details,u=d.totalduration;if(!o){this.warn("Levels were reset while loading level "+l);return}this.log("Level "+l+" loaded ["+d.startSN+","+d.endSN+"], cc ["+d.startCC+", "+d.endCC+"] duration:"+u);var c=o[l],p=this.fragCurrent;p&&(this.state===Y.FRAG_LOADING||this.state===Y.FRAG_LOADING_WAITING_RETRY)&&(p.level!==n.level||p.urlId!==c.urlId)&&p.loader&&this.abortCurrentFrag();var g=0;if(d.live||(i=c.details)!=null&&i.live){if(d.fragments[0]||(d.deltaUpdateFailed=!0),d.deltaUpdateFailed)return;g=this.alignPlaylists(d,c.details)}if(c.details=d,this.levelLastLoaded=l,this.hls.trigger(S.LEVEL_UPDATED,{details:d,level:l}),this.state===Y.WAITING_LEVEL){if(this.waitForCdnTuneIn(d))return;this.state=Y.IDLE}this.startFragRequested?d.live&&this.synchronizeToLiveEdge(d):this.setStartPosition(d,g),this.tick()},r._handleFragmentLoadProgress=function(t){var n,i=t.frag,o=t.part,l=t.payload,d=this.levels;if(!d){this.warn("Levels were reset while fragment load was in progress. Fragment "+i.sn+" of level "+i.level+" will not be buffered");return}var u=d[i.level],c=u.details;if(!c){this.warn("Dropping fragment "+i.sn+" of level "+i.level+" after level details were reset"),this.fragmentTracker.removeFragment(i);return}var p=u.videoCodec,g=c.PTSKnown||!c.live,y=(n=i.initSegment)==null?void 0:n.data,b=this._getAudioCodec(u),E=this.transmuxer=this.transmuxer||new ho(this.hls,he.MAIN,this._handleTransmuxComplete.bind(this),this._handleTransmuxerFlush.bind(this)),C=o?o.index:-1,w=C!==-1,k=new Dr(i.level,i.sn,i.stats.chunkCount,l.byteLength,C,w),D=this.initPTS[i.cc];E.push(l,y,b,p,i,o,c.totalduration,g,k,D)},r.onAudioTrackSwitching=function(t,n){var i=this.altAudio,o=!!n.url;if(!o){if(this.mediaBuffer!==this.media){this.log("Switching on main audio, use media.buffered to schedule main fragment loading"),this.mediaBuffer=this.media;var l=this.fragCurrent;l&&(this.log("Switching to main audio track, cancel main fragment load"),l.abortRequests(),this.fragmentTracker.removeFragment(l)),this.resetTransmuxer(),this.resetLoadingState()}else this.audioOnly&&this.resetTransmuxer();var d=this.hls;i&&(d.trigger(S.BUFFER_FLUSHING,{startOffset:0,endOffset:Number.POSITIVE_INFINITY,type:null}),this.fragmentTracker.removeAllFragments()),d.trigger(S.AUDIO_TRACK_SWITCHED,n)}},r.onAudioTrackSwitched=function(t,n){var i=n.id,o=!!this.hls.audioTracks[i].url;if(o){var l=this.videoBuffer;l&&this.mediaBuffer!==l&&(this.log("Switching on alternate audio, use video.buffered to schedule main fragment loading"),this.mediaBuffer=l)}this.altAudio=o,this.tick()},r.onBufferCreated=function(t,n){var i=n.tracks,o,l,d=!1;for(var u in i){var c=i[u];if(c.id==="main"){if(l=u,o=c,u==="video"){var p=i[u];p&&(this.videoBuffer=p.buffer)}}else d=!0}d&&o?(this.log("Alternate track found, use "+l+".buffered to schedule main fragment loading"),this.mediaBuffer=o.buffer):this.mediaBuffer=this.media},r.onFragBuffered=function(t,n){var i=n.frag,o=n.part;if(!(i&&i.type!==he.MAIN)){if(this.fragContextChanged(i)){this.warn("Fragment "+i.sn+(o?" p: "+o.index:"")+" of level "+i.level+" finished buffering, but was aborted. state: "+this.state),this.state===Y.PARSED&&(this.state=Y.IDLE);return}var l=o?o.stats:i.stats;this.fragLastKbps=Math.round(8*l.total/(l.buffering.end-l.loading.first)),i.sn!=="initSegment"&&(this.fragPrevious=i),this.fragBufferedComplete(i,o)}},r.onError=function(t,n){var i;if(n.fatal){this.state=Y.ERROR;return}switch(n.details){case U.FRAG_GAP:case U.FRAG_PARSING_ERROR:case U.FRAG_DECRYPT_ERROR:case U.FRAG_LOAD_ERROR:case U.FRAG_LOAD_TIMEOUT:case U.KEY_LOAD_ERROR:case U.KEY_LOAD_TIMEOUT:this.onFragmentOrKeyLoadError(he.MAIN,n);break;case U.LEVEL_LOAD_ERROR:case U.LEVEL_LOAD_TIMEOUT:case U.LEVEL_PARSING_ERROR:!n.levelRetry&&this.state===Y.WAITING_LEVEL&&((i=n.context)==null?void 0:i.type)===be.LEVEL&&(this.state=Y.IDLE);break;case U.BUFFER_FULL_ERROR:if(!n.parent||n.parent!=="main")return;this.reduceLengthAndFlushBuffer(n)&&this.flushMainBuffer(0,Number.POSITIVE_INFINITY);break;case U.INTERNAL_EXCEPTION:this.recoverWorkerError(n);break}},r.checkBuffer=function(){var t=this.media,n=this.gapController;if(!(!t||!n||!t.readyState)){if(this.loadedmetadata||!Fe.getBuffered(t).length){var i=this.state!==Y.IDLE?this.fragCurrent:null;n.poll(this.lastCurrentTime,i)}this.lastCurrentTime=t.currentTime}},r.onFragLoadEmergencyAborted=function(){this.state=Y.IDLE,this.loadedmetadata||(this.startFragRequested=!1,this.nextLoadPosition=this.startPosition),this.tickImmediate()},r.onBufferFlushed=function(t,n){var i=n.type;if(i!==Ce.AUDIO||this.audioOnly&&!this.altAudio){var o=(i===Ce.VIDEO?this.videoBuffer:this.mediaBuffer)||this.media;this.afterBufferFlushed(o,i,he.MAIN)}},r.onLevelsUpdated=function(t,n){this.levels=n.levels},r.swapAudioCodec=function(){this.audioCodecSwap=!this.audioCodecSwap},r.seekToStartPos=function(){var t=this.media;if(!!t){var n=t.currentTime,i=this.startPosition;if(i>=0&&n<i){if(t.seeking){this.log("could not seek to "+i+", already seeking at "+n);return}var o=Fe.getBuffered(t),l=o.length?o.start(0):0,d=l-i;d>0&&(d<this.config.maxBufferHole||d<this.config.maxFragLookUpTolerance)&&(this.log("adjusting start position by "+d+" to match buffer start"),i+=d,this.startPosition=i),this.log("seek to target start position "+i+" from current time "+n),t.currentTime=i}}},r._getAudioCodec=function(t){var n=this.config.defaultAudioCodec||t.audioCodec;return this.audioCodecSwap&&n&&(this.log("Swapping audio codec"),n.indexOf("mp4a.40.5")!==-1?n="mp4a.40.2":n="mp4a.40.5"),n},r._loadBitrateTestFrag=function(t,n){var i=this;t.bitrateTest=!0,this._doFragLoad(t,n).then(function(o){var l=i.hls;if(!(!o||i.fragContextChanged(t))){n.fragmentError=0,i.state=Y.IDLE,i.startFragRequested=!1,i.bitrateTest=!1;var d=t.stats;d.parsing.start=d.parsing.end=d.buffering.start=d.buffering.end=self.performance.now(),l.trigger(S.FRAG_LOADED,o),t.bitrateTest=!1}})},r._handleTransmuxComplete=function(t){var n,i="main",o=this.hls,l=t.remuxResult,d=t.chunkMeta,u=this.getCurrentContext(d);if(!u){this.resetWhenMissingContext(d);return}var c=u.frag,p=u.part,g=u.level,y=l.video,b=l.text,E=l.id3,C=l.initSegment,w=g.details,k=this.altAudio?void 0:l.audio;if(this.fragContextChanged(c)){this.fragmentTracker.removeFragment(c);return}if(this.state=Y.PARSING,C){C.tracks&&(this._bufferInitSegment(g,C.tracks,c,d),o.trigger(S.FRAG_PARSING_INIT_SEGMENT,{frag:c,id:i,tracks:C.tracks}));var D=C.initPTS,_=C.timescale;oe(D)&&(this.initPTS[c.cc]={baseTime:D,timescale:_},o.trigger(S.INIT_PTS_FOUND,{frag:c,id:i,initPTS:D,timescale:_}))}if(y&&l.independent!==!1){if(w){var F=y.startPTS,O=y.endPTS,M=y.startDTS,z=y.endDTS;if(p)p.elementaryStreams[y.type]={startPTS:F,endPTS:O,startDTS:M,endDTS:z};else if(y.firstKeyFrame&&y.independent&&d.id===1&&(this.couldBacktrack=!0),y.dropped&&y.independent){var H=this.getMainFwdBufferInfo(),G=(H?H.end:this.getLoadPosition())+this.config.maxBufferHole,te=y.firstKeyFramePTS?y.firstKeyFramePTS:F;if(G<te-this.config.maxBufferHole){this.backtrack(c);return}c.setElementaryStreamInfo(y.type,c.start,O,c.start,z,!0)}c.setElementaryStreamInfo(y.type,F,O,M,z),this.backtrackFragment&&(this.backtrackFragment=c),this.bufferFragmentData(y,c,p,d)}}else if(l.independent===!1){this.backtrack(c);return}if(k){var ye=k.startPTS,Ae=k.endPTS,Ee=k.startDTS,we=k.endDTS;p&&(p.elementaryStreams[Ce.AUDIO]={startPTS:ye,endPTS:Ae,startDTS:Ee,endDTS:we}),c.setElementaryStreamInfo(Ce.AUDIO,ye,Ae,Ee,we),this.bufferFragmentData(k,c,p,d)}if(w&&E!=null&&(n=E.samples)!=null&&n.length){var xe={id:i,frag:c,details:w,samples:E.samples};o.trigger(S.FRAG_PARSING_METADATA,xe)}if(w&&b){var _e={id:i,frag:c,details:w,samples:b.samples};o.trigger(S.FRAG_PARSING_USERDATA,_e)}},r._bufferInitSegment=function(t,n,i,o){var l=this;if(this.state===Y.PARSING){this.audioOnly=!!n.audio&&!n.video,this.altAudio&&!this.audioOnly&&delete n.audio;var d=n.audio,u=n.video,c=n.audiovideo;if(d){var p=t.audioCodec,g=navigator.userAgent.toLowerCase();this.audioCodecSwitch&&(p&&(p.indexOf("mp4a.40.5")!==-1?p="mp4a.40.2":p="mp4a.40.5"),d.metadata.channelCount!==1&&g.indexOf("firefox")===-1&&(p="mp4a.40.5")),g.indexOf("android")!==-1&&d.container!=="audio/mpeg"&&(p="mp4a.40.2",this.log("Android: force audio codec to "+p)),t.audioCodec&&t.audioCodec!==p&&this.log('Swapping manifest audio codec "'+t.audioCodec+'" for "'+p+'"'),d.levelCodec=p,d.id="main",this.log("Init audio buffer, container:"+d.container+", codecs[selected/level/parsed]=["+(p||"")+"/"+(t.audioCodec||"")+"/"+d.codec+"]")}u&&(u.levelCodec=t.videoCodec,u.id="main",this.log("Init video buffer, container:"+u.container+", codecs[level/parsed]=["+(t.videoCodec||"")+"/"+u.codec+"]")),c&&this.log("Init audiovideo buffer, container:"+c.container+", codecs[level/parsed]=["+(t.attrs.CODECS||"")+"/"+c.codec+"]"),this.hls.trigger(S.BUFFER_CODECS,n),Object.keys(n).forEach(function(y){var b=n[y],E=b.initSegment;E!=null&&E.byteLength&&l.hls.trigger(S.BUFFER_APPENDING,{type:y,data:E,frag:i,part:null,chunkMeta:o,parent:i.type})}),this.tick()}},r.getMainFwdBufferInfo=function(){return this.getFwdBufferInfo(this.mediaBuffer?this.mediaBuffer:this.media,he.MAIN)},r.backtrack=function(t){this.couldBacktrack=!0,this.backtrackFragment=t,this.resetTransmuxer(),this.flushBufferGap(t),this.fragmentTracker.removeFragment(t),this.fragPrevious=null,this.nextLoadPosition=t.start,this.state=Y.IDLE},r.checkFragmentChanged=function(){var t=this.media,n=null;if(t&&t.readyState>1&&t.seeking===!1){var i=t.currentTime;if(Fe.isBuffered(t,i)?n=this.getAppendedFrag(i):Fe.isBuffered(t,i+.1)&&(n=this.getAppendedFrag(i+.1)),n){this.backtrackFragment=null;var o=this.fragPlaying,l=n.level;(!o||n.sn!==o.sn||o.level!==l||n.urlId!==o.urlId)&&(this.fragPlaying=n,this.hls.trigger(S.FRAG_CHANGED,{frag:n}),(!o||o.level!==l)&&this.hls.trigger(S.LEVEL_SWITCHED,{level:l}))}}},Q(a,[{key:"nextLevel",get:function(){var t=this.nextBufferedFrag;return t?t.level:-1}},{key:"currentFrag",get:function(){var t=this.media;return t?this.fragPlaying||this.getAppendedFrag(t.currentTime):null}},{key:"currentProgramDateTime",get:function(){var t=this.media;if(t){var n=t.currentTime,i=this.currentFrag;if(i&&oe(n)&&oe(i.programDateTime)){var o=i.programDateTime+(n-i.start)*1e3;return new Date(o)}}return null}},{key:"currentLevel",get:function(){var t=this.currentFrag;return t?t.level:-1}},{key:"nextBufferedFrag",get:function(){var t=this.currentFrag;return t?this.followingBufferedFrag(t):null}},{key:"forceStartLoad",get:function(){return this._forceStartLoad}}]),a}(Br),er=function(){function s(r,e,t){e===void 0&&(e=0),t===void 0&&(t=0),this.halfLife=void 0,this.alpha_=void 0,this.estimate_=void 0,this.totalWeight_=void 0,this.halfLife=r,this.alpha_=r?Math.exp(Math.log(.5)/r):0,this.estimate_=e,this.totalWeight_=t}var a=s.prototype;return a.sample=function(e,t){var n=Math.pow(this.alpha_,e);this.estimate_=t*(1-n)+n*this.estimate_,this.totalWeight_+=e},a.getTotalWeight=function(){return this.totalWeight_},a.getEstimate=function(){if(this.alpha_){var e=1-Math.pow(this.alpha_,this.totalWeight_);if(e)return this.estimate_/e}return this.estimate_},s}(),ds=function(){function s(r,e,t,n){n===void 0&&(n=100),this.defaultEstimate_=void 0,this.minWeight_=void 0,this.minDelayMs_=void 0,this.slow_=void 0,this.fast_=void 0,this.defaultTTFB_=void 0,this.ttfb_=void 0,this.defaultEstimate_=t,this.minWeight_=.001,this.minDelayMs_=50,this.slow_=new er(r),this.fast_=new er(e),this.defaultTTFB_=n,this.ttfb_=new er(r)}var a=s.prototype;return a.update=function(e,t){var n=this.slow_,i=this.fast_,o=this.ttfb_;n.halfLife!==e&&(this.slow_=new er(e,n.getEstimate(),n.getTotalWeight())),i.halfLife!==t&&(this.fast_=new er(t,i.getEstimate(),i.getTotalWeight())),o.halfLife!==e&&(this.ttfb_=new er(e,o.getEstimate(),o.getTotalWeight()))},a.sample=function(e,t){e=Math.max(e,this.minDelayMs_);var n=8*t,i=e/1e3,o=n/i;this.fast_.sample(i,o),this.slow_.sample(i,o)},a.sampleTTFB=function(e){var t=e/1e3,n=Math.sqrt(2)*Math.exp(-Math.pow(t,2)/2);this.ttfb_.sample(n,Math.max(e,5))},a.canEstimate=function(){return this.fast_.getTotalWeight()>=this.minWeight_},a.getEstimate=function(){return this.canEstimate()?Math.min(this.fast_.getEstimate(),this.slow_.getEstimate()):this.defaultEstimate_},a.getEstimateTTFB=function(){return this.ttfb_.getTotalWeight()>=this.minWeight_?this.ttfb_.getEstimate():this.defaultTTFB_},a.destroy=function(){},s}(),us=function(){function s(r){this.hls=void 0,this.lastLevelLoadSec=0,this.lastLoadedFragLevel=0,this._nextAutoLevel=-1,this.timer=-1,this.onCheck=this._abandonRulesCheck.bind(this),this.fragCurrent=null,this.partCurrent=null,this.bitrateTestDelay=0,this.bwEstimator=void 0,this.hls=r;var e=r.config;this.bwEstimator=new ds(e.abrEwmaSlowVoD,e.abrEwmaFastVoD,e.abrEwmaDefaultEstimate),this.registerListeners()}var a=s.prototype;return a.registerListeners=function(){var e=this.hls;e.on(S.FRAG_LOADING,this.onFragLoading,this),e.on(S.FRAG_LOADED,this.onFragLoaded,this),e.on(S.FRAG_BUFFERED,this.onFragBuffered,this),e.on(S.LEVEL_SWITCHING,this.onLevelSwitching,this),e.on(S.LEVEL_LOADED,this.onLevelLoaded,this)},a.unregisterListeners=function(){var e=this.hls;e.off(S.FRAG_LOADING,this.onFragLoading,this),e.off(S.FRAG_LOADED,this.onFragLoaded,this),e.off(S.FRAG_BUFFERED,this.onFragBuffered,this),e.off(S.LEVEL_SWITCHING,this.onLevelSwitching,this),e.off(S.LEVEL_LOADED,this.onLevelLoaded,this)},a.destroy=function(){this.unregisterListeners(),this.clearTimer(),this.hls=this.onCheck=null,this.fragCurrent=this.partCurrent=null},a.onFragLoading=function(e,t){var n,i=t.frag;this.ignoreFragment(i)||(this.fragCurrent=i,this.partCurrent=(n=t.part)!=null?n:null,this.clearTimer(),this.timer=self.setInterval(this.onCheck,100))},a.onLevelSwitching=function(e,t){this.clearTimer()},a.getTimeToLoadFrag=function(e,t,n,i){var o=e+n/t,l=i?this.lastLevelLoadSec:0;return o+l},a.onLevelLoaded=function(e,t){var n=this.hls.config,i=t.stats,o=i.total,l=i.bwEstimate;oe(o)&&oe(l)&&(this.lastLevelLoadSec=8*o/l),t.details.live?this.bwEstimator.update(n.abrEwmaSlowLive,n.abrEwmaFastLive):this.bwEstimator.update(n.abrEwmaSlowVoD,n.abrEwmaFastVoD)},a._abandonRulesCheck=function(){var e=this.fragCurrent,t=this.partCurrent,n=this.hls,i=n.autoLevelEnabled,o=n.media;if(!(!e||!o)){var l=performance.now(),d=t?t.stats:e.stats,u=t?t.duration:e.duration,c=l-d.loading.start;if(d.aborted||d.loaded&&d.loaded===d.total||e.level===0){this.clearTimer(),this._nextAutoLevel=-1;return}if(!(!i||o.paused||!o.playbackRate||!o.readyState)){var p=n.mainForwardBufferInfo;if(p!==null){var g=this.bwEstimator.getEstimateTTFB(),y=Math.abs(o.playbackRate);if(!(c<=Math.max(g,1e3*(u/(y*2))))){var b=p.len/y;if(!(b>=2*u/y)){var E=d.loading.first?d.loading.first-d.loading.start:-1,C=d.loaded&&E>-1,w=this.bwEstimator.getEstimate(),k=n.levels,D=n.minAutoLevel,_=k[e.level],F=d.total||Math.max(d.loaded,Math.round(u*_.maxBitrate/8)),O=c-E;O<1&&C&&(O=Math.min(c,d.loaded*8/w));var M=C?d.loaded*1e3/O:0,z=M?(F-d.loaded)/M:F*8/w+g/1e3;if(!(z<=b)){var H=M?M*8:w,G=Number.POSITIVE_INFINITY,te;for(te=e.level-1;te>D;te--){var ye=k[te].maxBitrate;if(G=this.getTimeToLoadFrag(g/1e3,H,u*ye,!k[te].details),G<b)break}G>=z||G>u*10||(n.nextLoadLevel=te,C?this.bwEstimator.sample(c-Math.min(g,E),d.loaded):this.bwEstimator.sampleTTFB(c),this.clearTimer(),P.warn("[abr] Fragment "+e.sn+(t?" part "+t.index:"")+" of level "+e.level+` is loading too slowly;
      Time to underbuffer: `+b.toFixed(3)+` s
      Estimated load time for current fragment: `+z.toFixed(3)+` s
      Estimated load time for down switch fragment: `+G.toFixed(3)+` s
      TTFB estimate: `+E+`
      Current BW estimate: `+(oe(w)?(w/1024).toFixed(3):"Unknown")+` Kb/s
      New BW estimate: `+(this.bwEstimator.getEstimate()/1024).toFixed(3)+` Kb/s
      Aborting and switching to level `+te),e.loader&&(this.fragCurrent=this.partCurrent=null,e.abortRequests()),n.trigger(S.FRAG_LOAD_EMERGENCY_ABORTED,{frag:e,part:t,stats:d}))}}}}}}},a.onFragLoaded=function(e,t){var n=t.frag,i=t.part,o=i?i.stats:n.stats;if(n.type===he.MAIN&&this.bwEstimator.sampleTTFB(o.loading.first-o.loading.start),!this.ignoreFragment(n)){if(this.clearTimer(),this.lastLoadedFragLevel=n.level,this._nextAutoLevel=-1,this.hls.config.abrMaxWithRealBitrate){var l=i?i.duration:n.duration,d=this.hls.levels[n.level],u=(d.loaded?d.loaded.bytes:0)+o.loaded,c=(d.loaded?d.loaded.duration:0)+l;d.loaded={bytes:u,duration:c},d.realBitrate=Math.round(8*u/c)}if(n.bitrateTest){var p={stats:o,frag:n,part:i,id:n.type};this.onFragBuffered(S.FRAG_BUFFERED,p),n.bitrateTest=!1}}},a.onFragBuffered=function(e,t){var n=t.frag,i=t.part,o=i!=null&&i.stats.loaded?i.stats:n.stats;if(!o.aborted&&!this.ignoreFragment(n)){var l=o.parsing.end-o.loading.start-Math.min(o.loading.first-o.loading.start,this.bwEstimator.getEstimateTTFB());this.bwEstimator.sample(l,o.loaded),o.bwEstimate=this.bwEstimator.getEstimate(),n.bitrateTest?this.bitrateTestDelay=l/1e3:this.bitrateTestDelay=0}},a.ignoreFragment=function(e){return e.type!==he.MAIN||e.sn==="initSegment"},a.clearTimer=function(){self.clearInterval(this.timer)},a.getNextABRAutoLevel=function(){var e=this.fragCurrent,t=this.partCurrent,n=this.hls,i=n.maxAutoLevel,o=n.config,l=n.minAutoLevel,d=n.media,u=t?t.duration:e?e.duration:0,c=d&&d.playbackRate!==0?Math.abs(d.playbackRate):1,p=this.bwEstimator?this.bwEstimator.getEstimate():o.abrEwmaDefaultEstimate,g=n.mainForwardBufferInfo,y=(g?g.len:0)/c,b=this.findBestLevel(p,l,i,y,o.abrBandWidthFactor,o.abrBandWidthUpFactor);if(b>=0)return b;P.trace("[abr] "+(y?"rebuffering expected":"buffer is empty")+", finding optimal quality level");var E=u?Math.min(u,o.maxStarvationDelay):o.maxStarvationDelay,C=o.abrBandWidthFactor,w=o.abrBandWidthUpFactor;if(!y){var k=this.bitrateTestDelay;if(k){var D=u?Math.min(u,o.maxLoadingDelay):o.maxLoadingDelay;E=D-k,P.trace("[abr] bitrate test took "+Math.round(1e3*k)+"ms, set first fragment max fetchDuration to "+Math.round(1e3*E)+" ms"),C=w=1}}return b=this.findBestLevel(p,l,i,y+E,C,w),Math.max(b,0)},a.findBestLevel=function(e,t,n,i,o,l){for(var d,u=this.fragCurrent,c=this.partCurrent,p=this.lastLoadedFragLevel,g=this.hls.levels,y=g[p],b=!!(y!=null&&(d=y.details)!=null&&d.live),E=y==null?void 0:y.codecSet,C=c?c.duration:u?u.duration:0,w=this.bwEstimator.getEstimateTTFB()/1e3,k=t,D=-1,_=n;_>=t;_--){var F=g[_];if(!F||E&&F.codecSet!==E){F&&(k=Math.min(_,k),D=Math.max(_,D));continue}D!==-1&&P.trace("[abr] Skipped level(s) "+k+"-"+D+' with CODECS:"'+g[D].attrs.CODECS+'"; not compatible with "'+y.attrs.CODECS+'"');var O=F.details,M=(c?O==null?void 0:O.partTarget:O==null?void 0:O.averagetargetduration)||C,z=void 0;_<=p?z=o*e:z=l*e;var H=g[_].maxBitrate,G=this.getTimeToLoadFrag(w,z,H*M,O===void 0);if(P.trace("[abr] level:"+_+" adjustedbw-bitrate:"+Math.round(z-H)+" avgDuration:"+M.toFixed(1)+" maxFetchDuration:"+i.toFixed(1)+" fetchDuration:"+G.toFixed(1)),z>H&&(G===0||!oe(G)||b&&!this.bitrateTestDelay||G<i))return _}return-1},Q(s,[{key:"nextAutoLevel",get:function(){var e=this._nextAutoLevel,t=this.bwEstimator;if(e!==-1&&!t.canEstimate())return e;var n=this.getNextABRAutoLevel();if(e!==-1){var i=this.hls.levels;if(i.length>Math.max(e,n)&&i[e].loadError<=i[n].loadError)return e}return e!==-1&&(n=Math.min(e,n)),n},set:function(e){this._nextAutoLevel=e}}]),s}(),po=function(){function s(){this.chunks=[],this.dataLength=0}var a=s.prototype;return a.push=function(e){this.chunks.push(e),this.dataLength+=e.length},a.flush=function(){var e=this.chunks,t=this.dataLength,n;if(e.length)e.length===1?n=e[0]:n=cs(e,t);else return new Uint8Array(0);return this.reset(),n},a.reset=function(){this.chunks.length=0,this.dataLength=0},s}();function cs(s,a){for(var r=new Uint8Array(a),e=0,t=0;t<s.length;t++){var n=s[t];r.set(n,e),e+=n.length}return r}var vo=100,fs=function(s){j(a,s);function a(e,t,n){var i;return i=s.call(this,e,t,n,"[audio-stream-controller]",he.AUDIO)||this,i.videoBuffer=null,i.videoTrackCC=-1,i.waitingVideoCC=-1,i.bufferedTrack=null,i.switchingTrack=null,i.trackId=-1,i.waitingData=null,i.mainDetails=null,i.bufferFlushed=!1,i.cachedTrackLoadedData=null,i._registerListeners(),i}var r=a.prototype;return r.onHandlerDestroying=function(){this._unregisterListeners(),this.mainDetails=null,this.bufferedTrack=null,this.switchingTrack=null},r._registerListeners=function(){var t=this.hls;t.on(S.MEDIA_ATTACHED,this.onMediaAttached,this),t.on(S.MEDIA_DETACHING,this.onMediaDetaching,this),t.on(S.MANIFEST_LOADING,this.onManifestLoading,this),t.on(S.LEVEL_LOADED,this.onLevelLoaded,this),t.on(S.AUDIO_TRACKS_UPDATED,this.onAudioTracksUpdated,this),t.on(S.AUDIO_TRACK_SWITCHING,this.onAudioTrackSwitching,this),t.on(S.AUDIO_TRACK_LOADED,this.onAudioTrackLoaded,this),t.on(S.ERROR,this.onError,this),t.on(S.BUFFER_RESET,this.onBufferReset,this),t.on(S.BUFFER_CREATED,this.onBufferCreated,this),t.on(S.BUFFER_FLUSHED,this.onBufferFlushed,this),t.on(S.INIT_PTS_FOUND,this.onInitPtsFound,this),t.on(S.FRAG_BUFFERED,this.onFragBuffered,this)},r._unregisterListeners=function(){var t=this.hls;t.off(S.MEDIA_ATTACHED,this.onMediaAttached,this),t.off(S.MEDIA_DETACHING,this.onMediaDetaching,this),t.off(S.MANIFEST_LOADING,this.onManifestLoading,this),t.off(S.LEVEL_LOADED,this.onLevelLoaded,this),t.off(S.AUDIO_TRACKS_UPDATED,this.onAudioTracksUpdated,this),t.off(S.AUDIO_TRACK_SWITCHING,this.onAudioTrackSwitching,this),t.off(S.AUDIO_TRACK_LOADED,this.onAudioTrackLoaded,this),t.off(S.ERROR,this.onError,this),t.off(S.BUFFER_RESET,this.onBufferReset,this),t.off(S.BUFFER_CREATED,this.onBufferCreated,this),t.off(S.BUFFER_FLUSHED,this.onBufferFlushed,this),t.off(S.INIT_PTS_FOUND,this.onInitPtsFound,this),t.off(S.FRAG_BUFFERED,this.onFragBuffered,this)},r.onInitPtsFound=function(t,n){var i=n.frag,o=n.id,l=n.initPTS,d=n.timescale;if(o==="main"){var u=i.cc;this.initPTS[i.cc]={baseTime:l,timescale:d},this.log("InitPTS for cc: "+u+" found from main: "+l),this.videoTrackCC=u,this.state===Y.WAITING_INIT_PTS&&this.tick()}},r.startLoad=function(t){if(!this.levels){this.startPosition=t,this.state=Y.STOPPED;return}var n=this.lastCurrentTime;this.stopLoad(),this.setInterval(vo),n>0&&t===-1?(this.log("Override startPosition with lastCurrentTime @"+n.toFixed(3)),t=n,this.state=Y.IDLE):(this.loadedmetadata=!1,this.state=Y.WAITING_TRACK),this.nextLoadPosition=this.startPosition=this.lastCurrentTime=t,this.tick()},r.doTick=function(){switch(this.state){case Y.IDLE:this.doTickIdle();break;case Y.WAITING_TRACK:{var t,n=this.levels,i=this.trackId,o=n==null||(t=n[i])==null?void 0:t.details;if(o){if(this.waitForCdnTuneIn(o))break;this.state=Y.WAITING_INIT_PTS}break}case Y.FRAG_LOADING_WAITING_RETRY:{var l,d=performance.now(),u=this.retryDate;(!u||d>=u||(l=this.media)!=null&&l.seeking)&&(this.log("RetryDate reached, switch back to IDLE state"),this.resetStartWhenNotLoaded(this.trackId),this.state=Y.IDLE);break}case Y.WAITING_INIT_PTS:{var c=this.waitingData;if(c){var p=c.frag,g=c.part,y=c.cache,b=c.complete;if(this.initPTS[p.cc]!==void 0){this.waitingData=null,this.waitingVideoCC=-1,this.state=Y.FRAG_LOADING;var E=y.flush(),C={frag:p,part:g,payload:E,networkDetails:null};this._handleFragmentLoadProgress(C),b&&s.prototype._handleFragmentLoadComplete.call(this,C)}else if(this.videoTrackCC!==this.waitingVideoCC)this.log("Waiting fragment cc ("+p.cc+") cancelled because video is at cc "+this.videoTrackCC),this.clearWaitingFragment();else{var w=this.getLoadPosition(),k=Fe.bufferInfo(this.mediaBuffer,w,this.config.maxBufferHole),D=Ir(k.end,this.config.maxFragLookUpTolerance,p);D<0&&(this.log("Waiting fragment cc ("+p.cc+") @ "+p.start+" cancelled because another fragment at "+k.end+" is needed"),this.clearWaitingFragment())}}else this.state=Y.IDLE}}this.onTickEnd()},r.clearWaitingFragment=function(){var t=this.waitingData;t&&(this.fragmentTracker.removeFragment(t.frag),this.waitingData=null,this.waitingVideoCC=-1,this.state=Y.IDLE)},r.resetLoadingState=function(){this.clearWaitingFragment(),s.prototype.resetLoadingState.call(this)},r.onTickEnd=function(){var t=this.media;!(t!=null&&t.readyState)||(this.lastCurrentTime=t.currentTime)},r.doTickIdle=function(){var t=this.hls,n=this.levels,i=this.media,o=this.trackId,l=t.config;if(!!(n!=null&&n[o])&&!(!i&&(this.startFragRequested||!l.startFragPrefetch))){var d=n[o],u=d.details;if(!u||u.live&&this.levelLastLoaded!==o||this.waitForCdnTuneIn(u)){this.state=Y.WAITING_TRACK;return}var c=this.mediaBuffer?this.mediaBuffer:this.media;this.bufferFlushed&&c&&(this.bufferFlushed=!1,this.afterBufferFlushed(c,Ce.AUDIO,he.AUDIO));var p=this.getFwdBufferInfo(c,he.AUDIO);if(p!==null){var g=this.bufferedTrack,y=this.switchingTrack;if(!y&&this._streamEnded(p,u)){t.trigger(S.BUFFER_EOS,{type:"audio"}),this.state=Y.ENDED;return}var b=this.getFwdBufferInfo(this.videoBuffer?this.videoBuffer:this.media,he.MAIN),E=p.len,C=this.getMaxBufferLength(b==null?void 0:b.len);if(!(E>=C&&!y)){var w=u.fragments,k=w[0].start,D=p.end;if(y&&i){var _=this.getLoadPosition();g&&y.attrs!==g.attrs&&(D=_),u.PTSKnown&&_<k&&(p.end>k||p.nextStart)&&(this.log("Alt audio track ahead of main track, seek to start of alt audio track"),i.currentTime=k+.05)}var F=this.getNextFragment(D,u),O=!1;if(F&&this.isLoopLoading(F,D)&&(O=!!F.gap,F=this.getNextFragmentLoopLoading(F,u,p,he.MAIN,C)),!F){this.bufferFlushed=!0;return}var M=b&&F.start>b.end+u.targetduration;if(M||!(b!=null&&b.len)&&p.len){var z=this.fragmentTracker.getBufferedFrag(F.start,he.MAIN);if(z===null||(O||(O=!!z.gap||!!M&&b.len===0),M&&!O||O&&p.nextStart&&p.nextStart<z.end))return}this.loadFragment(F,d,D)}}}},r.getMaxBufferLength=function(t){var n=s.prototype.getMaxBufferLength.call(this);return t?Math.min(Math.max(n,t),this.config.maxMaxBufferLength):n},r.onMediaDetaching=function(){this.videoBuffer=null,s.prototype.onMediaDetaching.call(this)},r.onAudioTracksUpdated=function(t,n){var i=n.audioTracks;this.resetTransmuxer(),this.levels=i.map(function(o){return new mn(o)})},r.onAudioTrackSwitching=function(t,n){var i=!!n.url;this.trackId=n.id;var o=this.fragCurrent;o&&(o.abortRequests(),this.removeUnbufferedFrags(o.start)),this.resetLoadingState(),i?this.setInterval(vo):this.resetTransmuxer(),i?(this.switchingTrack=n,this.state=Y.IDLE):(this.switchingTrack=null,this.bufferedTrack=n,this.state=Y.STOPPED),this.tick()},r.onManifestLoading=function(){this.mainDetails=null,this.fragmentTracker.removeAllFragments(),this.startPosition=this.lastCurrentTime=0,this.bufferFlushed=!1,this.bufferedTrack=null,this.switchingTrack=null},r.onLevelLoaded=function(t,n){this.mainDetails=n.details,this.cachedTrackLoadedData!==null&&(this.hls.trigger(S.AUDIO_TRACK_LOADED,this.cachedTrackLoadedData),this.cachedTrackLoadedData=null)},r.onAudioTrackLoaded=function(t,n){var i;if(this.mainDetails==null){this.cachedTrackLoadedData=n;return}var o=this.levels,l=n.details,d=n.id;if(!o){this.warn("Audio tracks were reset while loading level "+d);return}this.log("Track "+d+" loaded ["+l.startSN+","+l.endSN+"],duration:"+l.totalduration);var u=o[d],c=0;if(l.live||(i=u.details)!=null&&i.live){var p=this.mainDetails;if(l.fragments[0]||(l.deltaUpdateFailed=!0),l.deltaUpdateFailed||!p)return;!u.details&&l.hasProgramDateTime&&p.hasProgramDateTime?(bi(l,p),c=l.fragments[0].start):c=this.alignPlaylists(l,u.details)}u.details=l,this.levelLastLoaded=d,!this.startFragRequested&&(this.mainDetails||!l.live)&&this.setStartPosition(u.details,c),this.state===Y.WAITING_TRACK&&!this.waitForCdnTuneIn(l)&&(this.state=Y.IDLE),this.tick()},r._handleFragmentLoadProgress=function(t){var n,i=t.frag,o=t.part,l=t.payload,d=this.config,u=this.trackId,c=this.levels;if(!c){this.warn("Audio tracks were reset while fragment load was in progress. Fragment "+i.sn+" of level "+i.level+" will not be buffered");return}var p=c[u];if(!p){this.warn("Audio track is undefined on fragment load progress");return}var g=p.details;if(!g){this.warn("Audio track details undefined on fragment load progress"),this.removeUnbufferedFrags(i.start);return}var y=d.defaultAudioCodec||p.audioCodec||"mp4a.40.2",b=this.transmuxer;b||(b=this.transmuxer=new ho(this.hls,he.AUDIO,this._handleTransmuxComplete.bind(this),this._handleTransmuxerFlush.bind(this)));var E=this.initPTS[i.cc],C=(n=i.initSegment)==null?void 0:n.data;if(E!==void 0){var w=!1,k=o?o.index:-1,D=k!==-1,_=new Dr(i.level,i.sn,i.stats.chunkCount,l.byteLength,k,D);b.push(l,C,y,"",i,o,g.totalduration,w,_,E)}else{this.log("Unknown video PTS for cc "+i.cc+", waiting for video PTS before demuxing audio frag "+i.sn+" of ["+g.startSN+" ,"+g.endSN+"],track "+u);var F=this.waitingData=this.waitingData||{frag:i,part:o,cache:new po,complete:!1},O=F.cache;O.push(new Uint8Array(l)),this.waitingVideoCC=this.videoTrackCC,this.state=Y.WAITING_INIT_PTS}},r._handleFragmentLoadComplete=function(t){if(this.waitingData){this.waitingData.complete=!0;return}s.prototype._handleFragmentLoadComplete.call(this,t)},r.onBufferReset=function(){this.mediaBuffer=this.videoBuffer=null,this.loadedmetadata=!1},r.onBufferCreated=function(t,n){var i=n.tracks.audio;i&&(this.mediaBuffer=i.buffer||null),n.tracks.video&&(this.videoBuffer=n.tracks.video.buffer||null)},r.onFragBuffered=function(t,n){var i=n.frag,o=n.part;if(i.type!==he.AUDIO){if(!this.loadedmetadata&&i.type===he.MAIN){var l;(l=this.videoBuffer||this.media)!=null&&l.buffered.length&&(this.loadedmetadata=!0)}return}if(this.fragContextChanged(i)){this.warn("Fragment "+i.sn+(o?" p: "+o.index:"")+" of level "+i.level+" finished buffering, but was aborted. state: "+this.state+", audioSwitch: "+(this.switchingTrack?this.switchingTrack.name:"false"));return}if(i.sn!=="initSegment"){this.fragPrevious=i;var d=this.switchingTrack;d&&(this.bufferedTrack=d,this.switchingTrack=null,this.hls.trigger(S.AUDIO_TRACK_SWITCHED,Ke({},d)))}this.fragBufferedComplete(i,o)},r.onError=function(t,n){var i;if(n.fatal){this.state=Y.ERROR;return}switch(n.details){case U.FRAG_GAP:case U.FRAG_PARSING_ERROR:case U.FRAG_DECRYPT_ERROR:case U.FRAG_LOAD_ERROR:case U.FRAG_LOAD_TIMEOUT:case U.KEY_LOAD_ERROR:case U.KEY_LOAD_TIMEOUT:this.onFragmentOrKeyLoadError(he.AUDIO,n);break;case U.AUDIO_TRACK_LOAD_ERROR:case U.AUDIO_TRACK_LOAD_TIMEOUT:case U.LEVEL_PARSING_ERROR:!n.levelRetry&&this.state===Y.WAITING_TRACK&&((i=n.context)==null?void 0:i.type)===be.AUDIO_TRACK&&(this.state=Y.IDLE);break;case U.BUFFER_FULL_ERROR:if(!n.parent||n.parent!=="audio")return;this.reduceLengthAndFlushBuffer(n)&&(this.bufferedTrack=null,s.prototype.flushMainBuffer.call(this,0,Number.POSITIVE_INFINITY,"audio"));break;case U.INTERNAL_EXCEPTION:this.recoverWorkerError(n);break}},r.onBufferFlushed=function(t,n){var i=n.type;i===Ce.AUDIO&&(this.bufferFlushed=!0,this.state===Y.ENDED&&(this.state=Y.IDLE))},r._handleTransmuxComplete=function(t){var n,i="audio",o=this.hls,l=t.remuxResult,d=t.chunkMeta,u=this.getCurrentContext(d);if(!u){this.resetWhenMissingContext(d);return}var c=u.frag,p=u.part,g=u.level,y=g.details,b=l.audio,E=l.text,C=l.id3,w=l.initSegment;if(this.fragContextChanged(c)||!y){this.fragmentTracker.removeFragment(c);return}if(this.state=Y.PARSING,this.switchingTrack&&b&&this.completeAudioSwitch(this.switchingTrack),w!=null&&w.tracks&&(this._bufferInitSegment(w.tracks,c,d),o.trigger(S.FRAG_PARSING_INIT_SEGMENT,{frag:c,id:i,tracks:w.tracks})),b){var k=b.startPTS,D=b.endPTS,_=b.startDTS,F=b.endDTS;p&&(p.elementaryStreams[Ce.AUDIO]={startPTS:k,endPTS:D,startDTS:_,endDTS:F}),c.setElementaryStreamInfo(Ce.AUDIO,k,D,_,F),this.bufferFragmentData(b,c,p,d)}if(C!=null&&(n=C.samples)!=null&&n.length){var O=q({id:i,frag:c,details:y},C);o.trigger(S.FRAG_PARSING_METADATA,O)}if(E){var M=q({id:i,frag:c,details:y},E);o.trigger(S.FRAG_PARSING_USERDATA,M)}},r._bufferInitSegment=function(t,n,i){if(this.state===Y.PARSING){t.video&&delete t.video;var o=t.audio;if(!!o){o.levelCodec=o.codec,o.id="audio",this.log("Init audio buffer, container:"+o.container+", codecs[parsed]=["+o.codec+"]"),this.hls.trigger(S.BUFFER_CODECS,t);var l=o.initSegment;if(l!=null&&l.byteLength){var d={type:"audio",frag:n,part:null,chunkMeta:i,parent:n.type,data:l};this.hls.trigger(S.BUFFER_APPENDING,d)}this.tick()}}},r.loadFragment=function(t,n,i){var o=this.fragmentTracker.getState(t);if(this.fragCurrent=t,this.switchingTrack||o===Ye.NOT_LOADED||o===Ye.PARTIAL){var l;t.sn==="initSegment"?this._loadInitSegment(t,n):(l=n.details)!=null&&l.live&&!this.initPTS[t.cc]?(this.log("Waiting for video PTS in continuity counter "+t.cc+" of live stream before loading audio fragment "+t.sn+" of level "+this.trackId),this.state=Y.WAITING_INIT_PTS):(this.startFragRequested=!0,s.prototype.loadFragment.call(this,t,n,i))}else this.clearTrackerIfNeeded(t)},r.completeAudioSwitch=function(t){var n=this.hls,i=this.media,o=this.bufferedTrack,l=o==null?void 0:o.attrs,d=t.attrs;i&&l&&(l.CHANNELS!==d.CHANNELS||l.NAME!==d.NAME||l.LANGUAGE!==d.LANGUAGE)&&(this.log("Switching audio track : flushing all audio"),s.prototype.flushMainBuffer.call(this,0,Number.POSITIVE_INFINITY,"audio")),this.bufferedTrack=t,this.switchingTrack=null,n.trigger(S.AUDIO_TRACK_SWITCHED,Ke({},t))},a}(Br),hs=function(s){j(a,s);function a(e){var t;return t=s.call(this,e,"[audio-track-controller]")||this,t.tracks=[],t.groupId=null,t.tracksInGroup=[],t.trackId=-1,t.currentTrack=null,t.selectDefaultTrack=!0,t.registerListeners(),t}var r=a.prototype;return r.registerListeners=function(){var t=this.hls;t.on(S.MANIFEST_LOADING,this.onManifestLoading,this),t.on(S.MANIFEST_PARSED,this.onManifestParsed,this),t.on(S.LEVEL_LOADING,this.onLevelLoading,this),t.on(S.LEVEL_SWITCHING,this.onLevelSwitching,this),t.on(S.AUDIO_TRACK_LOADED,this.onAudioTrackLoaded,this),t.on(S.ERROR,this.onError,this)},r.unregisterListeners=function(){var t=this.hls;t.off(S.MANIFEST_LOADING,this.onManifestLoading,this),t.off(S.MANIFEST_PARSED,this.onManifestParsed,this),t.off(S.LEVEL_LOADING,this.onLevelLoading,this),t.off(S.LEVEL_SWITCHING,this.onLevelSwitching,this),t.off(S.AUDIO_TRACK_LOADED,this.onAudioTrackLoaded,this),t.off(S.ERROR,this.onError,this)},r.destroy=function(){this.unregisterListeners(),this.tracks.length=0,this.tracksInGroup.length=0,this.currentTrack=null,s.prototype.destroy.call(this)},r.onManifestLoading=function(){this.tracks=[],this.groupId=null,this.tracksInGroup=[],this.trackId=-1,this.currentTrack=null,this.selectDefaultTrack=!0},r.onManifestParsed=function(t,n){this.tracks=n.audioTracks||[]},r.onAudioTrackLoaded=function(t,n){var i=n.id,o=n.groupId,l=n.details,d=this.tracksInGroup[i];if(!d||d.groupId!==o){this.warn("Track with id:"+i+" and group:"+o+" not found in active group "+d.groupId);return}var u=d.details;d.details=n.details,this.log("audio-track "+i+' "'+d.name+'" lang:'+d.lang+" group:"+o+" loaded ["+l.startSN+"-"+l.endSN+"]"),i===this.trackId&&this.playlistLoaded(i,n,u)},r.onLevelLoading=function(t,n){this.switchLevel(n.level)},r.onLevelSwitching=function(t,n){this.switchLevel(n.level)},r.switchLevel=function(t){var n=this.hls.levels[t];if(!!(n!=null&&n.audioGroupIds)){var i=n.audioGroupIds[n.urlId];if(this.groupId!==i){this.groupId=i||null;var o=this.tracks.filter(function(d){return!i||d.groupId===i});this.selectDefaultTrack&&!o.some(function(d){return d.default})&&(this.selectDefaultTrack=!1),this.tracksInGroup=o;var l={audioTracks:o};this.log("Updating audio tracks, "+o.length+" track(s) found in group:"+i),this.hls.trigger(S.AUDIO_TRACKS_UPDATED,l),this.selectInitialTrack()}else this.shouldReloadPlaylist(this.currentTrack)&&this.setAudioTrack(this.trackId)}},r.onError=function(t,n){n.fatal||!n.context||n.context.type===be.AUDIO_TRACK&&n.context.id===this.trackId&&n.context.groupId===this.groupId&&(this.requestScheduled=-1,this.checkRetry(n))},r.setAudioTrack=function(t){var n=this.tracksInGroup;if(t<0||t>=n.length){this.warn("Invalid id passed to audio-track controller");return}this.clearTimer();var i=this.currentTrack;n[this.trackId];var o=n[t],l=o.groupId,d=o.name;if(this.log("Switching to audio-track "+t+' "'+d+'" lang:'+o.lang+" group:"+l),this.trackId=t,this.currentTrack=o,this.selectDefaultTrack=!1,this.hls.trigger(S.AUDIO_TRACK_SWITCHING,Ke({},o)),!(o.details&&!o.details.live)){var u=this.switchParams(o.url,i==null?void 0:i.details);this.loadPlaylist(u)}},r.selectInitialTrack=function(){var t=this.tracksInGroup,n=this.findTrackId(this.currentTrack)|this.findTrackId(null);if(n!==-1)this.setAudioTrack(n);else{var i=new Error("No track found for running audio group-ID: "+this.groupId+" track count: "+t.length);this.warn(i.message),this.hls.trigger(S.ERROR,{type:fe.MEDIA_ERROR,details:U.AUDIO_TRACK_LOAD_ERROR,fatal:!0,error:i})}},r.findTrackId=function(t){for(var n=this.tracksInGroup,i=0;i<n.length;i++){var o=n[i];if((!this.selectDefaultTrack||o.default)&&(!t||t.attrs["STABLE-RENDITION-ID"]===o.attrs["STABLE-RENDITION-ID"]||t.name===o.name&&t.lang===o.lang))return o.id}return-1},r.loadPlaylist=function(t){s.prototype.loadPlaylist.call(this);var n=this.tracksInGroup[this.trackId];if(this.shouldLoadPlaylist(n)){var i=n.id,o=n.groupId,l=n.url;if(t)try{l=t.addDirectives(l)}catch(d){this.warn("Could not construct new URL with HLS Delivery Directives: "+d)}this.log("loading audio-track playlist "+i+' "'+n.name+'" lang:'+n.lang+" group:"+o),this.clearTimer(),this.hls.trigger(S.AUDIO_TRACK_LOADING,{url:l,id:i,groupId:o,deliveryDirectives:t||null})}},Q(a,[{key:"audioTracks",get:function(){return this.tracksInGroup}},{key:"audioTrack",get:function(){return this.trackId},set:function(t){this.selectDefaultTrack=!1,this.setAudioTrack(t)}}]),a}(wt);function mo(s,a){if(s.length!==a.length)return!1;for(var r=0;r<s.length;r++)if(!ps(s[r].attrs,a[r].attrs))return!1;return!0}function ps(s,a){var r=s["STABLE-RENDITION-ID"];return r?r===a["STABLE-RENDITION-ID"]:!["LANGUAGE","NAME","CHARACTERISTICS","AUTOSELECT","DEFAULT","FORCED"].some(function(e){return s[e]!==a[e]})}var go=500,vs=function(s){j(a,s);function a(e,t,n){var i;return i=s.call(this,e,t,n,"[subtitle-stream-controller]",he.SUBTITLE)||this,i.levels=[],i.currentTrackId=-1,i.tracksBuffered=[],i.mainDetails=null,i._registerListeners(),i}var r=a.prototype;return r.onHandlerDestroying=function(){this._unregisterListeners(),this.mainDetails=null},r._registerListeners=function(){var t=this.hls;t.on(S.MEDIA_ATTACHED,this.onMediaAttached,this),t.on(S.MEDIA_DETACHING,this.onMediaDetaching,this),t.on(S.MANIFEST_LOADING,this.onManifestLoading,this),t.on(S.LEVEL_LOADED,this.onLevelLoaded,this),t.on(S.ERROR,this.onError,this),t.on(S.SUBTITLE_TRACKS_UPDATED,this.onSubtitleTracksUpdated,this),t.on(S.SUBTITLE_TRACK_SWITCH,this.onSubtitleTrackSwitch,this),t.on(S.SUBTITLE_TRACK_LOADED,this.onSubtitleTrackLoaded,this),t.on(S.SUBTITLE_FRAG_PROCESSED,this.onSubtitleFragProcessed,this),t.on(S.BUFFER_FLUSHING,this.onBufferFlushing,this),t.on(S.FRAG_BUFFERED,this.onFragBuffered,this)},r._unregisterListeners=function(){var t=this.hls;t.off(S.MEDIA_ATTACHED,this.onMediaAttached,this),t.off(S.MEDIA_DETACHING,this.onMediaDetaching,this),t.off(S.MANIFEST_LOADING,this.onManifestLoading,this),t.off(S.LEVEL_LOADED,this.onLevelLoaded,this),t.off(S.ERROR,this.onError,this),t.off(S.SUBTITLE_TRACKS_UPDATED,this.onSubtitleTracksUpdated,this),t.off(S.SUBTITLE_TRACK_SWITCH,this.onSubtitleTrackSwitch,this),t.off(S.SUBTITLE_TRACK_LOADED,this.onSubtitleTrackLoaded,this),t.off(S.SUBTITLE_FRAG_PROCESSED,this.onSubtitleFragProcessed,this),t.off(S.BUFFER_FLUSHING,this.onBufferFlushing,this),t.off(S.FRAG_BUFFERED,this.onFragBuffered,this)},r.startLoad=function(t){this.stopLoad(),this.state=Y.IDLE,this.setInterval(go),this.nextLoadPosition=this.startPosition=this.lastCurrentTime=t,this.tick()},r.onManifestLoading=function(){this.mainDetails=null,this.fragmentTracker.removeAllFragments()},r.onMediaDetaching=function(){this.tracksBuffered=[],s.prototype.onMediaDetaching.call(this)},r.onLevelLoaded=function(t,n){this.mainDetails=n.details},r.onSubtitleFragProcessed=function(t,n){var i=n.frag,o=n.success;if(this.fragPrevious=i,this.state=Y.IDLE,!!o){var l=this.tracksBuffered[this.currentTrackId];if(!!l){for(var d,u=i.start,c=0;c<l.length;c++)if(u>=l[c].start&&u<=l[c].end){d=l[c];break}var p=i.start+i.duration;d?d.end=p:(d={start:u,end:p},l.push(d)),this.fragmentTracker.fragBuffered(i)}}},r.onBufferFlushing=function(t,n){var i=n.startOffset,o=n.endOffset;if(i===0&&o!==Number.POSITIVE_INFINITY){var l=this.currentTrackId,d=this.levels;if(!d.length||!d[l]||!d[l].details)return;var u=d[l].details,c=u.targetduration,p=o-c;if(p<=0)return;n.endOffsetSubtitles=Math.max(0,p),this.tracksBuffered.forEach(function(g){for(var y=0;y<g.length;){if(g[y].end<=p){g.shift();continue}else if(g[y].start<p)g[y].start=p;else break;y++}}),this.fragmentTracker.removeFragmentsInRange(i,p,he.SUBTITLE)}},r.onFragBuffered=function(t,n){if(!this.loadedmetadata&&n.frag.type===he.MAIN){var i;(i=this.media)!=null&&i.buffered.length&&(this.loadedmetadata=!0)}},r.onError=function(t,n){var i=n.frag;(i==null?void 0:i.type)===he.SUBTITLE&&(this.fragCurrent&&this.fragCurrent.abortRequests(),this.state!==Y.STOPPED&&(this.state=Y.IDLE))},r.onSubtitleTracksUpdated=function(t,n){var i=this,o=n.subtitleTracks;if(mo(this.levels,o)){this.levels=o.map(function(l){return new mn(l)});return}this.tracksBuffered=[],this.levels=o.map(function(l){var d=new mn(l);return i.tracksBuffered[d.id]=[],d}),this.fragmentTracker.removeFragmentsInRange(0,Number.POSITIVE_INFINITY,he.SUBTITLE),this.fragPrevious=null,this.mediaBuffer=null},r.onSubtitleTrackSwitch=function(t,n){if(this.currentTrackId=n.id,!this.levels.length||this.currentTrackId===-1){this.clearInterval();return}var i=this.levels[this.currentTrackId];i!=null&&i.details?this.mediaBuffer=this.mediaBufferTimeRanges:this.mediaBuffer=null,i&&this.setInterval(go)},r.onSubtitleTrackLoaded=function(t,n){var i,o=n.details,l=n.id,d=this.currentTrackId,u=this.levels;if(!!u.length){var c=u[d];if(!(l>=u.length||l!==d||!c)){this.mediaBuffer=this.mediaBufferTimeRanges;var p=0;if(o.live||(i=c.details)!=null&&i.live){var g=this.mainDetails;if(o.deltaUpdateFailed||!g)return;var y=g.fragments[0];c.details?(p=this.alignPlaylists(o,c.details),p===0&&y&&(p=y.start,wr(o,p))):o.hasProgramDateTime&&g.hasProgramDateTime?(bi(o,g),p=o.fragments[0].start):y&&(p=y.start,wr(o,p))}if(c.details=o,this.levelLastLoaded=l,!this.startFragRequested&&(this.mainDetails||!o.live)&&this.setStartPosition(c.details,p),this.tick(),o.live&&!this.fragCurrent&&this.media&&this.state===Y.IDLE){var b=gn(null,o.fragments,this.media.currentTime,0);b||(this.warn("Subtitle playlist not aligned with playback"),c.details=void 0)}}}},r._handleFragmentLoadComplete=function(t){var n=this,i=t.frag,o=t.payload,l=i.decryptdata,d=this.hls;if(!this.fragContextChanged(i)&&o&&o.byteLength>0&&l&&l.key&&l.iv&&l.method==="AES-128"){var u=performance.now();this.decrypter.decrypt(new Uint8Array(o),l.key.buffer,l.iv.buffer).catch(function(c){throw d.trigger(S.ERROR,{type:fe.MEDIA_ERROR,details:U.FRAG_DECRYPT_ERROR,fatal:!1,error:c,reason:c.message,frag:i}),c}).then(function(c){var p=performance.now();d.trigger(S.FRAG_DECRYPTED,{frag:i,payload:c,stats:{tstart:u,tdecrypt:p}})}).catch(function(c){n.warn(c.name+": "+c.message),n.state=Y.IDLE})}},r.doTick=function(){if(!this.media){this.state=Y.IDLE;return}if(this.state===Y.IDLE){var t=this.currentTrackId,n=this.levels,i=n[t];if(!n.length||!i||!i.details)return;var o=i.details,l=o.targetduration,d=this.config,u=this.getLoadPosition(),c=Fe.bufferedInfo(this.tracksBuffered[this.currentTrackId]||[],u-l,d.maxBufferHole),p=c.end,g=c.len,y=this.getFwdBufferInfo(this.media,he.MAIN),b=this.getMaxBufferLength(y==null?void 0:y.len)+l;if(g>b)return;var E=o.fragments,C=E.length,w=o.edge,k=null,D=this.fragPrevious;if(p<w){var _=d.maxFragLookUpTolerance;k=gn(D,E,Math.max(E[0].start,p),_),!k&&D&&D.start<E[0].start&&(k=E[0])}else k=E[C-1];if(!k)return;k=this.mapToInitFragWhenRequired(k),this.fragmentTracker.getState(k)===Ye.NOT_LOADED&&this.loadFragment(k,i,p)}},r.getMaxBufferLength=function(t){var n=s.prototype.getMaxBufferLength.call(this);return t?Math.max(n,t):n},r.loadFragment=function(t,n,i){this.fragCurrent=t,t.sn==="initSegment"?this._loadInitSegment(t,n):(this.startFragRequested=!0,s.prototype.loadFragment.call(this,t,n,i))},Q(a,[{key:"mediaBufferTimeRanges",get:function(){return new ms(this.tracksBuffered[this.currentTrackId]||[])}}]),a}(Br),ms=function(a){this.buffered=void 0;var r=function(t,n,i){if(n=n>>>0,n>i-1)throw new DOMException("Failed to execute '"+t+"' on 'TimeRanges': The index provided ("+n+") is greater than the maximum bound ("+i+")");return a[n][t]};this.buffered={get length(){return a.length},end:function(t){return r("end",t,a.length)},start:function(t){return r("start",t,a.length)}}},gs=function(s){j(a,s);function a(e){var t;return t=s.call(this,e,"[subtitle-track-controller]")||this,t.media=null,t.tracks=[],t.groupId=null,t.tracksInGroup=[],t.trackId=-1,t.selectDefaultTrack=!0,t.queuedDefaultTrack=-1,t.trackChangeListener=function(){return t.onTextTracksChanged()},t.asyncPollTrackChange=function(){return t.pollTrackChange(0)},t.useTextTrackPolling=!1,t.subtitlePollingInterval=-1,t._subtitleDisplay=!0,t.registerListeners(),t}var r=a.prototype;return r.destroy=function(){this.unregisterListeners(),this.tracks.length=0,this.tracksInGroup.length=0,this.trackChangeListener=this.asyncPollTrackChange=null,s.prototype.destroy.call(this)},r.registerListeners=function(){var t=this.hls;t.on(S.MEDIA_ATTACHED,this.onMediaAttached,this),t.on(S.MEDIA_DETACHING,this.onMediaDetaching,this),t.on(S.MANIFEST_LOADING,this.onManifestLoading,this),t.on(S.MANIFEST_PARSED,this.onManifestParsed,this),t.on(S.LEVEL_LOADING,this.onLevelLoading,this),t.on(S.LEVEL_SWITCHING,this.onLevelSwitching,this),t.on(S.SUBTITLE_TRACK_LOADED,this.onSubtitleTrackLoaded,this),t.on(S.ERROR,this.onError,this)},r.unregisterListeners=function(){var t=this.hls;t.off(S.MEDIA_ATTACHED,this.onMediaAttached,this),t.off(S.MEDIA_DETACHING,this.onMediaDetaching,this),t.off(S.MANIFEST_LOADING,this.onManifestLoading,this),t.off(S.MANIFEST_PARSED,this.onManifestParsed,this),t.off(S.LEVEL_LOADING,this.onLevelLoading,this),t.off(S.LEVEL_SWITCHING,this.onLevelSwitching,this),t.off(S.SUBTITLE_TRACK_LOADED,this.onSubtitleTrackLoaded,this),t.off(S.ERROR,this.onError,this)},r.onMediaAttached=function(t,n){this.media=n.media,!!this.media&&(this.queuedDefaultTrack>-1&&(this.subtitleTrack=this.queuedDefaultTrack,this.queuedDefaultTrack=-1),this.useTextTrackPolling=!(this.media.textTracks&&"onchange"in this.media.textTracks),this.useTextTrackPolling?this.pollTrackChange(500):this.media.textTracks.addEventListener("change",this.asyncPollTrackChange))},r.pollTrackChange=function(t){self.clearInterval(this.subtitlePollingInterval),this.subtitlePollingInterval=self.setInterval(this.trackChangeListener,t)},r.onMediaDetaching=function(){if(!!this.media){self.clearInterval(this.subtitlePollingInterval),this.useTextTrackPolling||this.media.textTracks.removeEventListener("change",this.asyncPollTrackChange),this.trackId>-1&&(this.queuedDefaultTrack=this.trackId);var t=ja(this.media.textTracks);t.forEach(function(n){Pe(n)}),this.subtitleTrack=-1,this.media=null}},r.onManifestLoading=function(){this.tracks=[],this.groupId=null,this.tracksInGroup=[],this.trackId=-1,this.selectDefaultTrack=!0},r.onManifestParsed=function(t,n){this.tracks=n.subtitleTracks},r.onSubtitleTrackLoaded=function(t,n){var i=n.id,o=n.details,l=this.trackId,d=this.tracksInGroup[l];if(!d){this.warn("Invalid subtitle track id "+i);return}var u=d.details;d.details=n.details,this.log("subtitle track "+i+" loaded ["+o.startSN+"-"+o.endSN+"]"),i===this.trackId&&this.playlistLoaded(i,n,u)},r.onLevelLoading=function(t,n){this.switchLevel(n.level)},r.onLevelSwitching=function(t,n){this.switchLevel(n.level)},r.switchLevel=function(t){var n=this.hls.levels[t];if(!!(n!=null&&n.textGroupIds)){var i=n.textGroupIds[n.urlId],o=this.tracksInGroup?this.tracksInGroup[this.trackId]:void 0;if(this.groupId!==i){var l=this.tracks.filter(function(c){return!i||c.groupId===i});this.tracksInGroup=l;var d=this.findTrackId(o==null?void 0:o.name)||this.findTrackId();this.groupId=i||null;var u={subtitleTracks:l};this.log("Updating subtitle tracks, "+l.length+' track(s) found in "'+i+'" group-id'),this.hls.trigger(S.SUBTITLE_TRACKS_UPDATED,u),d!==-1&&this.setSubtitleTrack(d,o)}else this.shouldReloadPlaylist(o)&&this.setSubtitleTrack(this.trackId,o)}},r.findTrackId=function(t){for(var n=this.tracksInGroup,i=0;i<n.length;i++){var o=n[i];if((!this.selectDefaultTrack||o.default)&&(!t||t===o.name))return o.id}return-1},r.onError=function(t,n){n.fatal||!n.context||n.context.type===be.SUBTITLE_TRACK&&n.context.id===this.trackId&&n.context.groupId===this.groupId&&this.checkRetry(n)},r.loadPlaylist=function(t){s.prototype.loadPlaylist.call(this);var n=this.tracksInGroup[this.trackId];if(this.shouldLoadPlaylist(n)){var i=n.id,o=n.groupId,l=n.url;if(t)try{l=t.addDirectives(l)}catch(d){this.warn("Could not construct new URL with HLS Delivery Directives: "+d)}this.log("Loading subtitle playlist for id "+i),this.hls.trigger(S.SUBTITLE_TRACK_LOADING,{url:l,id:i,groupId:o,deliveryDirectives:t||null})}},r.toggleTrackModes=function(t){var n=this,i=this.media,o=this.trackId;if(!!i){var l=ja(i.textTracks),d=l.filter(function(p){return p.groupId===n.groupId});if(t===-1)[].slice.call(l).forEach(function(p){p.mode="disabled"});else{var u=d[o];u&&(u.mode="disabled")}var c=d[t];c&&(c.mode=this.subtitleDisplay?"showing":"hidden")}},r.setSubtitleTrack=function(t,n){var i,o=this.tracksInGroup;if(!this.media){this.queuedDefaultTrack=t;return}if(this.trackId!==t&&this.toggleTrackModes(t),!(this.trackId===t&&(t===-1||(i=o[t])!=null&&i.details)||t<-1||t>=o.length)){this.clearTimer();var l=o[t];if(this.log("Switching to subtitle-track "+t+(l?' "'+l.name+'" lang:'+l.lang+" group:"+l.groupId:"")),this.trackId=t,l){var d=l.id,u=l.groupId,c=u===void 0?"":u,p=l.name,g=l.type,y=l.url;this.hls.trigger(S.SUBTITLE_TRACK_SWITCH,{id:d,groupId:c,name:p,type:g,url:y});var b=this.switchParams(l.url,n==null?void 0:n.details);this.loadPlaylist(b)}else this.hls.trigger(S.SUBTITLE_TRACK_SWITCH,{id:t})}},r.onTextTracksChanged=function(){if(this.useTextTrackPolling||self.clearInterval(this.subtitlePollingInterval),!(!this.media||!this.hls.config.renderTextTracksNatively)){for(var t=-1,n=ja(this.media.textTracks),i=0;i<n.length;i++)if(n[i].mode==="hidden")t=i;else if(n[i].mode==="showing"){t=i;break}this.subtitleTrack!==t&&(this.subtitleTrack=t)}},Q(a,[{key:"subtitleDisplay",get:function(){return this._subtitleDisplay},set:function(t){this._subtitleDisplay=t,this.trackId>-1&&this.toggleTrackModes(this.trackId)}},{key:"subtitleTracks",get:function(){return this.tracksInGroup}},{key:"subtitleTrack",get:function(){return this.trackId},set:function(t){this.selectDefaultTrack=!1;var n=this.tracksInGroup?this.tracksInGroup[this.trackId]:void 0;this.setSubtitleTrack(t,n)}}]),a}(wt);function ja(s){for(var a=[],r=0;r<s.length;r++){var e=s[r];(e.kind==="subtitles"||e.kind==="captions")&&e.label&&a.push(s[r])}return a}var ys=function(){function s(r){this.buffers=void 0,this.queues={video:[],audio:[],audiovideo:[]},this.buffers=r}var a=s.prototype;return a.append=function(e,t){var n=this.queues[t];n.push(e),n.length===1&&this.buffers[t]&&this.executeNext(t)},a.insertAbort=function(e,t){var n=this.queues[t];n.unshift(e),this.executeNext(t)},a.appendBlocker=function(e){var t,n=new Promise(function(o){t=o}),i={execute:t,onStart:function(){},onComplete:function(){},onError:function(){}};return this.append(i,e),n},a.executeNext=function(e){var t=this.buffers,n=this.queues,i=t[e],o=n[e];if(o.length){var l=o[0];try{l.execute()}catch(d){P.warn("[buffer-operation-queue]: Unhandled exception executing the current operation"),l.onError(d),i!=null&&i.updating||(o.shift(),this.executeNext(e))}}},a.shiftAndExecuteNext=function(e){this.queues[e].shift(),this.executeNext(e)},a.current=function(e){return this.queues[e][0]},s}(),yo=It(),Ao=/([ha]vc.)(?:\.[^.,]+)+/,As=function(){function s(r){var e=this;this.details=null,this._objectUrl=null,this.operationQueue=void 0,this.listeners=void 0,this.hls=void 0,this.bufferCodecEventsExpected=0,this._bufferCodecEventsTotal=0,this.media=null,this.mediaSource=null,this.lastMpegAudioChunk=null,this.appendError=0,this.tracks={},this.pendingTracks={},this.sourceBuffer=void 0,this._onMediaSourceOpen=function(){var t=e.media,n=e.mediaSource;P.log("[buffer-controller]: Media source opened"),t&&(t.removeEventListener("emptied",e._onMediaEmptied),e.updateMediaElementDuration(),e.hls.trigger(S.MEDIA_ATTACHED,{media:t})),n&&n.removeEventListener("sourceopen",e._onMediaSourceOpen),e.checkPendingTracks()},this._onMediaSourceClose=function(){P.log("[buffer-controller]: Media source closed")},this._onMediaSourceEnded=function(){P.log("[buffer-controller]: Media source ended")},this._onMediaEmptied=function(){var t=e.media,n=e._objectUrl;t&&t.src!==n&&P.error("Media element src was set while attaching MediaSource ("+n+" > "+t.src+")")},this.hls=r,this._initSourceBuffer(),this.registerListeners()}var a=s.prototype;return a.hasSourceTypes=function(){return this.getSourceBufferTypes().length>0||Object.keys(this.pendingTracks).length>0},a.destroy=function(){this.unregisterListeners(),this.details=null,this.lastMpegAudioChunk=null},a.registerListeners=function(){var e=this.hls;e.on(S.MEDIA_ATTACHING,this.onMediaAttaching,this),e.on(S.MEDIA_DETACHING,this.onMediaDetaching,this),e.on(S.MANIFEST_PARSED,this.onManifestParsed,this),e.on(S.BUFFER_RESET,this.onBufferReset,this),e.on(S.BUFFER_APPENDING,this.onBufferAppending,this),e.on(S.BUFFER_CODECS,this.onBufferCodecs,this),e.on(S.BUFFER_EOS,this.onBufferEos,this),e.on(S.BUFFER_FLUSHING,this.onBufferFlushing,this),e.on(S.LEVEL_UPDATED,this.onLevelUpdated,this),e.on(S.FRAG_PARSED,this.onFragParsed,this),e.on(S.FRAG_CHANGED,this.onFragChanged,this)},a.unregisterListeners=function(){var e=this.hls;e.off(S.MEDIA_ATTACHING,this.onMediaAttaching,this),e.off(S.MEDIA_DETACHING,this.onMediaDetaching,this),e.off(S.MANIFEST_PARSED,this.onManifestParsed,this),e.off(S.BUFFER_RESET,this.onBufferReset,this),e.off(S.BUFFER_APPENDING,this.onBufferAppending,this),e.off(S.BUFFER_CODECS,this.onBufferCodecs,this),e.off(S.BUFFER_EOS,this.onBufferEos,this),e.off(S.BUFFER_FLUSHING,this.onBufferFlushing,this),e.off(S.LEVEL_UPDATED,this.onLevelUpdated,this),e.off(S.FRAG_PARSED,this.onFragParsed,this),e.off(S.FRAG_CHANGED,this.onFragChanged,this)},a._initSourceBuffer=function(){this.sourceBuffer={},this.operationQueue=new ys(this.sourceBuffer),this.listeners={audio:[],video:[],audiovideo:[]},this.lastMpegAudioChunk=null},a.onManifestParsed=function(e,t){var n=2;(t.audio&&!t.video||!t.altAudio)&&(n=1),this.bufferCodecEventsExpected=this._bufferCodecEventsTotal=n,this.details=null,P.log(this.bufferCodecEventsExpected+" bufferCodec event(s) expected")},a.onMediaAttaching=function(e,t){var n=this.media=t.media;if(n&&yo){var i=this.mediaSource=new yo;i.addEventListener("sourceopen",this._onMediaSourceOpen),i.addEventListener("sourceended",this._onMediaSourceEnded),i.addEventListener("sourceclose",this._onMediaSourceClose),n.src=self.URL.createObjectURL(i),this._objectUrl=n.src,n.addEventListener("emptied",this._onMediaEmptied)}},a.onMediaDetaching=function(){var e=this.media,t=this.mediaSource,n=this._objectUrl;if(t){if(P.log("[buffer-controller]: media source detaching"),t.readyState==="open")try{t.endOfStream()}catch(i){P.warn("[buffer-controller]: onMediaDetaching: "+i.message+" while calling endOfStream")}this.onBufferReset(),t.removeEventListener("sourceopen",this._onMediaSourceOpen),t.removeEventListener("sourceended",this._onMediaSourceEnded),t.removeEventListener("sourceclose",this._onMediaSourceClose),e&&(e.removeEventListener("emptied",this._onMediaEmptied),n&&self.URL.revokeObjectURL(n),e.src===n?(e.removeAttribute("src"),e.load()):P.warn("[buffer-controller]: media.src was changed by a third party - skip cleanup")),this.mediaSource=null,this.media=null,this._objectUrl=null,this.bufferCodecEventsExpected=this._bufferCodecEventsTotal,this.pendingTracks={},this.tracks={}}this.hls.trigger(S.MEDIA_DETACHED,void 0)},a.onBufferReset=function(){var e=this;this.getSourceBufferTypes().forEach(function(t){var n=e.sourceBuffer[t];try{n&&(e.removeBufferListeners(t),e.mediaSource&&e.mediaSource.removeSourceBuffer(n),e.sourceBuffer[t]=void 0)}catch(i){P.warn("[buffer-controller]: Failed to reset the "+t+" buffer",i)}}),this._initSourceBuffer()},a.onBufferCodecs=function(e,t){var n=this,i=this.getSourceBufferTypes().length;Object.keys(t).forEach(function(o){if(i){var l=n.tracks[o];if(l&&typeof l.buffer.changeType=="function"){var d=t[o],u=d.id,c=d.codec,p=d.levelCodec,g=d.container,y=d.metadata,b=(l.levelCodec||l.codec).replace(Ao,"$1"),E=(p||c).replace(Ao,"$1");if(b!==E){var C=g+";codecs="+(p||c);n.appendChangeType(o,C),P.log("[buffer-controller]: switching codec "+b+" to "+E),n.tracks[o]={buffer:l.buffer,codec:c,container:g,levelCodec:p,metadata:y,id:u}}}}else n.pendingTracks[o]=t[o]}),!i&&(this.bufferCodecEventsExpected=Math.max(this.bufferCodecEventsExpected-1,0),this.mediaSource&&this.mediaSource.readyState==="open"&&this.checkPendingTracks())},a.appendChangeType=function(e,t){var n=this,i=this.operationQueue,o={execute:function(){var d=n.sourceBuffer[e];d&&(P.log("[buffer-controller]: changing "+e+" sourceBuffer type to "+t),d.changeType(t)),i.shiftAndExecuteNext(e)},onStart:function(){},onComplete:function(){},onError:function(d){P.warn("[buffer-controller]: Failed to change "+e+" SourceBuffer type",d)}};i.append(o,e)},a.onBufferAppending=function(e,t){var n=this,i=this.hls,o=this.operationQueue,l=this.tracks,d=t.data,u=t.type,c=t.frag,p=t.part,g=t.chunkMeta,y=g.buffering[u],b=self.performance.now();y.start=b;var E=c.stats.buffering,C=p?p.stats.buffering:null;E.start===0&&(E.start=b),C&&C.start===0&&(C.start=b);var w=l.audio,k=!1;u==="audio"&&(w==null?void 0:w.container)==="audio/mpeg"&&(k=!this.lastMpegAudioChunk||g.id===1||this.lastMpegAudioChunk.sn!==g.sn,this.lastMpegAudioChunk=g);var D=c.start,_={execute:function(){if(y.executeStart=self.performance.now(),k){var O=n.sourceBuffer[u];if(O){var M=D-O.timestampOffset;Math.abs(M)>=.1&&(P.log("[buffer-controller]: Updating audio SourceBuffer timestampOffset to "+D+" (delta: "+M+") sn: "+c.sn+")"),O.timestampOffset=D)}}n.appendExecutor(d,u)},onStart:function(){},onComplete:function(){var O=self.performance.now();y.executeEnd=y.end=O,E.first===0&&(E.first=O),C&&C.first===0&&(C.first=O);var M=n.sourceBuffer,z={};for(var H in M)z[H]=Fe.getBuffered(M[H]);n.appendError=0,n.hls.trigger(S.BUFFER_APPENDED,{type:u,frag:c,part:p,chunkMeta:g,parent:c.type,timeRanges:z})},onError:function(O){P.error("[buffer-controller]: Error encountered while trying to append to the "+u+" SourceBuffer",O);var M={type:fe.MEDIA_ERROR,parent:c.type,details:U.BUFFER_APPEND_ERROR,frag:c,part:p,chunkMeta:g,error:O,err:O,fatal:!1};O.code===DOMException.QUOTA_EXCEEDED_ERR?M.details=U.BUFFER_FULL_ERROR:(n.appendError++,M.details=U.BUFFER_APPEND_ERROR,n.appendError>i.config.appendErrorMaxRetry&&(P.error("[buffer-controller]: Failed "+i.config.appendErrorMaxRetry+" times to append segment in sourceBuffer"),M.fatal=!0)),i.trigger(S.ERROR,M)}};o.append(_,u)},a.onBufferFlushing=function(e,t){var n=this,i=this.operationQueue,o=function(d){return{execute:n.removeExecutor.bind(n,d,t.startOffset,t.endOffset),onStart:function(){},onComplete:function(){n.hls.trigger(S.BUFFER_FLUSHED,{type:d})},onError:function(c){P.warn("[buffer-controller]: Failed to remove from "+d+" SourceBuffer",c)}}};t.type?i.append(o(t.type),t.type):this.getSourceBufferTypes().forEach(function(l){i.append(o(l),l)})},a.onFragParsed=function(e,t){var n=this,i=t.frag,o=t.part,l=[],d=o?o.elementaryStreams:i.elementaryStreams;d[Ce.AUDIOVIDEO]?l.push("audiovideo"):(d[Ce.AUDIO]&&l.push("audio"),d[Ce.VIDEO]&&l.push("video"));var u=function(){var p=self.performance.now();i.stats.buffering.end=p,o&&(o.stats.buffering.end=p);var g=o?o.stats:i.stats;n.hls.trigger(S.FRAG_BUFFERED,{frag:i,part:o,stats:g,id:i.type})};l.length===0&&P.warn("Fragments must have at least one ElementaryStreamType set. type: "+i.type+" level: "+i.level+" sn: "+i.sn),this.blockBuffers(u,l)},a.onFragChanged=function(e,t){this.flushBackBuffer()},a.onBufferEos=function(e,t){var n=this,i=this.getSourceBufferTypes().reduce(function(o,l){var d=n.sourceBuffer[l];return d&&(!t.type||t.type===l)&&(d.ending=!0,d.ended||(d.ended=!0,P.log("[buffer-controller]: "+l+" sourceBuffer now EOS"))),o&&!!(!d||d.ended)},!0);i&&(P.log("[buffer-controller]: Queueing mediaSource.endOfStream()"),this.blockBuffers(function(){n.getSourceBufferTypes().forEach(function(l){var d=n.sourceBuffer[l];d&&(d.ending=!1)});var o=n.mediaSource;if(!o||o.readyState!=="open"){o&&P.info("[buffer-controller]: Could not call mediaSource.endOfStream(). mediaSource.readyState: "+o.readyState);return}P.log("[buffer-controller]: Calling mediaSource.endOfStream()"),o.endOfStream()}))},a.onLevelUpdated=function(e,t){var n=t.details;!n.fragments.length||(this.details=n,this.getSourceBufferTypes().length?this.blockBuffers(this.updateMediaElementDuration.bind(this)):this.updateMediaElementDuration())},a.flushBackBuffer=function(){var e=this.hls,t=this.details,n=this.media,i=this.sourceBuffer;if(!(!n||t===null)){var o=this.getSourceBufferTypes();if(!!o.length){var l=t.live&&e.config.liveBackBufferLength!==null?e.config.liveBackBufferLength:e.config.backBufferLength;if(!(!oe(l)||l<0)){var d=n.currentTime,u=t.levelTargetDuration,c=Math.max(l,u),p=Math.floor(d/u)*u-c;o.forEach(function(g){var y=i[g];if(y){var b=Fe.getBuffered(y);if(b.length>0&&p>b.start(0)){if(e.trigger(S.BACK_BUFFER_REACHED,{bufferEnd:p}),t.live)e.trigger(S.LIVE_BACK_BUFFER_REACHED,{bufferEnd:p});else if(y.ended&&b.end(b.length-1)-d<u*2){P.info("[buffer-controller]: Cannot flush "+g+" back buffer while SourceBuffer is in ended state");return}e.trigger(S.BUFFER_FLUSHING,{startOffset:0,endOffset:p,type:g})}}})}}}},a.updateMediaElementDuration=function(){if(!(!this.details||!this.media||!this.mediaSource||this.mediaSource.readyState!=="open")){var e=this.details,t=this.hls,n=this.media,i=this.mediaSource,o=e.fragments[0].start+e.totalduration,l=n.duration,d=oe(i.duration)?i.duration:0;e.live&&t.config.liveDurationInfinity?(P.log("[buffer-controller]: Media Source duration is set to Infinity"),i.duration=Infinity,this.updateSeekableRange(e)):(o>d&&o>l||!oe(l))&&(P.log("[buffer-controller]: Updating Media Source duration to "+o.toFixed(3)),i.duration=o)}},a.updateSeekableRange=function(e){var t=this.mediaSource,n=e.fragments,i=n.length;if(i&&e.live&&t!=null&&t.setLiveSeekableRange){var o=Math.max(0,n[0].start),l=Math.max(o,o+e.totalduration);t.setLiveSeekableRange(o,l)}},a.checkPendingTracks=function(){var e=this.bufferCodecEventsExpected,t=this.operationQueue,n=this.pendingTracks,i=Object.keys(n).length;if(i&&!e||i===2){this.createSourceBuffers(n),this.pendingTracks={};var o=this.getSourceBufferTypes();if(o.length)this.hls.trigger(S.BUFFER_CREATED,{tracks:this.tracks}),o.forEach(function(d){t.executeNext(d)});else{var l=new Error("could not create source buffer for media codec(s)");this.hls.trigger(S.ERROR,{type:fe.MEDIA_ERROR,details:U.BUFFER_INCOMPATIBLE_CODECS_ERROR,fatal:!0,error:l,reason:l.message})}}},a.createSourceBuffers=function(e){var t=this.sourceBuffer,n=this.mediaSource;if(!n)throw Error("createSourceBuffers called when mediaSource was null");for(var i in e)if(!t[i]){var o=e[i];if(!o)throw Error("source buffer exists for track "+i+", however track does not");var l=o.levelCodec||o.codec,d=o.container+";codecs="+l;P.log("[buffer-controller]: creating sourceBuffer("+d+")");try{var u=t[i]=n.addSourceBuffer(d),c=i;this.addBufferListener(c,"updatestart",this._onSBUpdateStart),this.addBufferListener(c,"updateend",this._onSBUpdateEnd),this.addBufferListener(c,"error",this._onSBUpdateError),this.tracks[i]={buffer:u,codec:l,container:o.container,levelCodec:o.levelCodec,metadata:o.metadata,id:o.id}}catch(p){P.error("[buffer-controller]: error while trying to add sourceBuffer: "+p.message),this.hls.trigger(S.ERROR,{type:fe.MEDIA_ERROR,details:U.BUFFER_ADD_CODEC_ERROR,fatal:!1,error:p,mimeType:d})}}},a._onSBUpdateStart=function(e){var t=this.operationQueue,n=t.current(e);n.onStart()},a._onSBUpdateEnd=function(e){var t=this.operationQueue,n=t.current(e);n.onComplete(),t.shiftAndExecuteNext(e)},a._onSBUpdateError=function(e,t){var n=new Error(e+" SourceBuffer error");P.error("[buffer-controller]: "+n,t),this.hls.trigger(S.ERROR,{type:fe.MEDIA_ERROR,details:U.BUFFER_APPENDING_ERROR,error:n,fatal:!1});var i=this.operationQueue.current(e);i&&i.onError(t)},a.removeExecutor=function(e,t,n){var i=this.media,o=this.mediaSource,l=this.operationQueue,d=this.sourceBuffer,u=d[e];if(!i||!o||!u){P.warn("[buffer-controller]: Attempting to remove from the "+e+" SourceBuffer, but it does not exist"),l.shiftAndExecuteNext(e);return}var c=oe(i.duration)?i.duration:Infinity,p=oe(o.duration)?o.duration:Infinity,g=Math.max(0,t),y=Math.min(n,c,p);y>g&&!u.ending?(u.ended=!1,P.log("[buffer-controller]: Removing ["+g+","+y+"] from the "+e+" SourceBuffer"),u.remove(g,y)):l.shiftAndExecuteNext(e)},a.appendExecutor=function(e,t){var n=this.operationQueue,i=this.sourceBuffer,o=i[t];if(!o){P.warn("[buffer-controller]: Attempting to append to the "+t+" SourceBuffer, but it does not exist"),n.shiftAndExecuteNext(t);return}o.ended=!1,o.appendBuffer(e)},a.blockBuffers=function(e,t){var n=this;if(t===void 0&&(t=this.getSourceBufferTypes()),!t.length){P.log("[buffer-controller]: Blocking operation requested, but no SourceBuffers exist"),Promise.resolve().then(e);return}var i=this.operationQueue,o=t.map(function(l){return i.appendBlocker(l)});Promise.all(o).then(function(){e(),t.forEach(function(l){var d=n.sourceBuffer[l];d!=null&&d.updating||i.shiftAndExecuteNext(l)})})},a.getSourceBufferTypes=function(){return Object.keys(this.sourceBuffer)},a.addBufferListener=function(e,t,n){var i=this.sourceBuffer[e];if(!!i){var o=n.bind(this,e);this.listeners[e].push({event:t,listener:o}),i.addEventListener(t,o)}},a.removeBufferListeners=function(e){var t=this.sourceBuffer[e];!t||this.listeners[e].forEach(function(n){t.removeEventListener(n.event,n.listener)})},s}(),bo={42:225,92:233,94:237,95:243,96:250,123:231,124:247,125:209,126:241,127:9608,128:174,129:176,130:189,131:191,132:8482,133:162,134:163,135:9834,136:224,137:32,138:232,139:226,140:234,141:238,142:244,143:251,144:193,145:201,146:211,147:218,148:220,149:252,150:8216,151:161,152:42,153:8217,154:9473,155:169,156:8480,157:8226,158:8220,159:8221,160:192,161:194,162:199,163:200,164:202,165:203,166:235,167:206,168:207,169:239,170:212,171:217,172:249,173:219,174:171,175:187,176:195,177:227,178:205,179:204,180:236,181:210,182:242,183:213,184:245,185:123,186:125,187:92,188:94,189:95,190:124,191:8764,192:196,193:228,194:214,195:246,196:223,197:165,198:164,199:9475,200:197,201:229,202:216,203:248,204:9487,205:9491,206:9495,207:9499},Eo=function(a){var r=a;return bo.hasOwnProperty(a)&&(r=bo[a]),String.fromCharCode(r)},pt=15,_t=100,bs={17:1,18:3,21:5,22:7,23:9,16:11,19:12,20:14},Es={17:2,18:4,21:6,22:8,23:10,19:13,20:15},xs={25:1,26:3,29:5,30:7,31:9,24:11,27:12,28:14},Ts={25:2,26:4,29:6,30:8,31:10,27:13,28:15},Ss=["white","green","blue","cyan","red","yellow","magenta","black","transparent"],Cs=function(){function s(){this.time=null,this.verboseLevel=0}var a=s.prototype;return a.log=function(e,t){if(this.verboseLevel>=e){var n=typeof t=="function"?t():t;P.log(this.time+" ["+e+"] "+n)}},s}(),Ln=function(a){for(var r=[],e=0;e<a.length;e++)r.push(a[e].toString(16));return r},xo=function(){function s(r,e,t,n,i){this.foreground=void 0,this.underline=void 0,this.italics=void 0,this.background=void 0,this.flash=void 0,this.foreground=r||"white",this.underline=e||!1,this.italics=t||!1,this.background=n||"black",this.flash=i||!1}var a=s.prototype;return a.reset=function(){this.foreground="white",this.underline=!1,this.italics=!1,this.background="black",this.flash=!1},a.setStyles=function(e){for(var t=["foreground","underline","italics","background","flash"],n=0;n<t.length;n++){var i=t[n];e.hasOwnProperty(i)&&(this[i]=e[i])}},a.isDefault=function(){return this.foreground==="white"&&!this.underline&&!this.italics&&this.background==="black"&&!this.flash},a.equals=function(e){return this.foreground===e.foreground&&this.underline===e.underline&&this.italics===e.italics&&this.background===e.background&&this.flash===e.flash},a.copy=function(e){this.foreground=e.foreground,this.underline=e.underline,this.italics=e.italics,this.background=e.background,this.flash=e.flash},a.toString=function(){return"color="+this.foreground+", underline="+this.underline+", italics="+this.italics+", background="+this.background+", flash="+this.flash},s}(),ks=function(){function s(r,e,t,n,i,o){this.uchar=void 0,this.penState=void 0,this.uchar=r||" ",this.penState=new xo(e,t,n,i,o)}var a=s.prototype;return a.reset=function(){this.uchar=" ",this.penState.reset()},a.setChar=function(e,t){this.uchar=e,this.penState.copy(t)},a.setPenState=function(e){this.penState.copy(e)},a.equals=function(e){return this.uchar===e.uchar&&this.penState.equals(e.penState)},a.copy=function(e){this.uchar=e.uchar,this.penState.copy(e.penState)},a.isEmpty=function(){return this.uchar===" "&&this.penState.isDefault()},s}(),ws=function(){function s(r){this.chars=void 0,this.pos=void 0,this.currPenState=void 0,this.cueStartTime=void 0,this.logger=void 0,this.chars=[];for(var e=0;e<_t;e++)this.chars.push(new ks);this.logger=r,this.pos=0,this.currPenState=new xo}var a=s.prototype;return a.equals=function(e){for(var t=!0,n=0;n<_t;n++)if(!this.chars[n].equals(e.chars[n])){t=!1;break}return t},a.copy=function(e){for(var t=0;t<_t;t++)this.chars[t].copy(e.chars[t])},a.isEmpty=function(){for(var e=!0,t=0;t<_t;t++)if(!this.chars[t].isEmpty()){e=!1;break}return e},a.setCursor=function(e){this.pos!==e&&(this.pos=e),this.pos<0?(this.logger.log(3,"Negative cursor position "+this.pos),this.pos=0):this.pos>_t&&(this.logger.log(3,"Too large cursor position "+this.pos),this.pos=_t)},a.moveCursor=function(e){var t=this.pos+e;if(e>1)for(var n=this.pos+1;n<t+1;n++)this.chars[n].setPenState(this.currPenState);this.setCursor(t)},a.backSpace=function(){this.moveCursor(-1),this.chars[this.pos].setChar(" ",this.currPenState)},a.insertChar=function(e){var t=this;e>=144&&this.backSpace();var n=Eo(e);if(this.pos>=_t){this.logger.log(0,function(){return"Cannot insert "+e.toString(16)+" ("+n+") at position "+t.pos+". Skipping it!"});return}this.chars[this.pos].setChar(n,this.currPenState),this.moveCursor(1)},a.clearFromPos=function(e){var t;for(t=e;t<_t;t++)this.chars[t].reset()},a.clear=function(){this.clearFromPos(0),this.pos=0,this.currPenState.reset()},a.clearToEndOfRow=function(){this.clearFromPos(this.pos)},a.getTextString=function(){for(var e=[],t=!0,n=0;n<_t;n++){var i=this.chars[n].uchar;i!==" "&&(t=!1),e.push(i)}return t?"":e.join("")},a.setPenStyles=function(e){this.currPenState.setStyles(e);var t=this.chars[this.pos];t.setPenState(this.currPenState)},s}(),Qa=function(){function s(r){this.rows=void 0,this.currRow=void 0,this.nrRollUpRows=void 0,this.lastOutputScreen=void 0,this.logger=void 0,this.rows=[];for(var e=0;e<pt;e++)this.rows.push(new ws(r));this.logger=r,this.currRow=pt-1,this.nrRollUpRows=null,this.lastOutputScreen=null,this.reset()}var a=s.prototype;return a.reset=function(){for(var e=0;e<pt;e++)this.rows[e].clear();this.currRow=pt-1},a.equals=function(e){for(var t=!0,n=0;n<pt;n++)if(!this.rows[n].equals(e.rows[n])){t=!1;break}return t},a.copy=function(e){for(var t=0;t<pt;t++)this.rows[t].copy(e.rows[t])},a.isEmpty=function(){for(var e=!0,t=0;t<pt;t++)if(!this.rows[t].isEmpty()){e=!1;break}return e},a.backSpace=function(){var e=this.rows[this.currRow];e.backSpace()},a.clearToEndOfRow=function(){var e=this.rows[this.currRow];e.clearToEndOfRow()},a.insertChar=function(e){var t=this.rows[this.currRow];t.insertChar(e)},a.setPen=function(e){var t=this.rows[this.currRow];t.setPenStyles(e)},a.moveCursor=function(e){var t=this.rows[this.currRow];t.moveCursor(e)},a.setCursor=function(e){this.logger.log(2,"setCursor: "+e);var t=this.rows[this.currRow];t.setCursor(e)},a.setPAC=function(e){this.logger.log(2,function(){return"pacData = "+JSON.stringify(e)});var t=e.row-1;if(this.nrRollUpRows&&t<this.nrRollUpRows-1&&(t=this.nrRollUpRows-1),this.nrRollUpRows&&this.currRow!==t){for(var n=0;n<pt;n++)this.rows[n].clear();var i=this.currRow+1-this.nrRollUpRows,o=this.lastOutputScreen;if(o){var l=o.rows[i].cueStartTime,d=this.logger.time;if(l&&d!==null&&l<d)for(var u=0;u<this.nrRollUpRows;u++)this.rows[t-this.nrRollUpRows+u+1].copy(o.rows[i+u])}}this.currRow=t;var c=this.rows[this.currRow];if(e.indent!==null){var p=e.indent,g=Math.max(p-1,0);c.setCursor(e.indent),e.color=c.chars[g].penState.foreground}var y={foreground:e.color,underline:e.underline,italics:e.italics,background:"black",flash:!1};this.setPen(y)},a.setBkgData=function(e){this.logger.log(2,function(){return"bkgData = "+JSON.stringify(e)}),this.backSpace(),this.setPen(e),this.insertChar(32)},a.setRollUpRows=function(e){this.nrRollUpRows=e},a.rollUp=function(){var e=this;if(this.nrRollUpRows===null){this.logger.log(3,"roll_up but nrRollUpRows not set yet");return}this.logger.log(1,function(){return e.getDisplayText()});var t=this.currRow+1-this.nrRollUpRows,n=this.rows.splice(t,1)[0];n.clear(),this.rows.splice(this.currRow,0,n),this.logger.log(2,"Rolling up")},a.getDisplayText=function(e){e=e||!1;for(var t=[],n="",i=-1,o=0;o<pt;o++){var l=this.rows[o].getTextString();l&&(i=o+1,e?t.push("Row "+i+": '"+l+"'"):t.push(l.trim()))}return t.length>0&&(e?n="["+t.join(" | ")+"]":n=t.join(`
`)),n},a.getTextAndFormat=function(){return this.rows},s}(),To=function(){function s(r,e,t){this.chNr=void 0,this.outputFilter=void 0,this.mode=void 0,this.verbose=void 0,this.displayedMemory=void 0,this.nonDisplayedMemory=void 0,this.lastOutputScreen=void 0,this.currRollUpRow=void 0,this.writeScreen=void 0,this.cueStartTime=void 0,this.logger=void 0,this.chNr=r,this.outputFilter=e,this.mode=null,this.verbose=0,this.displayedMemory=new Qa(t),this.nonDisplayedMemory=new Qa(t),this.lastOutputScreen=new Qa(t),this.currRollUpRow=this.displayedMemory.rows[pt-1],this.writeScreen=this.displayedMemory,this.mode=null,this.cueStartTime=null,this.logger=t}var a=s.prototype;return a.reset=function(){this.mode=null,this.displayedMemory.reset(),this.nonDisplayedMemory.reset(),this.lastOutputScreen.reset(),this.outputFilter.reset(),this.currRollUpRow=this.displayedMemory.rows[pt-1],this.writeScreen=this.displayedMemory,this.mode=null,this.cueStartTime=null},a.getHandler=function(){return this.outputFilter},a.setHandler=function(e){this.outputFilter=e},a.setPAC=function(e){this.writeScreen.setPAC(e)},a.setBkgData=function(e){this.writeScreen.setBkgData(e)},a.setMode=function(e){e!==this.mode&&(this.mode=e,this.logger.log(2,function(){return"MODE="+e}),this.mode==="MODE_POP-ON"?this.writeScreen=this.nonDisplayedMemory:(this.writeScreen=this.displayedMemory,this.writeScreen.reset()),this.mode!=="MODE_ROLL-UP"&&(this.displayedMemory.nrRollUpRows=null,this.nonDisplayedMemory.nrRollUpRows=null),this.mode=e)},a.insertChars=function(e){for(var t=this,n=0;n<e.length;n++)this.writeScreen.insertChar(e[n]);var i=this.writeScreen===this.displayedMemory?"DISP":"NON_DISP";this.logger.log(2,function(){return i+": "+t.writeScreen.getDisplayText(!0)}),(this.mode==="MODE_PAINT-ON"||this.mode==="MODE_ROLL-UP")&&(this.logger.log(1,function(){return"DISPLAYED: "+t.displayedMemory.getDisplayText(!0)}),this.outputDataUpdate())},a.ccRCL=function(){this.logger.log(2,"RCL - Resume Caption Loading"),this.setMode("MODE_POP-ON")},a.ccBS=function(){this.logger.log(2,"BS - BackSpace"),this.mode!=="MODE_TEXT"&&(this.writeScreen.backSpace(),this.writeScreen===this.displayedMemory&&this.outputDataUpdate())},a.ccAOF=function(){},a.ccAON=function(){},a.ccDER=function(){this.logger.log(2,"DER- Delete to End of Row"),this.writeScreen.clearToEndOfRow(),this.outputDataUpdate()},a.ccRU=function(e){this.logger.log(2,"RU("+e+") - Roll Up"),this.writeScreen=this.displayedMemory,this.setMode("MODE_ROLL-UP"),this.writeScreen.setRollUpRows(e)},a.ccFON=function(){this.logger.log(2,"FON - Flash On"),this.writeScreen.setPen({flash:!0})},a.ccRDC=function(){this.logger.log(2,"RDC - Resume Direct Captioning"),this.setMode("MODE_PAINT-ON")},a.ccTR=function(){this.logger.log(2,"TR"),this.setMode("MODE_TEXT")},a.ccRTD=function(){this.logger.log(2,"RTD"),this.setMode("MODE_TEXT")},a.ccEDM=function(){this.logger.log(2,"EDM - Erase Displayed Memory"),this.displayedMemory.reset(),this.outputDataUpdate(!0)},a.ccCR=function(){this.logger.log(2,"CR - Carriage Return"),this.writeScreen.rollUp(),this.outputDataUpdate(!0)},a.ccENM=function(){this.logger.log(2,"ENM - Erase Non-displayed Memory"),this.nonDisplayedMemory.reset()},a.ccEOC=function(){var e=this;if(this.logger.log(2,"EOC - End Of Caption"),this.mode==="MODE_POP-ON"){var t=this.displayedMemory;this.displayedMemory=this.nonDisplayedMemory,this.nonDisplayedMemory=t,this.writeScreen=this.nonDisplayedMemory,this.logger.log(1,function(){return"DISP: "+e.displayedMemory.getDisplayText()})}this.outputDataUpdate(!0)},a.ccTO=function(e){this.logger.log(2,"TO("+e+") - Tab Offset"),this.writeScreen.moveCursor(e)},a.ccMIDROW=function(e){var t={flash:!1};if(t.underline=e%2==1,t.italics=e>=46,t.italics)t.foreground="white";else{var n=Math.floor(e/2)-16,i=["white","green","blue","cyan","red","yellow","magenta"];t.foreground=i[n]}this.logger.log(2,"MIDROW: "+JSON.stringify(t)),this.writeScreen.setPen(t)},a.outputDataUpdate=function(e){e===void 0&&(e=!1);var t=this.logger.time;t!==null&&this.outputFilter&&(this.cueStartTime===null&&!this.displayedMemory.isEmpty()?this.cueStartTime=t:this.displayedMemory.equals(this.lastOutputScreen)||(this.outputFilter.newCue(this.cueStartTime,t,this.lastOutputScreen),e&&this.outputFilter.dispatchCue&&this.outputFilter.dispatchCue(),this.cueStartTime=this.displayedMemory.isEmpty()?null:t),this.lastOutputScreen.copy(this.displayedMemory))},a.cueSplitAtTime=function(e){this.outputFilter&&(this.displayedMemory.isEmpty()||(this.outputFilter.newCue&&this.outputFilter.newCue(this.cueStartTime,e,this.displayedMemory),this.cueStartTime=e))},s}(),So=function(){function s(r,e,t){this.channels=void 0,this.currentChannel=0,this.cmdHistory=void 0,this.logger=void 0;var n=new Cs;this.channels=[null,new To(r,e,n),new To(r+1,t,n)],this.cmdHistory=ko(),this.logger=n}var a=s.prototype;return a.getHandler=function(e){return this.channels[e].getHandler()},a.setHandler=function(e,t){this.channels[e].setHandler(t)},a.addData=function(e,t){var n,i,o,l=!1;this.logger.time=e;for(var d=0;d<t.length;d+=2)if(i=t[d]&127,o=t[d+1]&127,!(i===0&&o===0)){if(this.logger.log(3,"["+Ln([t[d],t[d+1]])+"] -> ("+Ln([i,o])+")"),n=this.parseCmd(i,o),n||(n=this.parseMidrow(i,o)),n||(n=this.parsePAC(i,o)),n||(n=this.parseBackgroundAttributes(i,o)),!n&&(l=this.parseChars(i,o),l)){var u=this.currentChannel;if(u&&u>0){var c=this.channels[u];c.insertChars(l)}else this.logger.log(2,"No channel found yet. TEXT-MODE?")}!n&&!l&&this.logger.log(2,"Couldn't parse cleaned data "+Ln([i,o])+" orig: "+Ln([t[d],t[d+1]]))}},a.parseCmd=function(e,t){var n=this.cmdHistory,i=(e===20||e===28||e===21||e===29)&&t>=32&&t<=47,o=(e===23||e===31)&&t>=33&&t<=35;if(!(i||o))return!1;if(Co(e,t,n))return tr(null,null,n),this.logger.log(3,"Repeated command ("+Ln([e,t])+") is dropped"),!0;var l=e===20||e===21||e===23?1:2,d=this.channels[l];return e===20||e===21||e===28||e===29?t===32?d.ccRCL():t===33?d.ccBS():t===34?d.ccAOF():t===35?d.ccAON():t===36?d.ccDER():t===37?d.ccRU(2):t===38?d.ccRU(3):t===39?d.ccRU(4):t===40?d.ccFON():t===41?d.ccRDC():t===42?d.ccTR():t===43?d.ccRTD():t===44?d.ccEDM():t===45?d.ccCR():t===46?d.ccENM():t===47&&d.ccEOC():d.ccTO(t-32),tr(e,t,n),this.currentChannel=l,!0},a.parseMidrow=function(e,t){var n=0;if((e===17||e===25)&&t>=32&&t<=47){if(e===17?n=1:n=2,n!==this.currentChannel)return this.logger.log(0,"Mismatch channel in midrow parsing"),!1;var i=this.channels[n];return i?(i.ccMIDROW(t),this.logger.log(3,"MIDROW ("+Ln([e,t])+")"),!0):!1}return!1},a.parsePAC=function(e,t){var n,i=this.cmdHistory,o=(e>=17&&e<=23||e>=25&&e<=31)&&t>=64&&t<=127,l=(e===16||e===24)&&t>=64&&t<=95;if(!(o||l))return!1;if(Co(e,t,i))return tr(null,null,i),!0;var d=e<=23?1:2;t>=64&&t<=95?n=d===1?bs[e]:xs[e]:n=d===1?Es[e]:Ts[e];var u=this.channels[d];return u?(u.setPAC(this.interpretPAC(n,t)),tr(e,t,i),this.currentChannel=d,!0):!1},a.interpretPAC=function(e,t){var n,i={color:null,italics:!1,indent:null,underline:!1,row:e};return t>95?n=t-96:n=t-64,i.underline=(n&1)==1,n<=13?i.color=["white","green","blue","cyan","red","yellow","magenta","white"][Math.floor(n/2)]:n<=15?(i.italics=!0,i.color="white"):i.indent=Math.floor((n-16)/2)*4,i},a.parseChars=function(e,t){var n,i=null,o=null;if(e>=25?(n=2,o=e-8):(n=1,o=e),o>=17&&o<=19){var l;o===17?l=t+80:o===18?l=t+112:l=t+144,this.logger.log(2,"Special char '"+Eo(l)+"' in channel "+n),i=[l]}else e>=32&&e<=127&&(i=t===0?[e]:[e,t]);if(i){var d=Ln(i);this.logger.log(3,"Char codes =  "+d.join(",")),tr(e,t,this.cmdHistory)}return i},a.parseBackgroundAttributes=function(e,t){var n=(e===16||e===24)&&t>=32&&t<=47,i=(e===23||e===31)&&t>=45&&t<=47;if(!(n||i))return!1;var o,l={};e===16||e===24?(o=Math.floor((t-32)/2),l.background=Ss[o],t%2==1&&(l.background=l.background+"_semi")):t===45?l.background="transparent":(l.foreground="black",t===47&&(l.underline=!0));var d=e<=23?1:2,u=this.channels[d];return u.setBkgData(l),tr(e,t,this.cmdHistory),!0},a.reset=function(){for(var e=0;e<Object.keys(this.channels).length;e++){var t=this.channels[e];t&&t.reset()}this.cmdHistory=ko()},a.cueSplitAtTime=function(e){for(var t=0;t<this.channels.length;t++){var n=this.channels[t];n&&n.cueSplitAtTime(e)}},s}();function tr(s,a,r){r.a=s,r.b=a}function Co(s,a,r){return r.a===s&&r.b===a}function ko(){return{a:null,b:null}}var _i=function(){function s(r,e){this.timelineController=void 0,this.cueRanges=[],this.trackName=void 0,this.startTime=null,this.endTime=null,this.screen=null,this.timelineController=r,this.trackName=e}var a=s.prototype;return a.dispatchCue=function(){this.startTime!==null&&(this.timelineController.addCues(this.trackName,this.startTime,this.endTime,this.screen,this.cueRanges),this.startTime=null)},a.newCue=function(e,t,n){(this.startTime===null||this.startTime>e)&&(this.startTime=e),this.endTime=t,this.screen=n,this.timelineController.createCaptionsTrack(this.trackName)},a.reset=function(){this.cueRanges=[],this.startTime=null},s}(),Xa=function(){if(typeof self!="undefined"&&self.VTTCue)return self.VTTCue;var s=["","lr","rl"],a=["start","middle","end","left","right"];function r(o,l){if(typeof l!="string"||!Array.isArray(o))return!1;var d=l.toLowerCase();return~o.indexOf(d)?d:!1}function e(o){return r(s,o)}function t(o){return r(a,o)}function n(o){for(var l=arguments.length,d=new Array(l>1?l-1:0),u=1;u<l;u++)d[u-1]=arguments[u];for(var c=1;c<arguments.length;c++){var p=arguments[c];for(var g in p)o[g]=p[g]}return o}function i(o,l,d){var u=this,c={enumerable:!0};u.hasBeenReset=!1;var p="",g=!1,y=o,b=l,E=d,C=null,w="",k=!0,D="auto",_="start",F=50,O="middle",M=50,z="middle";Object.defineProperty(u,"id",n({},c,{get:function(){return p},set:function(G){p=""+G}})),Object.defineProperty(u,"pauseOnExit",n({},c,{get:function(){return g},set:function(G){g=!!G}})),Object.defineProperty(u,"startTime",n({},c,{get:function(){return y},set:function(G){if(typeof G!="number")throw new TypeError("Start time must be set to a number.");y=G,this.hasBeenReset=!0}})),Object.defineProperty(u,"endTime",n({},c,{get:function(){return b},set:function(G){if(typeof G!="number")throw new TypeError("End time must be set to a number.");b=G,this.hasBeenReset=!0}})),Object.defineProperty(u,"text",n({},c,{get:function(){return E},set:function(G){E=""+G,this.hasBeenReset=!0}})),Object.defineProperty(u,"region",n({},c,{get:function(){return C},set:function(G){C=G,this.hasBeenReset=!0}})),Object.defineProperty(u,"vertical",n({},c,{get:function(){return w},set:function(G){var te=e(G);if(te===!1)throw new SyntaxError("An invalid or illegal string was specified.");w=te,this.hasBeenReset=!0}})),Object.defineProperty(u,"snapToLines",n({},c,{get:function(){return k},set:function(G){k=!!G,this.hasBeenReset=!0}})),Object.defineProperty(u,"line",n({},c,{get:function(){return D},set:function(G){if(typeof G!="number"&&G!=="auto")throw new SyntaxError("An invalid number or illegal string was specified.");D=G,this.hasBeenReset=!0}})),Object.defineProperty(u,"lineAlign",n({},c,{get:function(){return _},set:function(G){var te=t(G);if(!te)throw new SyntaxError("An invalid or illegal string was specified.");_=te,this.hasBeenReset=!0}})),Object.defineProperty(u,"position",n({},c,{get:function(){return F},set:function(G){if(G<0||G>100)throw new Error("Position must be between 0 and 100.");F=G,this.hasBeenReset=!0}})),Object.defineProperty(u,"positionAlign",n({},c,{get:function(){return O},set:function(G){var te=t(G);if(!te)throw new SyntaxError("An invalid or illegal string was specified.");O=te,this.hasBeenReset=!0}})),Object.defineProperty(u,"size",n({},c,{get:function(){return M},set:function(G){if(G<0||G>100)throw new Error("Size must be between 0 and 100.");M=G,this.hasBeenReset=!0}})),Object.defineProperty(u,"align",n({},c,{get:function(){return z},set:function(G){var te=t(G);if(!te)throw new SyntaxError("An invalid or illegal string was specified.");z=te,this.hasBeenReset=!0}})),u.displayState=void 0}return i.prototype.getCueAsHTML=function(){var o=self.WebVTT;return o.convertCueToDOMTree(self,this.text)},i}(),Ls=function(){function s(){}var a=s.prototype;return a.decode=function(e,t){if(!e)return"";if(typeof e!="string")throw new Error("Error - expected string data.");return decodeURIComponent(encodeURIComponent(e))},s}();function wo(s){function a(e,t,n,i){return(e|0)*3600+(t|0)*60+(n|0)+parseFloat(i||0)}var r=s.match(/^(?:(\d+):)?(\d{2}):(\d{2})(\.\d+)?/);return r?parseFloat(r[2])>59?a(r[2],r[3],0,r[4]):a(r[1],r[2],r[3],r[4]):null}var Is=function(){function s(){this.values=Object.create(null)}var a=s.prototype;return a.set=function(e,t){!this.get(e)&&t!==""&&(this.values[e]=t)},a.get=function(e,t,n){return n?this.has(e)?this.values[e]:t[n]:this.has(e)?this.values[e]:t},a.has=function(e){return e in this.values},a.alt=function(e,t,n){for(var i=0;i<n.length;++i)if(t===n[i]){this.set(e,t);break}},a.integer=function(e,t){/^-?\d+$/.test(t)&&this.set(e,parseInt(t,10))},a.percent=function(e,t){if(/^([\d]{1,3})(\.[\d]*)?%$/.test(t)){var n=parseFloat(t);if(n>=0&&n<=100)return this.set(e,n),!0}return!1},s}();function Lo(s,a,r,e){var t=e?s.split(e):[s];for(var n in t)if(typeof t[n]=="string"){var i=t[n].split(r);if(i.length===2){var o=i[0],l=i[1];a(o,l)}}}var Za=new Xa(0,0,""),Pi=Za.align==="middle"?"middle":"center";function Rs(s,a,r){var e=s;function t(){var o=wo(s);if(o===null)throw new Error("Malformed timestamp: "+e);return s=s.replace(/^[^\sa-zA-Z-]+/,""),o}function n(o,l){var d=new Is;Lo(o,function(p,g){var y;switch(p){case"region":for(var b=r.length-1;b>=0;b--)if(r[b].id===g){d.set(p,r[b].region);break}break;case"vertical":d.alt(p,g,["rl","lr"]);break;case"line":y=g.split(","),d.integer(p,y[0]),d.percent(p,y[0])&&d.set("snapToLines",!1),d.alt(p,y[0],["auto"]),y.length===2&&d.alt("lineAlign",y[1],["start",Pi,"end"]);break;case"position":y=g.split(","),d.percent(p,y[0]),y.length===2&&d.alt("positionAlign",y[1],["start",Pi,"end","line-left","line-right","auto"]);break;case"size":d.percent(p,g);break;case"align":d.alt(p,g,["start",Pi,"end","left","right"]);break}},/:/,/\s/),l.region=d.get("region",null),l.vertical=d.get("vertical","");var u=d.get("line","auto");u==="auto"&&Za.line===-1&&(u=-1),l.line=u,l.lineAlign=d.get("lineAlign","start"),l.snapToLines=d.get("snapToLines",!0),l.size=d.get("size",100),l.align=d.get("align",Pi);var c=d.get("position","auto");c==="auto"&&Za.position===50&&(c=l.align==="start"||l.align==="left"?0:l.align==="end"||l.align==="right"?100:50),l.position=c}function i(){s=s.replace(/^\s+/,"")}if(i(),a.startTime=t(),i(),s.slice(0,3)!=="-->")throw new Error("Malformed time stamp (time stamps must be separated by '-->'): "+e);s=s.slice(3),i(),a.endTime=t(),i(),n(s,a)}function Io(s){return s.replace(/<br(?: \/)?>/gi,`
`)}var Ds=function(){function s(){this.state="INITIAL",this.buffer="",this.decoder=new Ls,this.regionList=[],this.cue=null,this.oncue=void 0,this.onparsingerror=void 0,this.onflush=void 0}var a=s.prototype;return a.parse=function(e){var t=this;e&&(t.buffer+=t.decoder.decode(e,{stream:!0}));function n(){var c=t.buffer,p=0;for(c=Io(c);p<c.length&&c[p]!=="\r"&&c[p]!==`
`;)++p;var g=c.slice(0,p);return c[p]==="\r"&&++p,c[p]===`
`&&++p,t.buffer=c.slice(p),g}function i(c){Lo(c,function(p,g){},/:/)}try{var o="";if(t.state==="INITIAL"){if(!/\r\n|\n/.test(t.buffer))return this;o=n();var l=o.match(/^(ï»¿)?WEBVTT([ \t].*)?$/);if(!(l!=null&&l[0]))throw new Error("Malformed WebVTT signature.");t.state="HEADER"}for(var d=!1;t.buffer;){if(!/\r\n|\n/.test(t.buffer))return this;switch(d?d=!1:o=n(),t.state){case"HEADER":/:/.test(o)?i(o):o||(t.state="ID");continue;case"NOTE":o||(t.state="ID");continue;case"ID":if(/^NOTE($|[ \t])/.test(o)){t.state="NOTE";break}if(!o)continue;if(t.cue=new Xa(0,0,""),t.state="CUE",o.indexOf("-->")===-1){t.cue.id=o;continue}case"CUE":if(!t.cue){t.state="BADCUE";continue}try{Rs(o,t.cue,t.regionList)}catch(c){t.cue=null,t.state="BADCUE";continue}t.state="CUETEXT";continue;case"CUETEXT":{var u=o.indexOf("-->")!==-1;if(!o||u&&(d=!0)){t.oncue&&t.cue&&t.oncue(t.cue),t.cue=null,t.state="ID";continue}if(t.cue===null)continue;t.cue.text&&(t.cue.text+=`
`),t.cue.text+=o}continue;case"BADCUE":o||(t.state="ID")}}}catch(c){t.state==="CUETEXT"&&t.cue&&t.oncue&&t.oncue(t.cue),t.cue=null,t.state=t.state==="INITIAL"?"BADWEBVTT":"BADCUE"}return this},a.flush=function(){var e=this;try{if((e.cue||e.state==="HEADER")&&(e.buffer+=`

`,e.parse()),e.state==="INITIAL"||e.state==="BADWEBVTT")throw new Error("Malformed WebVTT signature.")}catch(t){e.onparsingerror&&e.onparsingerror(t)}return e.onflush&&e.onflush(),this},s}(),_s=/\r\n|\n\r|\n|\r/g,Ja=function(a,r,e){return e===void 0&&(e=0),a.slice(e,e+r.length)===r},Ps=function(a){var r=parseInt(a.slice(-3)),e=parseInt(a.slice(-6,-4)),t=parseInt(a.slice(-9,-7)),n=a.length>9?parseInt(a.substring(0,a.indexOf(":"))):0;if(!oe(r)||!oe(e)||!oe(t)||!oe(n))throw Error("Malformed X-TIMESTAMP-MAP: Local:"+a);return r+=1e3*e,r+=60*1e3*t,r+=60*60*1e3*n,r},$a=function(a){for(var r=5381,e=a.length;e;)r=r*33^a.charCodeAt(--e);return(r>>>0).toString()};function eo(s,a,r){return $a(s.toString())+$a(a.toString())+$a(r)}var Bs=function(a,r,e){var t=a[r],n=a[t.prevCC];if(!n||!n.new&&t.new){a.ccOffset=a.presentationOffset=t.start,t.new=!1;return}for(;(i=n)!=null&&i.new;){var i;a.ccOffset+=t.start-n.start,t.new=!1,t=n,n=a[t.prevCC]}a.presentationOffset=e};function Os(s,a,r,e,t,n,i){var o=new Ds,l=rt(new Uint8Array(s)).trim().replace(_s,`
`).split(`
`),d=[],u=I(a.baseTime,a.timescale),c="00:00.000",p=0,g=0,y,b=!0;o.oncue=function(E){var C=r[e],w=r.ccOffset,k=(p-u)/9e4;C!=null&&C.new&&(g!==void 0?w=r.ccOffset=C.start:Bs(r,e,k)),k&&(w=k-r.presentationOffset);var D=E.endTime-E.startTime,_=W((E.startTime+w-g)*9e4,t*9e4)/9e4;E.startTime=Math.max(_,0),E.endTime=Math.max(_+D,0);var F=E.text.trim();E.text=decodeURIComponent(encodeURIComponent(F)),E.id||(E.id=eo(E.startTime,E.endTime,F)),E.endTime>0&&d.push(E)},o.onparsingerror=function(E){y=E},o.onflush=function(){if(y){i(y);return}n(d)},l.forEach(function(E){if(b)if(Ja(E,"X-TIMESTAMP-MAP=")){b=!1,E.slice(16).split(",").forEach(function(C){Ja(C,"LOCAL:")?c=C.slice(6):Ja(C,"MPEGTS:")&&(p=parseInt(C.slice(7)))});try{g=Ps(c)/1e3}catch(C){y=C}return}else E===""&&(b=!1);o.parse(E+`
`)}),o.flush()}var to="stpp.ttml.im1t",Ro=/^(\d{2,}):(\d{2}):(\d{2}):(\d{2})\.?(\d+)?$/,Do=/^(\d*(?:\.\d*)?)(h|m|s|ms|f|t)$/,Ms={left:"start",center:"center",right:"end",start:"start",end:"end"};function _o(s,a,r,e){var t=Te(new Uint8Array(s),["mdat"]);if(t.length===0){e(new Error("Could not parse IMSC1 mdat"));return}var n=t.map(function(o){return rt(o)}),i=T(a.baseTime,1,a.timescale);try{n.forEach(function(o){return r(Fs(o,i))})}catch(o){e(o)}}function Fs(s,a){var r=new DOMParser,e=r.parseFromString(s,"text/xml"),t=e.getElementsByTagName("tt")[0];if(!t)throw new Error("Invalid ttml");var n={frameRate:30,subFrameRate:1,frameRateMultiplier:0,tickRate:0},i=Object.keys(n).reduce(function(c,p){return c[p]=t.getAttribute("ttp:"+p)||n[p],c},{}),o=t.getAttribute("xml:space")!=="preserve",l=Po(no(t,"styling","style")),d=Po(no(t,"layout","region")),u=no(t,"body","[begin]");return[].map.call(u,function(c){var p=Bo(c,o);if(!p||!c.hasAttribute("begin"))return null;var g=io(c.getAttribute("begin"),i),y=io(c.getAttribute("dur"),i),b=io(c.getAttribute("end"),i);if(g===null)throw Oo(c);if(b===null){if(y===null)throw Oo(c);b=g+y}var E=new Xa(g-a,b-a,p);E.id=eo(E.startTime,E.endTime,E.text);var C=d[c.getAttribute("region")],w=l[c.getAttribute("style")],k=Ns(C,w,l),D=k.textAlign;if(D){var _=Ms[D];_&&(E.lineAlign=_),E.align=D}return q(E,k),E}).filter(function(c){return c!==null})}function no(s,a,r){var e=s.getElementsByTagName(a)[0];return e?[].slice.call(e.querySelectorAll(r)):[]}function Po(s){return s.reduce(function(a,r){var e=r.getAttribute("xml:id");return e&&(a[e]=r),a},{})}function Bo(s,a){return[].slice.call(s.childNodes).reduce(function(r,e,t){var n;return e.nodeName==="br"&&t?r+`
`:(n=e.childNodes)!=null&&n.length?Bo(e,a):a?r+e.textContent.trim().replace(/\s+/g," "):r+e.textContent},"")}function Ns(s,a,r){var e="http://www.w3.org/ns/ttml#styling",t=null,n=["displayAlign","textAlign","color","backgroundColor","fontSize","fontFamily"],i=s!=null&&s.hasAttribute("style")?s.getAttribute("style"):null;return i&&r.hasOwnProperty(i)&&(t=r[i]),n.reduce(function(o,l){var d=ro(a,e,l)||ro(s,e,l)||ro(t,e,l);return d&&(o[l]=d),o},{})}function ro(s,a,r){return s&&s.hasAttributeNS(a,r)?s.getAttributeNS(a,r):null}function Oo(s){return new Error("Could not parse ttml timestamp "+s)}function io(s,a){if(!s)return null;var r=wo(s);return r===null&&(Ro.test(s)?r=Us(s,a):Do.test(s)&&(r=Ks(s,a))),r}function Us(s,a){var r=Ro.exec(s),e=(r[4]|0)+(r[5]|0)/a.subFrameRate;return(r[1]|0)*3600+(r[2]|0)*60+(r[3]|0)+e/a.frameRate}function Ks(s,a){var r=Do.exec(s),e=Number(r[1]),t=r[2];switch(t){case"h":return e*3600;case"m":return e*60;case"ms":return e*1e3;case"f":return e/a.frameRate;case"t":return e/a.tickRate}return e}var Gs=function(){function s(r){if(this.hls=void 0,this.media=null,this.config=void 0,this.enabled=!0,this.Cues=void 0,this.textTracks=[],this.tracks=[],this.initPTS=[],this.unparsedVttFrags=[],this.captionsTracks={},this.nonNativeCaptionsTracks={},this.cea608Parser1=void 0,this.cea608Parser2=void 0,this.lastSn=-1,this.lastPartIndex=-1,this.prevCC=-1,this.vttCCs=Mo(),this.captionsProperties=void 0,this.hls=r,this.config=r.config,this.Cues=r.config.cueHandler,this.captionsProperties={textTrack1:{label:this.config.captionsTextTrack1Label,languageCode:this.config.captionsTextTrack1LanguageCode},textTrack2:{label:this.config.captionsTextTrack2Label,languageCode:this.config.captionsTextTrack2LanguageCode},textTrack3:{label:this.config.captionsTextTrack3Label,languageCode:this.config.captionsTextTrack3LanguageCode},textTrack4:{label:this.config.captionsTextTrack4Label,languageCode:this.config.captionsTextTrack4LanguageCode}},this.config.enableCEA708Captions){var e=new _i(this,"textTrack1"),t=new _i(this,"textTrack2"),n=new _i(this,"textTrack3"),i=new _i(this,"textTrack4");this.cea608Parser1=new So(1,e,t),this.cea608Parser2=new So(3,n,i)}r.on(S.MEDIA_ATTACHING,this.onMediaAttaching,this),r.on(S.MEDIA_DETACHING,this.onMediaDetaching,this),r.on(S.MANIFEST_LOADING,this.onManifestLoading,this),r.on(S.MANIFEST_LOADED,this.onManifestLoaded,this),r.on(S.SUBTITLE_TRACKS_UPDATED,this.onSubtitleTracksUpdated,this),r.on(S.FRAG_LOADING,this.onFragLoading,this),r.on(S.FRAG_LOADED,this.onFragLoaded,this),r.on(S.FRAG_PARSING_USERDATA,this.onFragParsingUserdata,this),r.on(S.FRAG_DECRYPTED,this.onFragDecrypted,this),r.on(S.INIT_PTS_FOUND,this.onInitPtsFound,this),r.on(S.SUBTITLE_TRACKS_CLEARED,this.onSubtitleTracksCleared,this),r.on(S.BUFFER_FLUSHING,this.onBufferFlushing,this)}var a=s.prototype;return a.destroy=function(){var e=this.hls;e.off(S.MEDIA_ATTACHING,this.onMediaAttaching,this),e.off(S.MEDIA_DETACHING,this.onMediaDetaching,this),e.off(S.MANIFEST_LOADING,this.onManifestLoading,this),e.off(S.MANIFEST_LOADED,this.onManifestLoaded,this),e.off(S.SUBTITLE_TRACKS_UPDATED,this.onSubtitleTracksUpdated,this),e.off(S.FRAG_LOADING,this.onFragLoading,this),e.off(S.FRAG_LOADED,this.onFragLoaded,this),e.off(S.FRAG_PARSING_USERDATA,this.onFragParsingUserdata,this),e.off(S.FRAG_DECRYPTED,this.onFragDecrypted,this),e.off(S.INIT_PTS_FOUND,this.onInitPtsFound,this),e.off(S.SUBTITLE_TRACKS_CLEARED,this.onSubtitleTracksCleared,this),e.off(S.BUFFER_FLUSHING,this.onBufferFlushing,this),this.hls=this.config=this.cea608Parser1=this.cea608Parser2=null},a.addCues=function(e,t,n,i,o){for(var l=!1,d=o.length;d--;){var u=o[d],c=qs(u[0],u[1],t,n);if(c>=0&&(u[0]=Math.min(u[0],t),u[1]=Math.max(u[1],n),l=!0,c/(n-t)>.5))return}if(l||o.push([t,n]),this.config.renderTextTracksNatively){var p=this.captionsTracks[e];this.Cues.newCue(p,t,n,i)}else{var g=this.Cues.newCue(null,t,n,i);this.hls.trigger(S.CUES_PARSED,{type:"captions",cues:g,track:e})}},a.onInitPtsFound=function(e,t){var n=this,i=t.frag,o=t.id,l=t.initPTS,d=t.timescale,u=this.unparsedVttFrags;o==="main"&&(this.initPTS[i.cc]={baseTime:l,timescale:d}),u.length&&(this.unparsedVttFrags=[],u.forEach(function(c){n.onFragLoaded(S.FRAG_LOADED,c)}))},a.getExistingTrack=function(e){var t=this.media;if(t)for(var n=0;n<t.textTracks.length;n++){var i=t.textTracks[n];if(i[e])return i}return null},a.createCaptionsTrack=function(e){this.config.renderTextTracksNatively?this.createNativeTrack(e):this.createNonNativeTrack(e)},a.createNativeTrack=function(e){if(!this.captionsTracks[e]){var t=this.captionsProperties,n=this.captionsTracks,i=this.media,o=t[e],l=o.label,d=o.languageCode,u=this.getExistingTrack(e);if(u)n[e]=u,Pe(n[e]),qn(n[e],i);else{var c=this.createTextTrack("captions",l,d);c&&(c[e]=!0,n[e]=c)}}},a.createNonNativeTrack=function(e){if(!this.nonNativeCaptionsTracks[e]){var t=this.captionsProperties[e];if(!!t){var n=t.label,i={_id:e,label:n,kind:"captions",default:t.media?!!t.media.default:!1,closedCaptions:t.media};this.nonNativeCaptionsTracks[e]=i,this.hls.trigger(S.NON_NATIVE_TEXT_TRACKS_FOUND,{tracks:[i]})}}},a.createTextTrack=function(e,t,n){var i=this.media;if(!!i)return i.addTextTrack(e,t,n)},a.onMediaAttaching=function(e,t){this.media=t.media,this._cleanTracks()},a.onMediaDetaching=function(){var e=this.captionsTracks;Object.keys(e).forEach(function(t){Pe(e[t]),delete e[t]}),this.nonNativeCaptionsTracks={}},a.onManifestLoading=function(){this.lastSn=-1,this.lastPartIndex=-1,this.prevCC=-1,this.vttCCs=Mo(),this._cleanTracks(),this.tracks=[],this.captionsTracks={},this.nonNativeCaptionsTracks={},this.textTracks=[],this.unparsedVttFrags=this.unparsedVttFrags||[],this.initPTS=[],this.cea608Parser1&&this.cea608Parser2&&(this.cea608Parser1.reset(),this.cea608Parser2.reset())},a._cleanTracks=function(){var e=this.media;if(!!e){var t=e.textTracks;if(t)for(var n=0;n<t.length;n++)Pe(t[n])}},a.onSubtitleTracksUpdated=function(e,t){var n=this,i=t.subtitleTracks||[],o=i.some(function(c){return c.textCodec===to});if(this.config.enableWebVTT||o&&this.config.enableIMSC1){var l=mo(this.tracks,i);if(l){this.tracks=i;return}if(this.textTracks=[],this.tracks=i,this.config.renderTextTracksNatively){var d=this.media?this.media.textTracks:null;this.tracks.forEach(function(c,p){var g;if(d&&p<d.length){for(var y=null,b=0;b<d.length;b++)if(Hs(d[b],c)){y=d[b];break}y&&(g=y)}if(g)Pe(g);else{var E=n._captionsOrSubtitlesFromCharacteristics(c);g=n.createTextTrack(E,c.name,c.lang),g&&(g.mode="disabled")}g&&(g.groupId=c.groupId,n.textTracks.push(g))})}else if(this.tracks.length){var u=this.tracks.map(function(c){return{label:c.name,kind:c.type.toLowerCase(),default:c.default,subtitleTrack:c}});this.hls.trigger(S.NON_NATIVE_TEXT_TRACKS_FOUND,{tracks:u})}}},a._captionsOrSubtitlesFromCharacteristics=function(e){if(e.attrs.CHARACTERISTICS){var t=/transcribes-spoken-dialog/gi.test(e.attrs.CHARACTERISTICS),n=/describes-music-and-sound/gi.test(e.attrs.CHARACTERISTICS);if(t&&n)return"captions"}return"subtitles"},a.onManifestLoaded=function(e,t){var n=this;this.config.enableCEA708Captions&&t.captions&&t.captions.forEach(function(i){var o=/(?:CC|SERVICE)([1-4])/.exec(i.instreamId);if(!!o){var l="textTrack"+o[1],d=n.captionsProperties[l];!d||(d.label=i.name,i.lang&&(d.languageCode=i.lang),d.media=i)}})},a.closedCaptionsForLevel=function(e){var t=this.hls.levels[e.level];return t==null?void 0:t.attrs["CLOSED-CAPTIONS"]},a.onFragLoading=function(e,t){var n=this.cea608Parser1,i=this.cea608Parser2,o=this.lastSn,l=this.lastPartIndex;if(!(!this.enabled||!(n&&i))&&t.frag.type===he.MAIN){var d,u,c=t.frag.sn,p=(d=t==null||(u=t.part)==null?void 0:u.index)!=null?d:-1;c===o+1||c===o&&p===l+1||(n.reset(),i.reset()),this.lastSn=c,this.lastPartIndex=p}},a.onFragLoaded=function(e,t){var n=t.frag,i=t.payload,o=this.initPTS,l=this.unparsedVttFrags;if(n.type===he.SUBTITLE)if(i.byteLength){if(!o[n.cc]){l.push(t),o.length&&this.hls.trigger(S.SUBTITLE_FRAG_PROCESSED,{success:!1,frag:n,error:new Error("Missing initial subtitle PTS")});return}var d=n.decryptdata,u="stats"in t;if(d==null||!d.encrypted||u){var c=this.tracks[n.level],p=this.vttCCs;p[n.cc]||(p[n.cc]={start:n.start,prevCC:this.prevCC,new:!0},this.prevCC=n.cc),c&&c.textCodec===to?this._parseIMSC1(n,i):this._parseVTTs(n,i,p)}}else this.hls.trigger(S.SUBTITLE_FRAG_PROCESSED,{success:!1,frag:n,error:new Error("Empty subtitle payload")})},a._parseIMSC1=function(e,t){var n=this,i=this.hls;_o(t,this.initPTS[e.cc],function(o){n._appendCues(o,e.level),i.trigger(S.SUBTITLE_FRAG_PROCESSED,{success:!0,frag:e})},function(o){P.log("Failed to parse IMSC1: "+o),i.trigger(S.SUBTITLE_FRAG_PROCESSED,{success:!1,frag:e,error:o})})},a._parseVTTs=function(e,t,n){var i,o=this,l=this.hls,d=(i=e.initSegment)!=null&&i.data?kt(e.initSegment.data,new Uint8Array(t)):t;Os(d,this.initPTS[e.cc],n,e.cc,e.start,function(u){o._appendCues(u,e.level),l.trigger(S.SUBTITLE_FRAG_PROCESSED,{success:!0,frag:e})},function(u){o._fallbackToIMSC1(e,t),P.log("Failed to parse VTT cue: "+u),l.trigger(S.SUBTITLE_FRAG_PROCESSED,{success:!1,frag:e,error:u})})},a._fallbackToIMSC1=function(e,t){var n=this,i=this.tracks[e.level];i.textCodec||_o(t,this.initPTS[e.cc],function(){i.textCodec=to,n._parseIMSC1(e,t)},function(){i.textCodec="wvtt"})},a._appendCues=function(e,t){var n=this.hls;if(this.config.renderTextTracksNatively){var i=this.textTracks[t];if(!i||i.mode==="disabled")return;e.forEach(function(d){return li(i,d)})}else{var o=this.tracks[t];if(!o)return;var l=o.default?"default":"subtitles"+t;n.trigger(S.CUES_PARSED,{type:"subtitles",cues:e,track:l})}},a.onFragDecrypted=function(e,t){var n=t.frag;if(n.type===he.SUBTITLE){if(!this.initPTS[n.cc]){this.unparsedVttFrags.push(t);return}this.onFragLoaded(S.FRAG_LOADED,t)}},a.onSubtitleTracksCleared=function(){this.tracks=[],this.captionsTracks={}},a.onFragParsingUserdata=function(e,t){var n=this.cea608Parser1,i=this.cea608Parser2;if(!(!this.enabled||!(n&&i))){var o=t.frag,l=t.samples;if(!(o.type===he.MAIN&&this.closedCaptionsForLevel(o)==="NONE"))for(var d=0;d<l.length;d++){var u=l[d].bytes;if(u){var c=this.extractCea608Data(u);n.addData(l[d].pts,c[0]),i.addData(l[d].pts,c[1])}}}},a.onBufferFlushing=function(e,t){var n=t.startOffset,i=t.endOffset,o=t.endOffsetSubtitles,l=t.type,d=this.media;if(!(!d||d.currentTime<i)){if(!l||l==="video"){var u=this.captionsTracks;Object.keys(u).forEach(function(p){return Yn(u[p],n,i)})}if(this.config.renderTextTracksNatively&&n===0&&o!==void 0){var c=this.textTracks;Object.keys(c).forEach(function(p){return Yn(c[p],n,o)})}}},a.extractCea608Data=function(e){for(var t=[[],[]],n=e[0]&31,i=2,o=0;o<n;o++){var l=e[i++],d=127&e[i++],u=127&e[i++];if(!(d===0&&u===0)){var c=(4&l)!=0;if(c){var p=3&l;(p===0||p===1)&&(t[p].push(d),t[p].push(u))}}}return t},s}();function Hs(s,a){return!!s&&s.label===a.name&&!(s.textTrack1||s.textTrack2)}function qs(s,a,r,e){return Math.min(a,e)-Math.max(s,r)}function Mo(){return{ccOffset:0,presentationOffset:0,0:{start:0,prevCC:-1,new:!0}}}var Ys=function(){function s(r){this.hls=void 0,this.autoLevelCapping=void 0,this.firstLevel=void 0,this.media=void 0,this.restrictedLevels=void 0,this.timer=void 0,this.clientRect=void 0,this.streamController=void 0,this.hls=r,this.autoLevelCapping=Number.POSITIVE_INFINITY,this.firstLevel=-1,this.media=null,this.restrictedLevels=[],this.timer=void 0,this.clientRect=null,this.registerListeners()}var a=s.prototype;return a.setStreamController=function(e){this.streamController=e},a.destroy=function(){this.unregisterListener(),this.hls.config.capLevelToPlayerSize&&this.stopCapping(),this.media=null,this.clientRect=null,this.hls=this.streamController=null},a.registerListeners=function(){var e=this.hls;e.on(S.FPS_DROP_LEVEL_CAPPING,this.onFpsDropLevelCapping,this),e.on(S.MEDIA_ATTACHING,this.onMediaAttaching,this),e.on(S.MANIFEST_PARSED,this.onManifestParsed,this),e.on(S.BUFFER_CODECS,this.onBufferCodecs,this),e.on(S.MEDIA_DETACHING,this.onMediaDetaching,this)},a.unregisterListener=function(){var e=this.hls;e.off(S.FPS_DROP_LEVEL_CAPPING,this.onFpsDropLevelCapping,this),e.off(S.MEDIA_ATTACHING,this.onMediaAttaching,this),e.off(S.MANIFEST_PARSED,this.onManifestParsed,this),e.off(S.BUFFER_CODECS,this.onBufferCodecs,this),e.off(S.MEDIA_DETACHING,this.onMediaDetaching,this)},a.onFpsDropLevelCapping=function(e,t){var n=this.hls.levels[t.droppedLevel];this.isLevelAllowed(n)&&this.restrictedLevels.push({bitrate:n.bitrate,height:n.height,width:n.width})},a.onMediaAttaching=function(e,t){this.media=t.media instanceof HTMLVideoElement?t.media:null,this.clientRect=null},a.onManifestParsed=function(e,t){var n=this.hls;this.restrictedLevels=[],this.firstLevel=t.firstLevel,n.config.capLevelToPlayerSize&&t.video&&this.startCapping()},a.onBufferCodecs=function(e,t){var n=this.hls;n.config.capLevelToPlayerSize&&t.video&&this.startCapping()},a.onMediaDetaching=function(){this.stopCapping()},a.detectPlayerSize=function(){if(this.media&&this.mediaHeight>0&&this.mediaWidth>0){var e=this.hls.levels;if(e.length){var t=this.hls;t.autoLevelCapping=this.getMaxLevel(e.length-1),t.autoLevelCapping>this.autoLevelCapping&&this.streamController&&this.streamController.nextLevelSwitch(),this.autoLevelCapping=t.autoLevelCapping}}},a.getMaxLevel=function(e){var t=this,n=this.hls.levels;if(!n.length)return-1;var i=n.filter(function(o,l){return t.isLevelAllowed(o)&&l<=e});return this.clientRect=null,s.getMaxLevelByMediaSize(i,this.mediaWidth,this.mediaHeight)},a.startCapping=function(){this.timer||(this.autoLevelCapping=Number.POSITIVE_INFINITY,this.hls.firstLevel=this.getMaxLevel(this.firstLevel),self.clearInterval(this.timer),this.timer=self.setInterval(this.detectPlayerSize.bind(this),1e3),this.detectPlayerSize())},a.stopCapping=function(){this.restrictedLevels=[],this.firstLevel=-1,this.autoLevelCapping=Number.POSITIVE_INFINITY,this.timer&&(self.clearInterval(this.timer),this.timer=void 0)},a.getDimensions=function(){if(this.clientRect)return this.clientRect;var e=this.media,t={width:0,height:0};if(e){var n=e.getBoundingClientRect();t.width=n.width,t.height=n.height,!t.width&&!t.height&&(t.width=n.right-n.left||e.width||0,t.height=n.bottom-n.top||e.height||0)}return this.clientRect=t,t},a.isLevelAllowed=function(e){var t=this.restrictedLevels;return!t.some(function(n){return e.bitrate===n.bitrate&&e.width===n.width&&e.height===n.height})},s.getMaxLevelByMediaSize=function(e,t,n){if(!(e!=null&&e.length))return-1;for(var i=function(c,p){return p?c.width!==p.width||c.height!==p.height:!0},o=e.length-1,l=0;l<e.length;l+=1){var d=e[l];if((d.width>=t||d.height>=n)&&i(d,e[l+1])){o=l;break}}return o},Q(s,[{key:"mediaWidth",get:function(){return this.getDimensions().width*this.contentScaleFactor}},{key:"mediaHeight",get:function(){return this.getDimensions().height*this.contentScaleFactor}},{key:"contentScaleFactor",get:function(){var e=1;if(!this.hls.config.ignoreDevicePixelRatio)try{e=self.devicePixelRatio}catch(t){}return e}}]),s}(),Ws=function(){function s(r){this.hls=void 0,this.isVideoPlaybackQualityAvailable=!1,this.timer=void 0,this.media=null,this.lastTime=void 0,this.lastDroppedFrames=0,this.lastDecodedFrames=0,this.streamController=void 0,this.hls=r,this.registerListeners()}var a=s.prototype;return a.setStreamController=function(e){this.streamController=e},a.registerListeners=function(){this.hls.on(S.MEDIA_ATTACHING,this.onMediaAttaching,this)},a.unregisterListeners=function(){this.hls.off(S.MEDIA_ATTACHING,this.onMediaAttaching,this)},a.destroy=function(){this.timer&&clearInterval(this.timer),this.unregisterListeners(),this.isVideoPlaybackQualityAvailable=!1,this.media=null},a.onMediaAttaching=function(e,t){var n=this.hls.config;if(n.capLevelOnFPSDrop){var i=t.media instanceof self.HTMLVideoElement?t.media:null;this.media=i,i&&typeof i.getVideoPlaybackQuality=="function"&&(this.isVideoPlaybackQualityAvailable=!0),self.clearInterval(this.timer),this.timer=self.setInterval(this.checkFPSInterval.bind(this),n.fpsDroppedMonitoringPeriod)}},a.checkFPS=function(e,t,n){var i=performance.now();if(t){if(this.lastTime){var o=i-this.lastTime,l=n-this.lastDroppedFrames,d=t-this.lastDecodedFrames,u=1e3*l/o,c=this.hls;if(c.trigger(S.FPS_DROP,{currentDropped:l,currentDecoded:d,totalDroppedFrames:n}),u>0&&l>c.config.fpsDroppedMonitoringThreshold*d){var p=c.currentLevel;P.warn("drop FPS ratio greater than max allowed value for currentLevel: "+p),p>0&&(c.autoLevelCapping===-1||c.autoLevelCapping>=p)&&(p=p-1,c.trigger(S.FPS_DROP_LEVEL_CAPPING,{level:p,droppedLevel:c.currentLevel}),c.autoLevelCapping=p,this.streamController.nextLevelSwitch())}}this.lastTime=i,this.lastDroppedFrames=n,this.lastDecodedFrames=t}},a.checkFPSInterval=function(){var e=this.media;if(e)if(this.isVideoPlaybackQualityAvailable){var t=e.getVideoPlaybackQuality();this.checkFPS(e,t.totalVideoFrames,t.droppedVideoFrames)}else this.checkFPS(e,e.webkitDecodedFrameCount,e.webkitDroppedFrameCount)},s}(),Bi="[eme]",Fo=function(){function s(r){this.hls=void 0,this.config=void 0,this.media=null,this.keyFormatPromise=null,this.keySystemAccessPromises={},this._requestLicenseFailureCount=0,this.mediaKeySessions=[],this.keyIdToKeySessionPromise={},this.setMediaKeysQueue=s.CDMCleanupPromise?[s.CDMCleanupPromise]:[],this.onMediaEncrypted=this._onMediaEncrypted.bind(this),this.onWaitingForKey=this._onWaitingForKey.bind(this),this.debug=P.debug.bind(P,Bi),this.log=P.log.bind(P,Bi),this.warn=P.warn.bind(P,Bi),this.error=P.error.bind(P,Bi),this.hls=r,this.config=r.config,this.registerListeners()}var a=s.prototype;return a.destroy=function(){this.unregisterListeners(),this.onMediaDetached();var e=this.config;e.requestMediaKeySystemAccessFunc=null,e.licenseXhrSetup=e.licenseResponseCallback=void 0,e.drmSystems=e.drmSystemOptions={},this.hls=this.onMediaEncrypted=this.onWaitingForKey=this.keyIdToKeySessionPromise=null,this.config=null},a.registerListeners=function(){this.hls.on(S.MEDIA_ATTACHED,this.onMediaAttached,this),this.hls.on(S.MEDIA_DETACHED,this.onMediaDetached,this),this.hls.on(S.MANIFEST_LOADED,this.onManifestLoaded,this)},a.unregisterListeners=function(){this.hls.off(S.MEDIA_ATTACHED,this.onMediaAttached,this),this.hls.off(S.MEDIA_DETACHED,this.onMediaDetached,this),this.hls.off(S.MANIFEST_LOADED,this.onManifestLoaded,this)},a.getLicenseServerUrl=function(e){var t=this.config,n=t.drmSystems,i=t.widevineLicenseUrl,o=n[e];if(o)return o.licenseUrl;if(e===De.WIDEVINE&&i)return i;throw new Error('no license server URL configured for key-system "'+e+'"')},a.getServerCertificateUrl=function(e){var t=this.config.drmSystems,n=t[e];if(n)return n.serverCertificateUrl;this.log('No Server Certificate in config.drmSystems["'+e+'"]')},a.attemptKeySystemAccess=function(e){var t=this,n=this.hls.levels,i=function(u,c,p){return!!u&&p.indexOf(u)===c},o=n.map(function(d){return d.audioCodec}).filter(i),l=n.map(function(d){return d.videoCodec}).filter(i);return o.length+l.length===0&&l.push("avc1.42e01e"),new Promise(function(d,u){var c=function p(g){var y=g.shift();t.getMediaKeysPromise(y,o,l).then(function(b){return d({keySystem:y,mediaKeys:b})}).catch(function(b){g.length?p(g):b instanceof dt?u(b):u(new dt({type:fe.KEY_SYSTEM_ERROR,details:U.KEY_SYSTEM_NO_ACCESS,error:b,fatal:!0},b.message))})};c(e)})},a.requestMediaKeySystemAccess=function(e,t){var n=this.config.requestMediaKeySystemAccessFunc;if(typeof n!="function"){var i="Configured requestMediaKeySystemAccess is not a function "+n;return ar===null&&self.location.protocol==="http:"&&(i="navigator.requestMediaKeySystemAccess is not available over insecure protocol "+location.protocol),Promise.reject(new Error(i))}return n(e,t)},a.getMediaKeysPromise=function(e,t,n){var i=this,o=or(e,t,n,this.config.drmSystemOptions),l=this.keySystemAccessPromises[e],d=l==null?void 0:l.keySystemAccess;if(!d){this.log('Requesting encrypted media "'+e+'" key-system access with config: '+JSON.stringify(o)),d=this.requestMediaKeySystemAccess(e,o);var u=this.keySystemAccessPromises[e]={keySystemAccess:d};return d.catch(function(c){i.log('Failed to obtain access to key-system "'+e+'": '+c)}),d.then(function(c){i.log('Access for key-system "'+c.keySystem+'" obtained');var p=i.fetchServerCertificate(e);return i.log('Create media-keys for "'+e+'"'),u.mediaKeys=c.createMediaKeys().then(function(g){return i.log('Media-keys created for "'+e+'"'),p.then(function(y){return y?i.setMediaKeysServerCertificate(g,e,y):g})}),u.mediaKeys.catch(function(g){i.error('Failed to create media-keys for "'+e+'"}: '+g)}),u.mediaKeys})}return d.then(function(){return l.mediaKeys})},a.createMediaKeySessionContext=function(e){var t=e.decryptdata,n=e.keySystem,i=e.mediaKeys;this.log('Creating key-system session "'+n+'" keyId: '+Qe.hexDump(t.keyId||[]));var o=i.createSession(),l={decryptdata:t,keySystem:n,mediaKeys:i,mediaKeysSession:o,keyStatus:"status-pending"};return this.mediaKeySessions.push(l),l},a.renewKeySession=function(e){var t=e.decryptdata;if(t.pssh){var n=this.createMediaKeySessionContext(e),i=this.getKeyIdString(t),o="cenc";this.keyIdToKeySessionPromise[i]=this.generateRequestWithPreferredKeySession(n,o,t.pssh,"expired")}else this.warn("Could not renew expired session. Missing pssh initData.");this.removeSession(e)},a.getKeyIdString=function(e){if(!e)throw new Error("Could not read keyId of undefined decryptdata");if(e.keyId===null)throw new Error("keyId is null");return Qe.hexDump(e.keyId)},a.updateKeySession=function(e,t){var n,i=e.mediaKeysSession;return this.log('Updating key-session "'+i.sessionId+'" for keyID '+Qe.hexDump(((n=e.decryptdata)==null?void 0:n.keyId)||[])+`
      } (data length: `+(t&&t.byteLength)+")"),i.update(t)},a.selectKeySystemFormat=function(e){var t=Object.keys(e.levelkeys||{});return this.keyFormatPromise||(this.log("Selecting key-system from fragment (sn: "+e.sn+" "+e.type+": "+e.level+") key formats "+t.join(", ")),this.keyFormatPromise=this.getKeyFormatPromise(t)),this.keyFormatPromise},a.getKeyFormatPromise=function(e){var t=this;return new Promise(function(n,i){var o=Ct(t.config),l=e.map(Hr).filter(function(d){return!!d&&o.indexOf(d)!==-1});return t.getKeySystemSelectionPromise(l).then(function(d){var u=d.keySystem,c=Yr(u);c?n(c):i(new Error('Unable to find format for key-system "'+u+'"'))}).catch(i)})},a.loadKey=function(e){var t=this,n=e.keyInfo.decryptdata,i=this.getKeyIdString(n),o="(keyId: "+i+' format: "'+n.keyFormat+'" method: '+n.method+" uri: "+n.uri+")";this.log("Starting session for key "+o);var l=this.keyIdToKeySessionPromise[i];return l||(l=this.keyIdToKeySessionPromise[i]=this.getKeySystemForKeyPromise(n).then(function(d){var u=d.keySystem,c=d.mediaKeys;return t.throwIfDestroyed(),t.log("Handle encrypted media sn: "+e.frag.sn+" "+e.frag.type+": "+e.frag.level+" using key "+o),t.attemptSetMediaKeys(u,c).then(function(){t.throwIfDestroyed();var p=t.createMediaKeySessionContext({keySystem:u,mediaKeys:c,decryptdata:n}),g="cenc";return t.generateRequestWithPreferredKeySession(p,g,n.pssh,"playlist-key")})}),l.catch(function(d){return t.handleError(d)})),l},a.throwIfDestroyed=function(e){if(!this.hls)throw new Error("invalid state")},a.handleError=function(e){!this.hls||(this.error(e.message),e instanceof dt?this.hls.trigger(S.ERROR,e.data):this.hls.trigger(S.ERROR,{type:fe.KEY_SYSTEM_ERROR,details:U.KEY_SYSTEM_NO_KEYS,error:e,fatal:!0}))},a.getKeySystemForKeyPromise=function(e){var t=this.getKeyIdString(e),n=this.keyIdToKeySessionPromise[t];if(!n){var i=Hr(e.keyFormat),o=i?[i]:Ct(this.config);return this.attemptKeySystemAccess(o)}return n},a.getKeySystemSelectionPromise=function(e){if(e.length||(e=Ct(this.config)),e.length===0)throw new dt({type:fe.KEY_SYSTEM_ERROR,details:U.KEY_SYSTEM_NO_CONFIGURED_LICENSE,fatal:!0},"Missing key-system license configuration options "+JSON.stringify({drmSystems:this.config.drmSystems}));return this.attemptKeySystemAccess(e)},a._onMediaEncrypted=function(e){var t=this,n=e.initDataType,i=e.initData;if(this.debug('"'+e.type+'" event: init data type: "'+n+'"'),i!==null){var o,l;if(n==="sinf"&&this.config.drmSystems[De.FAIRPLAY]){var d=Ge(new Uint8Array(i));try{var u=Pn(JSON.parse(d).sinf),c=Jr(new Uint8Array(u));if(!c)return;o=c.subarray(8,24),l=De.FAIRPLAY}catch(D){this.warn('Failed to parse sinf "encrypted" event message initData');return}}else{var p=Nn(i);if(p===null)return;p.version===0&&p.systemId===qr.WIDEVINE&&p.data&&(o=p.data.subarray(8,24)),l=Hi(p.systemId)}if(!(!l||!o)){for(var g=Qe.hexDump(o),y=this.keyIdToKeySessionPromise,b=this.mediaKeySessions,E=y[g],C=function(){var _=b[w],F=_.decryptdata;if(F.pssh||!F.keyId)return"continue";var O=Qe.hexDump(F.keyId);if(g===O||F.uri.replace(/-/g,"").indexOf(g)!==-1)return E=y[O],delete y[O],F.pssh=new Uint8Array(i),F.keyId=o,E=y[g]=E.then(function(){return t.generateRequestWithPreferredKeySession(_,n,i,"encrypted-event-key-match")}),"break"},w=0;w<b.length;w++){var k=C();if(k!=="continue"&&k==="break")break}E||(E=y[g]=this.getKeySystemSelectionPromise([l]).then(function(D){var _,F=D.keySystem,O=D.mediaKeys;t.throwIfDestroyed();var M=new Gt("ISO-23001-7",g,(_=Yr(F))!=null?_:"");return M.pssh=new Uint8Array(i),M.keyId=o,t.attemptSetMediaKeys(F,O).then(function(){t.throwIfDestroyed();var z=t.createMediaKeySessionContext({decryptdata:M,keySystem:F,mediaKeys:O});return t.generateRequestWithPreferredKeySession(z,n,i,"encrypted-event-no-match")})})),E.catch(function(D){return t.handleError(D)})}}},a._onWaitingForKey=function(e){this.log('"'+e.type+'" event')},a.attemptSetMediaKeys=function(e,t){var n=this,i=this.setMediaKeysQueue.slice();this.log('Setting media-keys for "'+e+'"');var o=Promise.all(i).then(function(){if(!n.media)throw new Error("Attempted to set mediaKeys without media element attached");return n.media.setMediaKeys(t)});return this.setMediaKeysQueue.push(o),o.then(function(){n.log('Media-keys set for "'+e+'"'),i.push(o),n.setMediaKeysQueue=n.setMediaKeysQueue.filter(function(l){return i.indexOf(l)===-1})})},a.generateRequestWithPreferredKeySession=function(e,t,n,i){var o,l,d=this,u=(o=this.config.drmSystems)==null||(l=o[e.keySystem])==null?void 0:l.generateRequest;if(u)try{var c=u.call(this.hls,t,n,e);if(!c)throw new Error("Invalid response from configured generateRequest filter");t=c.initDataType,n=e.decryptdata.pssh=c.initData?new Uint8Array(c.initData):null}catch(E){var p;if(this.warn(E.message),(p=this.hls)!=null&&p.config.debug)throw E}if(n===null)return this.log('Skipping key-session request for "'+i+'" (no initData)'),Promise.resolve(e);var g=this.getKeyIdString(e.decryptdata);this.log('Generating key-session request for "'+i+'": '+g+" (init data type: "+t+" length: "+(n?n.byteLength:null)+")");var y=new Di;e.mediaKeysSession.onmessage=function(E){var C=e.mediaKeysSession;if(!C){y.emit("error",new Error("invalid state"));return}var w=E.messageType,k=E.message;d.log('"'+w+'" message event for session "'+C.sessionId+'" message size: '+k.byteLength),w==="license-request"||w==="license-renewal"?d.renewLicense(e,k).catch(function(D){d.handleError(D),y.emit("error",D)}):w==="license-release"?e.keySystem===De.FAIRPLAY&&(d.updateKeySession(e,Bn("acknowledged")),d.removeSession(e)):d.warn('unhandled media key message type "'+w+'"')},e.mediaKeysSession.onkeystatuseschange=function(E){var C=e.mediaKeysSession;if(!C){y.emit("error",new Error("invalid state"));return}d.onKeyStatusChange(e);var w=e.keyStatus;y.emit("keyStatus",w),w==="expired"&&(d.warn(e.keySystem+" expired for key "+g),d.renewKeySession(e))};var b=new Promise(function(E,C){y.on("error",C),y.on("keyStatus",function(w){w.startsWith("usable")?E():w==="output-restricted"?C(new dt({type:fe.KEY_SYSTEM_ERROR,details:U.KEY_SYSTEM_STATUS_OUTPUT_RESTRICTED,fatal:!1},"HDCP level output restricted")):w==="internal-error"?C(new dt({type:fe.KEY_SYSTEM_ERROR,details:U.KEY_SYSTEM_STATUS_INTERNAL_ERROR,fatal:!0},'key status changed to "'+w+'"')):w==="expired"?C(new Error("key expired while generating request")):d.warn('unhandled key status change "'+w+'"')})});return e.mediaKeysSession.generateRequest(t,n).then(function(){var E;d.log('Request generated for key-session "'+((E=e.mediaKeysSession)==null?void 0:E.sessionId)+'" keyId: '+g)}).catch(function(E){throw new dt({type:fe.KEY_SYSTEM_ERROR,details:U.KEY_SYSTEM_NO_SESSION,error:E,fatal:!1},"Error generating key-session request: "+E)}).then(function(){return b}).catch(function(E){throw y.removeAllListeners(),d.removeSession(e),E}).then(function(){return y.removeAllListeners(),e})},a.onKeyStatusChange=function(e){var t=this;e.mediaKeysSession.keyStatuses.forEach(function(n,i){t.log('key status change "'+n+'" for keyStatuses keyId: '+Qe.hexDump("buffer"in i?new Uint8Array(i.buffer,i.byteOffset,i.byteLength):new Uint8Array(i))+" session keyId: "+Qe.hexDump(new Uint8Array(e.decryptdata.keyId||[]))+" uri: "+e.decryptdata.uri),e.keyStatus=n})},a.fetchServerCertificate=function(e){var t=this.config,n=t.loader,i=new n(t),o=this.getServerCertificateUrl(e);return o?(this.log('Fetching serverCertificate for "'+e+'"'),new Promise(function(l,d){var u={responseType:"arraybuffer",url:o},c=t.certLoadPolicy.default,p={loadPolicy:c,timeout:c.maxLoadTimeMs,maxRetry:0,retryDelay:0,maxRetryDelay:0},g={onSuccess:function(b,E,C,w){l(b.data)},onError:function(b,E,C,w){d(new dt({type:fe.KEY_SYSTEM_ERROR,details:U.KEY_SYSTEM_SERVER_CERTIFICATE_REQUEST_FAILED,fatal:!0,networkDetails:C,response:Ke({url:u.url,data:void 0},b)},'"'+e+'" certificate request failed ('+o+"). Status: "+b.code+" ("+b.text+")"))},onTimeout:function(b,E,C){d(new dt({type:fe.KEY_SYSTEM_ERROR,details:U.KEY_SYSTEM_SERVER_CERTIFICATE_REQUEST_FAILED,fatal:!0,networkDetails:C,response:{url:u.url,data:void 0}},'"'+e+'" certificate request timed out ('+o+")"))},onAbort:function(b,E,C){d(new Error("aborted"))}};i.load(u,p,g)})):Promise.resolve()},a.setMediaKeysServerCertificate=function(e,t,n){var i=this;return new Promise(function(o,l){e.setServerCertificate(n).then(function(d){i.log("setServerCertificate "+(d?"success":"not supported by CDM")+" ("+(n==null?void 0:n.byteLength)+') on "'+t+'"'),o(e)}).catch(function(d){l(new dt({type:fe.KEY_SYSTEM_ERROR,details:U.KEY_SYSTEM_SERVER_CERTIFICATE_UPDATE_FAILED,error:d,fatal:!0},d.message))})})},a.renewLicense=function(e,t){var n=this;return this.requestLicense(e,new Uint8Array(t)).then(function(i){return n.updateKeySession(e,new Uint8Array(i)).catch(function(o){throw new dt({type:fe.KEY_SYSTEM_ERROR,details:U.KEY_SYSTEM_SESSION_UPDATE_FAILED,error:o,fatal:!0},o.message)})})},a.setupLicenseXHR=function(e,t,n,i){var o=this,l=this.config.licenseXhrSetup;return l?Promise.resolve().then(function(){if(!n.decryptdata)throw new Error("Key removed");return l.call(o.hls,e,t,n,i)}).catch(function(d){if(!n.decryptdata)throw d;return e.open("POST",t,!0),l.call(o.hls,e,t,n,i)}).then(function(d){e.readyState||e.open("POST",t,!0);var u=d||i;return{xhr:e,licenseChallenge:u}}):(e.open("POST",t,!0),Promise.resolve({xhr:e,licenseChallenge:i}))},a.requestLicense=function(e,t){var n=this,i=this.config.keyLoadPolicy.default;return new Promise(function(o,l){var d=n.getLicenseServerUrl(e.keySystem);n.log("Sending license request to URL: "+d);var u=new XMLHttpRequest;u.responseType="arraybuffer",u.onreadystatechange=function(){if(!n.hls||!e.mediaKeysSession)return l(new Error("invalid state"));if(u.readyState===4)if(u.status===200){n._requestLicenseFailureCount=0;var c=u.response;n.log("License received "+(c instanceof ArrayBuffer?c.byteLength:c));var p=n.config.licenseResponseCallback;if(p)try{c=p.call(n.hls,u,d,e)}catch(E){n.error(E)}o(c)}else{var g=i.errorRetry,y=g?g.maxNumRetry:0;if(n._requestLicenseFailureCount++,n._requestLicenseFailureCount>y||u.status>=400&&u.status<500)l(new dt({type:fe.KEY_SYSTEM_ERROR,details:U.KEY_SYSTEM_LICENSE_REQUEST_FAILED,fatal:!0,networkDetails:u,response:{url:d,data:void 0,code:u.status,text:u.statusText}},"License Request XHR failed ("+d+"). Status: "+u.status+" ("+u.statusText+")"));else{var b=y-n._requestLicenseFailureCount+1;n.warn("Retrying license request, "+b+" attempts left"),n.requestLicense(e,t).then(o,l)}}},e.licenseXhr&&e.licenseXhr.readyState!==XMLHttpRequest.DONE&&e.licenseXhr.abort(),e.licenseXhr=u,n.setupLicenseXHR(u,d,e,t).then(function(c){var p=c.xhr,g=c.licenseChallenge;p.send(g)})})},a.onMediaAttached=function(e,t){if(!!this.config.emeEnabled){var n=t.media;this.media=n,n.addEventListener("encrypted",this.onMediaEncrypted),n.addEventListener("waitingforkey",this.onWaitingForKey)}},a.onMediaDetached=function(){var e=this,t=this.media,n=this.mediaKeySessions;t&&(t.removeEventListener("encrypted",this.onMediaEncrypted),t.removeEventListener("waitingforkey",this.onWaitingForKey),this.media=null),this._requestLicenseFailureCount=0,this.setMediaKeysQueue=[],this.mediaKeySessions=[],this.keyIdToKeySessionPromise={},Gt.clearKeyUriToKeyIdMap();var i=n.length;s.CDMCleanupPromise=Promise.all(n.map(function(o){return e.removeSession(o)}).concat(t==null?void 0:t.setMediaKeys(null).catch(function(o){e.log("Could not clear media keys: "+o+". media.src: "+(t==null?void 0:t.src))}))).then(function(){i&&(e.log("finished closing key sessions and clearing media keys"),n.length=0)}).catch(function(o){e.log("Could not close sessions and clear media keys: "+o+". media.src: "+(t==null?void 0:t.src))})},a.onManifestLoaded=function(e,t){var n=t.sessionKeys;if(!(!n||!this.config.emeEnabled)&&!this.keyFormatPromise){var i=n.reduce(function(o,l){return o.indexOf(l.keyFormat)===-1&&o.push(l.keyFormat),o},[]);this.log("Selecting key-system from session-keys "+i.join(", ")),this.keyFormatPromise=this.getKeyFormatPromise(i)}},a.removeSession=function(e){var t=this,n=e.mediaKeysSession,i=e.licenseXhr;if(n){this.log("Remove licenses and keys and close session "+n.sessionId),n.onmessage=null,n.onkeystatuseschange=null,i&&i.readyState!==XMLHttpRequest.DONE&&i.abort(),e.mediaKeysSession=e.decryptdata=e.licenseXhr=void 0;var o=this.mediaKeySessions.indexOf(e);return o>-1&&this.mediaKeySessions.splice(o,1),n.remove().catch(function(l){t.log("Could not remove session: "+l)}).then(function(){return n.close()}).catch(function(l){t.log("Could not close session: "+l)})}},s}();Fo.CDMCleanupPromise=void 0;var dt=function(s){j(a,s);function a(r,e){var t;return t=s.call(this,e)||this,t.data=void 0,r.error||(r.error=new Error(e)),t.data=r,r.err=r.error,t}return a}(ie(Error)),zs=1,et={MANIFEST:"m",AUDIO:"a",VIDEO:"v",MUXED:"av",INIT:"i",CAPTION:"c",TIMED_TEXT:"tt",KEY:"k",OTHER:"o"},Vs="h",js=function(){function s(r){var e=this;this.hls=void 0,this.config=void 0,this.media=void 0,this.sid=void 0,this.cid=void 0,this.useHeaders=!1,this.initialized=!1,this.starved=!1,this.buffering=!0,this.audioBuffer=void 0,this.videoBuffer=void 0,this.onWaiting=function(){e.initialized&&(e.starved=!0),e.buffering=!0},this.onPlaying=function(){e.initialized||(e.initialized=!0),e.buffering=!1},this.applyPlaylistData=function(i){try{e.apply(i,{ot:et.MANIFEST,su:!e.initialized})}catch(o){P.warn("Could not generate manifest CMCD data.",o)}},this.applyFragmentData=function(i){try{var o=i.frag,l=e.hls.levels[o.level],d=e.getObjectType(o),u={d:o.duration*1e3,ot:d};(d===et.VIDEO||d===et.AUDIO||d==et.MUXED)&&(u.br=l.bitrate/1e3,u.tb=e.getTopBandwidth(d)/1e3,u.bl=e.getBufferLength(d)),e.apply(i,u)}catch(c){P.warn("Could not generate segment CMCD data.",c)}},this.hls=r;var t=this.config=r.config,n=t.cmcd;n!=null&&(t.pLoader=this.createPlaylistLoader(),t.fLoader=this.createFragmentLoader(),this.sid=n.sessionId||s.uuid(),this.cid=n.contentId,this.useHeaders=n.useHeaders===!0,this.registerListeners())}var a=s.prototype;return a.registerListeners=function(){var e=this.hls;e.on(S.MEDIA_ATTACHED,this.onMediaAttached,this),e.on(S.MEDIA_DETACHED,this.onMediaDetached,this),e.on(S.BUFFER_CREATED,this.onBufferCreated,this)},a.unregisterListeners=function(){var e=this.hls;e.off(S.MEDIA_ATTACHED,this.onMediaAttached,this),e.off(S.MEDIA_DETACHED,this.onMediaDetached,this),e.off(S.BUFFER_CREATED,this.onBufferCreated,this)},a.destroy=function(){this.unregisterListeners(),this.onMediaDetached(),this.hls=this.config=this.audioBuffer=this.videoBuffer=null},a.onMediaAttached=function(e,t){this.media=t.media,this.media.addEventListener("waiting",this.onWaiting),this.media.addEventListener("playing",this.onPlaying)},a.onMediaDetached=function(){!this.media||(this.media.removeEventListener("waiting",this.onWaiting),this.media.removeEventListener("playing",this.onPlaying),this.media=null)},a.onBufferCreated=function(e,t){var n,i;this.audioBuffer=(n=t.tracks.audio)==null?void 0:n.buffer,this.videoBuffer=(i=t.tracks.video)==null?void 0:i.buffer},a.createData=function(){var e;return{v:zs,sf:Vs,sid:this.sid,cid:this.cid,pr:(e=this.media)==null?void 0:e.playbackRate,mtp:this.hls.bandwidthEstimate/1e3}},a.apply=function(e,t){t===void 0&&(t={}),q(t,this.createData());var n=t.ot===et.INIT||t.ot===et.VIDEO||t.ot===et.MUXED;if(this.starved&&n&&(t.bs=!0,t.su=!0,this.starved=!1),t.su==null&&(t.su=this.buffering),this.useHeaders){var i=s.toHeaders(t);if(!Object.keys(i).length)return;e.headers||(e.headers={}),q(e.headers,i)}else{var o=s.toQuery(t);if(!o)return;e.url=s.appendQueryToUri(e.url,o)}},a.getObjectType=function(e){var t=e.type;if(t==="subtitle")return et.TIMED_TEXT;if(e.sn==="initSegment")return et.INIT;if(t==="audio")return et.AUDIO;if(t==="main")return this.hls.audioTracks.length?et.VIDEO:et.MUXED},a.getTopBandwidth=function(e){var t=0,n,i=this.hls;if(e===et.AUDIO)n=i.audioTracks;else{var o=i.maxAutoLevel,l=o>-1?o+1:i.levels.length;n=i.levels.slice(0,l)}for(var d=ve(n),u;!(u=d()).done;){var c=u.value;c.bitrate>t&&(t=c.bitrate)}return t>0?t:NaN},a.getBufferLength=function(e){var t=this.hls.media,n=e===et.AUDIO?this.audioBuffer:this.videoBuffer;if(!n||!t)return NaN;var i=Fe.bufferInfo(n,t.currentTime,this.config.maxBufferHole);return i.len*1e3},a.createPlaylistLoader=function(){var e=this.config.pLoader,t=this.applyPlaylistData,n=e||this.config.loader;return function(){function i(l){this.loader=void 0,this.loader=new n(l)}var o=i.prototype;return o.destroy=function(){this.loader.destroy()},o.abort=function(){this.loader.abort()},o.load=function(d,u,c){t(d),this.loader.load(d,u,c)},Q(i,[{key:"stats",get:function(){return this.loader.stats}},{key:"context",get:function(){return this.loader.context}}]),i}()},a.createFragmentLoader=function(){var e=this.config.fLoader,t=this.applyFragmentData,n=e||this.config.loader;return function(){function i(l){this.loader=void 0,this.loader=new n(l)}var o=i.prototype;return o.destroy=function(){this.loader.destroy()},o.abort=function(){this.loader.abort()},o.load=function(d,u,c){t(d),this.loader.load(d,u,c)},Q(i,[{key:"stats",get:function(){return this.loader.stats}},{key:"context",get:function(){return this.loader.context}}]),i}()},s.uuid=function(){var e=URL.createObjectURL(new Blob),t=e.toString();return URL.revokeObjectURL(e),t.slice(t.lastIndexOf("/")+1)},s.serialize=function(e){for(var t=[],n=function(k){return!Number.isNaN(k)&&k!=null&&k!==""&&k!==!1},i=function(k){return Math.round(k)},o=function(k){return i(k/100)*100},l=function(k){return encodeURIComponent(k)},d={br:i,d:i,bl:o,dl:o,mtp:o,nor:l,rtp:o,tb:i},u=Object.keys(e||{}).sort(),c=ve(u),p;!(p=c()).done;){var g=p.value,y=e[g];if(!!n(y)&&!(g==="v"&&y===1)&&!(g=="pr"&&y===1)){var b=d[g];b&&(y=b(y));var E=typeof y,C=void 0;g==="ot"||g==="sf"||g==="st"?C=g+"="+y:E==="boolean"?C=g:E==="number"?C=g+"="+y:C=g+"="+JSON.stringify(y),t.push(C)}}return t.join(",")},s.toHeaders=function(e){for(var t=Object.keys(e),n={},i=["Object","Request","Session","Status"],o=[{},{},{},{}],l={br:0,d:0,ot:0,tb:0,bl:1,dl:1,mtp:1,nor:1,nrr:1,su:1,cid:2,pr:2,sf:2,sid:2,st:2,v:2,bs:3,rtp:3},d=0,u=t;d<u.length;d++){var c=u[d],p=l[c]!=null?l[c]:1;o[p][c]=e[c]}for(var g=0;g<o.length;g++){var y=s.serialize(o[g]);y&&(n["CMCD-"+i[g]]=y)}return n},s.toQuery=function(e){return"CMCD="+encodeURIComponent(s.serialize(e))},s.appendQueryToUri=function(e,t){if(!t)return e;var n=e.includes("?")?"&":"?";return""+e+n+t},s}(),Qs=3e5,Xs=function(){function s(r){this.hls=void 0,this.log=void 0,this.loader=null,this.uri=null,this.pathwayId=".",this.pathwayPriority=null,this.timeToLoad=300,this.reloadTimer=-1,this.updated=0,this.started=!1,this.enabled=!0,this.levels=null,this.audioTracks=null,this.subtitleTracks=null,this.penalizedPathways={},this.hls=r,this.log=P.log.bind(P,"[content-steering]:"),this.registerListeners()}var a=s.prototype;return a.registerListeners=function(){var e=this.hls;e.on(S.MANIFEST_LOADING,this.onManifestLoading,this),e.on(S.MANIFEST_LOADED,this.onManifestLoaded,this),e.on(S.MANIFEST_PARSED,this.onManifestParsed,this),e.on(S.ERROR,this.onError,this)},a.unregisterListeners=function(){var e=this.hls;!e||(e.off(S.MANIFEST_LOADING,this.onManifestLoading,this),e.off(S.MANIFEST_LOADED,this.onManifestLoaded,this),e.off(S.MANIFEST_PARSED,this.onManifestParsed,this),e.off(S.ERROR,this.onError,this))},a.startLoad=function(){if(this.started=!0,self.clearTimeout(this.reloadTimer),this.enabled&&this.uri)if(this.updated){var e=Math.max(this.timeToLoad*1e3-(performance.now()-this.updated),0);this.scheduleRefresh(this.uri,e)}else this.loadSteeringManifest(this.uri)},a.stopLoad=function(){this.started=!1,this.loader&&(this.loader.destroy(),this.loader=null),self.clearTimeout(this.reloadTimer)},a.destroy=function(){this.unregisterListeners(),this.stopLoad(),this.hls=null,this.levels=this.audioTracks=this.subtitleTracks=null},a.removeLevel=function(e){var t=this.levels;t&&(this.levels=t.filter(function(n){return n!==e}))},a.onManifestLoading=function(){this.stopLoad(),this.enabled=!0,this.timeToLoad=300,this.updated=0,this.uri=null,this.pathwayId=".",this.levels=this.audioTracks=this.subtitleTracks=null},a.onManifestLoaded=function(e,t){var n=t.contentSteering;n!==null&&(this.pathwayId=n.pathwayId,this.uri=n.uri,this.started&&this.startLoad())},a.onManifestParsed=function(e,t){this.audioTracks=t.audioTracks,this.subtitleTracks=t.subtitleTracks},a.onError=function(e,t){var n=t.errorAction;if((n==null?void 0:n.action)===ze.SendAlternateToPenaltyBox&&n.flags===Oe.MoveAllAlternatesMatchingHost){var i=this.pathwayPriority,o=this.pathwayId;this.penalizedPathways[o]||(this.penalizedPathways[o]=performance.now()),!i&&this.levels&&(i=this.levels.reduce(function(l,d){return l.indexOf(d.pathwayId)===-1&&l.push(d.pathwayId),l},[])),i&&i.length>1&&(this.updatePathwayPriority(i),n.resolved=this.pathwayId!==o)}},a.filterParsedLevels=function(e){this.levels=e;var t=this.getLevelsForPathway(this.pathwayId);if(t.length===0){var n=e[0].pathwayId;this.log("No levels found in Pathway "+this.pathwayId+'. Setting initial Pathway to "'+n+'"'),t=this.getLevelsForPathway(n),this.pathwayId=n}return t.length!==e.length?(this.log("Found "+t.length+"/"+e.length+' levels in Pathway "'+this.pathwayId+'"'),t):e},a.getLevelsForPathway=function(e){return this.levels===null?[]:this.levels.filter(function(t){return e===t.pathwayId})},a.updatePathwayPriority=function(e){this.pathwayPriority=e;var t,n=this.penalizedPathways,i=performance.now();Object.keys(n).forEach(function(p){i-n[p]>Qs&&delete n[p]});for(var o=0;o<e.length;o++){var l=e[o];if(!n[l]){if(l===this.pathwayId)return;var d=this.hls.nextLoadLevel,u=this.hls.levels[d];if(t=this.getLevelsForPathway(l),t.length>0){this.log('Setting Pathway to "'+l+'"'),this.pathwayId=l,this.hls.trigger(S.LEVELS_UPDATED,{levels:t});var c=this.hls.levels[d];u&&c&&this.levels&&(c.attrs["STABLE-VARIANT-ID"]!==u.attrs["STABLE-VARIANT-ID"]&&c.bitrate!==u.bitrate&&this.log("Unstable Pathways change from bitrate "+u.bitrate+" to "+c.bitrate),this.hls.nextLoadLevel=d);break}}}},a.clonePathways=function(e){var t=this,n=this.levels;if(!!n){var i={},o={};e.forEach(function(l){var d=l.ID,u=l["BASE-ID"],c=l["URI-REPLACEMENT"];if(!n.some(function(g){return g.pathwayId===d})){var p=t.getLevelsForPathway(u).map(function(g){var y=q({},g);y.details=void 0,y.url=Uo(g.uri,g.attrs["STABLE-VARIANT-ID"],"PER-VARIANT-URIS",c);var b=new ne(g.attrs);b["PATHWAY-ID"]=d;var E=b.AUDIO&&b.AUDIO+"_clone_"+d,C=b.SUBTITLES&&b.SUBTITLES+"_clone_"+d;E&&(i[b.AUDIO]=E,b.AUDIO=E),C&&(o[b.SUBTITLES]=C,b.SUBTITLES=C),y.attrs=b;var w=new mn(y);return yn(w,"audio",E),yn(w,"text",C),w});n.push.apply(n,p),No(t.audioTracks,i,c,d),No(t.subtitleTracks,o,c,d)}})}},a.loadSteeringManifest=function(e){var t=this,n=this.hls.config,i=n.loader;this.loader&&this.loader.destroy(),this.loader=new i(n);var o;try{o=new self.URL(e)}catch(y){this.enabled=!1,this.log("Failed to parse Steering Manifest URI: "+e);return}if(o.protocol!=="data:"){var l=(this.hls.bandwidthEstimate||n.abrEwmaDefaultEstimate)|0;o.searchParams.set("_HLS_pathway",this.pathwayId),o.searchParams.set("_HLS_throughput",""+l)}var d={responseType:"json",url:o.href},u=n.steeringManifestLoadPolicy.default,c=u.errorRetry||u.timeoutRetry||{},p={loadPolicy:u,timeout:u.maxLoadTimeMs,maxRetry:c.maxNumRetry||0,retryDelay:c.retryDelayMs||0,maxRetryDelay:c.maxRetryDelayMs||0},g={onSuccess:function(b,E,C,w){t.log('Loaded steering manifest: "'+o+'"');var k=b.data;if(k.VERSION!==1){t.log("Steering VERSION "+k.VERSION+" not supported!");return}t.updated=performance.now(),t.timeToLoad=k.TTL;var D=k["RELOAD-URI"],_=k["PATHWAY-CLONES"],F=k["PATHWAY-PRIORITY"];if(D)try{t.uri=new self.URL(D,o).href}catch(O){t.enabled=!1,t.log("Failed to parse Steering Manifest RELOAD-URI: "+D);return}t.scheduleRefresh(t.uri||C.url),_&&t.clonePathways(_),F&&t.updatePathwayPriority(F)},onError:function(b,E,C,w){if(t.log("Error loading steering manifest: "+b.code+" "+b.text+" ("+E.url+")"),t.stopLoad(),b.code===410){t.enabled=!1,t.log("Steering manifest "+E.url+" no longer available");return}var k=t.timeToLoad*1e3;if(b.code===429){var D=t.loader;if(typeof(D==null?void 0:D.getResponseHeader)=="function"){var _=D.getResponseHeader("Retry-After");_&&(k=parseFloat(_)*1e3)}t.log("Steering manifest "+E.url+" rate limited");return}t.scheduleRefresh(t.uri||E.url,k)},onTimeout:function(b,E,C){t.log("Timeout loading steering manifest ("+E.url+")"),t.scheduleRefresh(t.uri||E.url)}};this.log("Requesting steering manifest: "+o),this.loader.load(d,p,g)},a.scheduleRefresh=function(e,t){var n=this;t===void 0&&(t=this.timeToLoad*1e3),self.clearTimeout(this.reloadTimer),this.reloadTimer=self.setTimeout(function(){n.loadSteeringManifest(e)},t)},s}();function No(s,a,r,e){!s||Object.keys(a).forEach(function(t){var n=s.filter(function(i){return i.groupId===t}).map(function(i){var o=q({},i);return o.details=void 0,o.attrs=new ne(o.attrs),o.url=o.attrs.URI=Uo(i.url,i.attrs["STABLE-RENDITION-ID"],"PER-RENDITION-URIS",r),o.groupId=o.attrs["GROUP-ID"]=a[t],o.attrs["PATHWAY-ID"]=e,o});s.push.apply(s,n)})}function Uo(s,a,r,e){var t=e.HOST,n=e.PARAMS,i=e[r],o;a&&(o=i==null?void 0:i[a],o&&(s=o));var l=new self.URL(s);return t&&!o&&(l.host=t),n&&Object.keys(n).sort().forEach(function(d){d&&l.searchParams.set(d,n[d])}),l.href}var Zs=/^age:\s*[\d.]+\s*$/im,Ko=function(){function s(r){this.xhrSetup=void 0,this.requestTimeout=void 0,this.retryTimeout=void 0,this.retryDelay=void 0,this.config=null,this.callbacks=null,this.context=void 0,this.loader=null,this.stats=void 0,this.xhrSetup=r&&r.xhrSetup||null,this.stats=new on,this.retryDelay=0}var a=s.prototype;return a.destroy=function(){this.callbacks=null,this.abortInternal(),this.loader=null,this.config=null},a.abortInternal=function(){var e=this.loader;self.clearTimeout(this.requestTimeout),self.clearTimeout(this.retryTimeout),e&&(e.onreadystatechange=null,e.onprogress=null,e.readyState!==4&&(this.stats.aborted=!0,e.abort()))},a.abort=function(){var e;this.abortInternal(),(e=this.callbacks)!=null&&e.onAbort&&this.callbacks.onAbort(this.stats,this.context,this.loader)},a.load=function(e,t,n){if(this.stats.loading.start)throw new Error("Loader can only be used once.");this.stats.loading.start=self.performance.now(),this.context=e,this.config=t,this.callbacks=n,this.loadInternal()},a.loadInternal=function(){var e=this,t=this.config,n=this.context;if(!!t){var i=this.loader=new self.XMLHttpRequest,o=this.stats;o.loading.first=0,o.loaded=0;var l=this.xhrSetup;l?Promise.resolve().then(function(){if(!e.stats.aborted)return l(i,n.url)}).catch(function(d){return i.open("GET",n.url,!0),l(i,n.url)}).then(function(){e.stats.aborted||e.openAndSendXhr(i,n,t)}).catch(function(d){e.callbacks.onError({code:i.status,text:d.message},n,i,o)}):this.openAndSendXhr(i,n,t)}},a.openAndSendXhr=function(e,t,n){e.readyState||e.open("GET",t.url,!0);var i=this.context.headers,o=n.loadPolicy,l=o.maxTimeToFirstByteMs,d=o.maxLoadTimeMs;if(i)for(var u in i)e.setRequestHeader(u,i[u]);t.rangeEnd&&e.setRequestHeader("Range","bytes="+t.rangeStart+"-"+(t.rangeEnd-1)),e.onreadystatechange=this.readystatechange.bind(this),e.onprogress=this.loadprogress.bind(this),e.responseType=t.responseType,self.clearTimeout(this.requestTimeout),n.timeout=l&&oe(l)?l:d,this.requestTimeout=self.setTimeout(this.loadtimeout.bind(this),n.timeout),e.send()},a.readystatechange=function(){var e=this.context,t=this.loader,n=this.stats;if(!(!e||!t)){var i=t.readyState,o=this.config;if(!n.aborted&&i>=2&&(n.loading.first===0&&(n.loading.first=Math.max(self.performance.now(),n.loading.start),o.timeout!==o.loadPolicy.maxLoadTimeMs&&(self.clearTimeout(this.requestTimeout),o.timeout=o.loadPolicy.maxLoadTimeMs,this.requestTimeout=self.setTimeout(this.loadtimeout.bind(this),o.loadPolicy.maxLoadTimeMs-(n.loading.first-n.loading.start)))),i===4)){self.clearTimeout(this.requestTimeout),t.onreadystatechange=null,t.onprogress=null;var l=t.status,d=t.responseType!=="text";if(l>=200&&l<300&&(d&&t.response||t.responseText!==null)){n.loading.end=Math.max(self.performance.now(),n.loading.first);var u=d?t.response:t.responseText,c=t.responseType==="arraybuffer"?u.byteLength:u.length;if(n.loaded=n.total=c,n.bwEstimate=n.total*8e3/(n.loading.end-n.loading.first),!this.callbacks)return;var p=this.callbacks.onProgress;if(p&&p(n,e,u,t),!this.callbacks)return;var g={url:t.responseURL,data:u,code:l};this.callbacks.onSuccess(g,n,e,t)}else{var y=o.loadPolicy.errorRetry,b=n.retry;Vn(y,b,!1,l)?this.retry(y):(P.error(l+" while loading "+e.url),this.callbacks.onError({code:l,text:t.statusText},e,t,n))}}}},a.loadtimeout=function(){var e,t=(e=this.config)==null?void 0:e.loadPolicy.timeoutRetry,n=this.stats.retry;if(Vn(t,n,!0))this.retry(t);else{P.warn("timeout while loading "+this.context.url);var i=this.callbacks;i&&(this.abortInternal(),i.onTimeout(this.stats,this.context,this.loader))}},a.retry=function(e){var t=this.context,n=this.stats;this.retryDelay=Lr(e,n.retry),n.retry++,P.warn((status?"HTTP Status "+status:"Timeout")+" while loading "+t.url+", retrying "+n.retry+"/"+e.maxNumRetry+" in "+this.retryDelay+"ms"),this.abortInternal(),this.loader=null,self.clearTimeout(this.retryTimeout),this.retryTimeout=self.setTimeout(this.loadInternal.bind(this),this.retryDelay)},a.loadprogress=function(e){var t=this.stats;t.loaded=e.loaded,e.lengthComputable&&(t.total=e.total)},a.getCacheAge=function(){var e=null;if(this.loader&&Zs.test(this.loader.getAllResponseHeaders())){var t=this.loader.getResponseHeader("age");e=t?parseFloat(t):null}return e},a.getResponseHeader=function(e){return this.loader&&new RegExp("^"+e+":\\s*[\\d.]+\\s*$","im").test(this.loader.getAllResponseHeaders())?this.loader.getResponseHeader(e):null},s}();function Js(){if(self.fetch&&self.AbortController&&self.ReadableStream&&self.Request)try{return new self.ReadableStream({}),!0}catch(s){}return!1}var $s=/(\d+)-(\d+)\/(\d+)/,Go=function(){function s(r){this.fetchSetup=void 0,this.requestTimeout=void 0,this.request=void 0,this.response=void 0,this.controller=void 0,this.context=void 0,this.config=null,this.callbacks=null,this.stats=void 0,this.loader=null,this.fetchSetup=r.fetchSetup||rl,this.controller=new self.AbortController,this.stats=new on}var a=s.prototype;return a.destroy=function(){this.loader=this.callbacks=null,this.abortInternal()},a.abortInternal=function(){var e=this.response;e!=null&&e.ok||(this.stats.aborted=!0,this.controller.abort())},a.abort=function(){var e;this.abortInternal(),(e=this.callbacks)!=null&&e.onAbort&&this.callbacks.onAbort(this.stats,this.context,this.response)},a.load=function(e,t,n){var i=this,o=this.stats;if(o.loading.start)throw new Error("Loader can only be used once.");o.loading.start=self.performance.now();var l=el(e,this.controller.signal),d=n.onProgress,u=e.responseType==="arraybuffer",c=u?"byteLength":"length",p=t.loadPolicy,g=p.maxTimeToFirstByteMs,y=p.maxLoadTimeMs;this.context=e,this.config=t,this.callbacks=n,this.request=this.fetchSetup(e,l),self.clearTimeout(this.requestTimeout),t.timeout=g&&oe(g)?g:y,this.requestTimeout=self.setTimeout(function(){i.abortInternal(),n.onTimeout(o,e,i.response)},t.timeout),self.fetch(this.request).then(function(b){i.response=i.loader=b;var E=Math.max(self.performance.now(),o.loading.start);if(self.clearTimeout(i.requestTimeout),t.timeout=y,i.requestTimeout=self.setTimeout(function(){i.abortInternal(),n.onTimeout(o,e,i.response)},y-(E-o.loading.start)),!b.ok){var C=b.status,w=b.statusText;throw new il(w||"fetch, bad network response",C,b)}return o.loading.first=E,o.total=nl(b.headers)||o.total,d&&oe(t.highWaterMark)?i.loadProgressively(b,o,e,t.highWaterMark,d):u?b.arrayBuffer():e.responseType==="json"?b.json():b.text()}).then(function(b){var E=i.response;self.clearTimeout(i.requestTimeout),o.loading.end=Math.max(self.performance.now(),o.loading.first);var C=b[c];C&&(o.loaded=o.total=C);var w={url:E.url,data:b,code:E.status};d&&!oe(t.highWaterMark)&&d(o,e,b,E),n.onSuccess(w,o,e,E)}).catch(function(b){if(self.clearTimeout(i.requestTimeout),!o.aborted){var E=b&&b.code||0,C=b?b.message:null;n.onError({code:E,text:C},e,b?b.details:null,o)}})},a.getCacheAge=function(){var e=null;if(this.response){var t=this.response.headers.get("age");e=t?parseFloat(t):null}return e},a.getResponseHeader=function(e){return this.response?this.response.headers.get(e):null},a.loadProgressively=function(e,t,n,i,o){i===void 0&&(i=0);var l=new po,d=e.body.getReader(),u=function c(){return d.read().then(function(p){if(p.done)return l.dataLength&&o(t,n,l.flush(),e),Promise.resolve(new ArrayBuffer(0));var g=p.value,y=g.length;return t.loaded+=y,y<i||l.dataLength?(l.push(g),l.dataLength>=i&&o(t,n,l.flush(),e)):o(t,n,g,e),c()}).catch(function(){return Promise.reject()})};return u()},s}();function el(s,a){var r={method:"GET",mode:"cors",credentials:"same-origin",signal:a,headers:new self.Headers(q({},s.headers))};return s.rangeEnd&&r.headers.set("Range","bytes="+s.rangeStart+"-"+String(s.rangeEnd-1)),r}function tl(s){var a=$s.exec(s);if(a)return parseInt(a[2])-parseInt(a[1])+1}function nl(s){var a=s.get("Content-Range");if(a){var r=tl(a);if(oe(r))return r}var e=s.get("Content-Length");if(e)return parseInt(e)}function rl(s,a){return new self.Request(s.url,a)}var il=function(s){j(a,s);function a(r,e,t){var n;return n=s.call(this,r)||this,n.code=void 0,n.details=void 0,n.code=e,n.details=t,n}return a}(ie(Error)),al=/\s/,ol={newCue:function(a,r,e,t){for(var n=[],i,o,l,d,u,c=self.VTTCue||self.TextTrackCue,p=0;p<t.rows.length;p++)if(i=t.rows[p],l=!0,d=0,u="",!i.isEmpty()){for(var g,y=0;y<i.chars.length;y++)al.test(i.chars[y].uchar)&&l?d++:(u+=i.chars[y].uchar,l=!1);i.cueStartTime=r,r===e&&(e+=1e-4),d>=16?d--:d++;var b=Io(u.trim()),E=eo(r,e,b);a!=null&&(g=a.cues)!=null&&g.getCueById(E)||(o=new c(r,e,b),o.id=E,o.line=p+1,o.align="left",o.position=10+Math.min(80,Math.floor(d*8/32)*10),n.push(o))}return a&&n.length&&(n.sort(function(C,w){return C.line==="auto"||w.line==="auto"?0:C.line>8&&w.line>8?w.line-C.line:C.line-w.line}),n.forEach(function(C){return li(a,C)})),n}},sl={maxTimeToFirstByteMs:8e3,maxLoadTimeMs:2e4,timeoutRetry:null,errorRetry:null},ll=Ke(Ke({autoStartLoad:!0,startPosition:-1,defaultAudioCodec:void 0,debug:!1,capLevelOnFPSDrop:!1,capLevelToPlayerSize:!1,ignoreDevicePixelRatio:!1,initialLiveManifestSize:1,maxBufferLength:30,backBufferLength:Infinity,maxBufferSize:60*1e3*1e3,maxBufferHole:.1,highBufferWatchdogPeriod:2,nudgeOffset:.1,nudgeMaxRetry:3,maxFragLookUpTolerance:.25,liveSyncDurationCount:3,liveMaxLatencyDurationCount:Infinity,liveSyncDuration:void 0,liveMaxLatencyDuration:void 0,maxLiveSyncPlaybackRate:1,liveDurationInfinity:!1,liveBackBufferLength:null,maxMaxBufferLength:600,enableWorker:!0,workerPath:null,enableSoftwareAES:!0,startLevel:void 0,startFragPrefetch:!1,fpsDroppedMonitoringPeriod:5e3,fpsDroppedMonitoringThreshold:.2,appendErrorMaxRetry:3,loader:Ko,fLoader:void 0,pLoader:void 0,xhrSetup:void 0,licenseXhrSetup:void 0,licenseResponseCallback:void 0,abrController:us,bufferController:As,capLevelController:Ys,errorController:pa,fpsController:Ws,stretchShortVideoTrack:!1,maxAudioFramesDrift:1,forceKeyFrameOnDiscontinuity:!0,abrEwmaFastLive:3,abrEwmaSlowLive:9,abrEwmaFastVoD:3,abrEwmaSlowVoD:9,abrEwmaDefaultEstimate:5e5,abrBandWidthFactor:.95,abrBandWidthUpFactor:.7,abrMaxWithRealBitrate:!1,maxStarvationDelay:4,maxLoadingDelay:4,minAutoBitrate:0,emeEnabled:!1,widevineLicenseUrl:void 0,drmSystems:{},drmSystemOptions:{},requestMediaKeySystemAccessFunc:ar,testBandwidth:!0,progressive:!1,lowLatencyMode:!0,cmcd:void 0,enableDateRangeMetadataCues:!0,enableEmsgMetadataCues:!0,enableID3MetadataCues:!0,certLoadPolicy:{default:sl},keyLoadPolicy:{default:{maxTimeToFirstByteMs:8e3,maxLoadTimeMs:2e4,timeoutRetry:{maxNumRetry:1,retryDelayMs:1e3,maxRetryDelayMs:2e4,backoff:"linear"},errorRetry:{maxNumRetry:8,retryDelayMs:1e3,maxRetryDelayMs:2e4,backoff:"linear"}}},manifestLoadPolicy:{default:{maxTimeToFirstByteMs:Infinity,maxLoadTimeMs:2e4,timeoutRetry:{maxNumRetry:2,retryDelayMs:0,maxRetryDelayMs:0},errorRetry:{maxNumRetry:1,retryDelayMs:1e3,maxRetryDelayMs:8e3}}},playlistLoadPolicy:{default:{maxTimeToFirstByteMs:1e4,maxLoadTimeMs:2e4,timeoutRetry:{maxNumRetry:2,retryDelayMs:0,maxRetryDelayMs:0},errorRetry:{maxNumRetry:2,retryDelayMs:1e3,maxRetryDelayMs:8e3}}},fragLoadPolicy:{default:{maxTimeToFirstByteMs:1e4,maxLoadTimeMs:12e4,timeoutRetry:{maxNumRetry:4,retryDelayMs:0,maxRetryDelayMs:0},errorRetry:{maxNumRetry:6,retryDelayMs:1e3,maxRetryDelayMs:8e3}}},steeringManifestLoadPolicy:{default:{maxTimeToFirstByteMs:1e4,maxLoadTimeMs:2e4,timeoutRetry:{maxNumRetry:2,retryDelayMs:0,maxRetryDelayMs:0},errorRetry:{maxNumRetry:1,retryDelayMs:1e3,maxRetryDelayMs:8e3}}},manifestLoadingTimeOut:1e4,manifestLoadingMaxRetry:1,manifestLoadingRetryDelay:1e3,manifestLoadingMaxRetryTimeout:64e3,levelLoadingTimeOut:1e4,levelLoadingMaxRetry:4,levelLoadingRetryDelay:1e3,levelLoadingMaxRetryTimeout:64e3,fragLoadingTimeOut:2e4,fragLoadingMaxRetry:6,fragLoadingRetryDelay:1e3,fragLoadingMaxRetryTimeout:64e3},dl()),{},{subtitleStreamController:vs,subtitleTrackController:gs,timelineController:Gs,audioStreamController:fs,audioTrackController:hs,emeController:Fo,cmcdController:js,contentSteeringController:Xs});function dl(){return{cueHandler:ol,enableWebVTT:!0,enableIMSC1:!0,enableCEA708Captions:!0,captionsTextTrack1Label:"English",captionsTextTrack1LanguageCode:"en",captionsTextTrack2Label:"Spanish",captionsTextTrack2LanguageCode:"es",captionsTextTrack3Label:"Unknown CC",captionsTextTrack3LanguageCode:"",captionsTextTrack4Label:"Unknown CC",captionsTextTrack4LanguageCode:"",renderTextTracksNatively:!0}}function ul(s,a){if((a.liveSyncDurationCount||a.liveMaxLatencyDurationCount)&&(a.liveSyncDuration||a.liveMaxLatencyDuration))throw new Error("Illegal hls.js config: don't mix up liveSyncDurationCount/liveMaxLatencyDurationCount and liveSyncDuration/liveMaxLatencyDuration");if(a.liveMaxLatencyDurationCount!==void 0&&(a.liveSyncDurationCount===void 0||a.liveMaxLatencyDurationCount<=a.liveSyncDurationCount))throw new Error('Illegal hls.js config: "liveMaxLatencyDurationCount" must be greater than "liveSyncDurationCount"');if(a.liveMaxLatencyDuration!==void 0&&(a.liveSyncDuration===void 0||a.liveMaxLatencyDuration<=a.liveSyncDuration))throw new Error('Illegal hls.js config: "liveMaxLatencyDuration" must be greater than "liveSyncDuration"');var r=ao(s),e=["manifest","level","frag"],t=["TimeOut","MaxRetry","RetryDelay","MaxRetryTimeout"];return e.forEach(function(n){var i=(n==="level"?"playlist":n)+"LoadPolicy",o=a[i]===void 0,l=[];t.forEach(function(d){var u=n+"Loading"+d,c=a[u];if(c!==void 0&&o){l.push(u);var p=r[i].default;switch(a[i]={default:p},d){case"TimeOut":p.maxLoadTimeMs=c,p.maxTimeToFirstByteMs=c;break;case"MaxRetry":p.errorRetry.maxNumRetry=c,p.timeoutRetry.maxNumRetry=c;break;case"RetryDelay":p.errorRetry.retryDelayMs=c,p.timeoutRetry.retryDelayMs=c;break;case"MaxRetryTimeout":p.errorRetry.maxRetryDelayMs=c,p.timeoutRetry.maxRetryDelayMs=c;break}}}),l.length&&P.warn('hls.js config: "'+l.join('", "')+'" setting(s) are deprecated, use "'+i+'": '+JSON.stringify(a[i]))}),Ke(Ke({},r),a)}function ao(s){return s&&typeof s=="object"?Array.isArray(s)?s.map(ao):Object.keys(s).reduce(function(a,r){return a[r]=ao(s[r]),a},{}):s}function cl(s){var a=s.loader;if(a!==Go&&a!==Ko)P.log("[config]: Custom loader detected, cannot enable progressive streaming"),s.progressive=!1;else{var r=Js();r&&(s.loader=Go,s.progressive=!0,s.enableSoftwareAES=!0,P.log("[config]: Progressive streaming enabled, using FetchLoader"))}}var Ho=function(){s.isSupported=function(){return Ia()};function s(r){r===void 0&&(r={}),this.config=void 0,this.userConfig=void 0,this.coreComponents=void 0,this.networkControllers=void 0,this._emitter=new Di,this._autoLevelCapping=void 0,this._maxHdcpLevel=null,this.abrController=void 0,this.bufferController=void 0,this.capLevelController=void 0,this.latencyController=void 0,this.levelController=void 0,this.streamController=void 0,this.audioTrackController=void 0,this.subtitleTrackController=void 0,this.emeController=void 0,this.cmcdController=void 0,this._media=null,this.url=null,Ur(r.debug||!1,"Hls instance");var e=this.config=ul(s.DefaultConfig,r);this.userConfig=r,this._autoLevelCapping=-1,e.progressive&&cl(e);var t=e.abrController,n=e.bufferController,i=e.capLevelController,o=e.errorController,l=e.fpsController,d=new o(this),u=this.abrController=new t(this),c=this.bufferController=new n(this),p=this.capLevelController=new i(this),g=new l(this),y=new si(this),b=new ea(this),E=e.contentSteeringController,C=E?new E(this):null,w=this.levelController=new ma(this,C),k=new ga(this),D=new Aa(this.config),_=this.streamController=new ls(this,k,D);p.setStreamController(_),g.setStreamController(_);var F=[y,w,_];C&&F.splice(1,0,C),this.networkControllers=F;var O=[u,c,p,g,b,k];this.audioTrackController=this.createController(e.audioTrackController,F);var M=e.audioStreamController;M&&F.push(new M(this,k,D)),this.subtitleTrackController=this.createController(e.subtitleTrackController,F);var z=e.subtitleStreamController;z&&F.push(new z(this,k,D)),this.createController(e.timelineController,O),D.emeController=this.emeController=this.createController(e.emeController,O),this.cmcdController=this.createController(e.cmcdController,O),this.latencyController=this.createController(ta,O),this.coreComponents=O,F.push(d);var H=d.onErrorOut;typeof H=="function"&&this.on(S.ERROR,H,d)}var a=s.prototype;return a.createController=function(e,t){if(e){var n=new e(this);return t&&t.push(n),n}return null},a.on=function(e,t,n){n===void 0&&(n=this),this._emitter.on(e,t,n)},a.once=function(e,t,n){n===void 0&&(n=this),this._emitter.once(e,t,n)},a.removeAllListeners=function(e){this._emitter.removeAllListeners(e)},a.off=function(e,t,n,i){n===void 0&&(n=this),this._emitter.off(e,t,n,i)},a.listeners=function(e){return this._emitter.listeners(e)},a.emit=function(e,t,n){return this._emitter.emit(e,t,n)},a.trigger=function(e,t){if(this.config.debug)return this.emit(e,e,t);try{return this.emit(e,e,t)}catch(n){P.error("An internal error happened while handling event "+e+'. Error message: "'+n.message+'". Here is a stacktrace:',n),this.trigger(S.ERROR,{type:fe.OTHER_ERROR,details:U.INTERNAL_EXCEPTION,fatal:!1,event:e,error:n})}return!1},a.listenerCount=function(e){return this._emitter.listenerCount(e)},a.destroy=function(){P.log("destroy"),this.trigger(S.DESTROYING,void 0),this.detachMedia(),this.removeAllListeners(),this._autoLevelCapping=-1,this.url=null,this.networkControllers.forEach(function(t){return t.destroy()}),this.networkControllers.length=0,this.coreComponents.forEach(function(t){return t.destroy()}),this.coreComponents.length=0;var e=this.config;e.xhrSetup=e.fetchSetup=void 0,this.userConfig=null},a.attachMedia=function(e){P.log("attachMedia"),this._media=e,this.trigger(S.MEDIA_ATTACHING,{media:e})},a.detachMedia=function(){P.log("detachMedia"),this.trigger(S.MEDIA_DETACHING,void 0),this._media=null},a.loadSource=function(e){this.stopLoad();var t=this.media,n=this.url,i=this.url=ge.buildAbsoluteURL(self.location.href,e,{alwaysNormalize:!0});P.log("loadSource:"+i),t&&n&&n!==i&&this.bufferController.hasSourceTypes()&&(this.detachMedia(),this.attachMedia(t)),this.trigger(S.MANIFEST_LOADING,{url:e})},a.startLoad=function(e){e===void 0&&(e=-1),P.log("startLoad("+e+")"),this.networkControllers.forEach(function(t){t.startLoad(e)})},a.stopLoad=function(){P.log("stopLoad"),this.networkControllers.forEach(function(e){e.stopLoad()})},a.swapAudioCodec=function(){P.log("swapAudioCodec"),this.streamController.swapAudioCodec()},a.recoverMediaError=function(){P.log("recoverMediaError");var e=this._media;this.detachMedia(),e&&this.attachMedia(e)},a.removeLevel=function(e,t){t===void 0&&(t=0),this.levelController.removeLevel(e,t)},Q(s,[{key:"levels",get:function(){var e=this.levelController.levels;return e||[]}},{key:"currentLevel",get:function(){return this.streamController.currentLevel},set:function(e){P.log("set currentLevel:"+e),this.loadLevel=e,this.abrController.clearTimer(),this.streamController.immediateLevelSwitch()}},{key:"nextLevel",get:function(){return this.streamController.nextLevel},set:function(e){P.log("set nextLevel:"+e),this.levelController.manualLevel=e,this.streamController.nextLevelSwitch()}},{key:"loadLevel",get:function(){return this.levelController.level},set:function(e){P.log("set loadLevel:"+e),this.levelController.manualLevel=e}},{key:"nextLoadLevel",get:function(){return this.levelController.nextLoadLevel},set:function(e){this.levelController.nextLoadLevel=e}},{key:"firstLevel",get:function(){return Math.max(this.levelController.firstLevel,this.minAutoLevel)},set:function(e){P.log("set firstLevel:"+e),this.levelController.firstLevel=e}},{key:"startLevel",get:function(){return this.levelController.startLevel},set:function(e){P.log("set startLevel:"+e),e!==-1&&(e=Math.max(e,this.minAutoLevel)),this.levelController.startLevel=e}},{key:"capLevelToPlayerSize",get:function(){return this.config.capLevelToPlayerSize},set:function(e){var t=!!e;t!==this.config.capLevelToPlayerSize&&(t?this.capLevelController.startCapping():(this.capLevelController.stopCapping(),this.autoLevelCapping=-1,this.streamController.nextLevelSwitch()),this.config.capLevelToPlayerSize=t)}},{key:"autoLevelCapping",get:function(){return this._autoLevelCapping},set:function(e){this._autoLevelCapping!==e&&(P.log("set autoLevelCapping:"+e),this._autoLevelCapping=e)}},{key:"bandwidthEstimate",get:function(){var e=this.abrController.bwEstimator;return e?e.getEstimate():NaN}},{key:"ttfbEstimate",get:function(){var e=this.abrController.bwEstimator;return e?e.getEstimateTTFB():NaN}},{key:"maxHdcpLevel",get:function(){return this._maxHdcpLevel},set:function(e){Cr.indexOf(e)>-1&&(this._maxHdcpLevel=e)}},{key:"autoLevelEnabled",get:function(){return this.levelController.manualLevel===-1}},{key:"manualLevel",get:function(){return this.levelController.manualLevel}},{key:"minAutoLevel",get:function(){var e=this.levels,t=this.config.minAutoBitrate;if(!e)return 0;for(var n=e.length,i=0;i<n;i++)if(e[i].maxBitrate>=t)return i;return 0}},{key:"maxAutoLevel",get:function(){var e=this.levels,t=this.autoLevelCapping,n=this.maxHdcpLevel,i;if(t===-1&&e&&e.length?i=e.length-1:i=t,n)for(var o=i;o--;){var l=e[o].attrs["HDCP-LEVEL"];if(l&&l<=n)return o}return i}},{key:"nextAutoLevel",get:function(){return Math.min(Math.max(this.abrController.nextAutoLevel,this.minAutoLevel),this.maxAutoLevel)},set:function(e){this.abrController.nextAutoLevel=Math.max(this.minAutoLevel,e)}},{key:"playingDate",get:function(){return this.streamController.currentProgramDateTime}},{key:"mainForwardBufferInfo",get:function(){return this.streamController.getMainFwdBufferInfo()}},{key:"audioTracks",get:function(){var e=this.audioTrackController;return e?e.audioTracks:[]}},{key:"audioTrack",get:function(){var e=this.audioTrackController;return e?e.audioTrack:-1},set:function(e){var t=this.audioTrackController;t&&(t.audioTrack=e)}},{key:"subtitleTracks",get:function(){var e=this.subtitleTrackController;return e?e.subtitleTracks:[]}},{key:"subtitleTrack",get:function(){var e=this.subtitleTrackController;return e?e.subtitleTrack:-1},set:function(e){var t=this.subtitleTrackController;t&&(t.subtitleTrack=e)}},{key:"media",get:function(){return this._media}},{key:"subtitleDisplay",get:function(){var e=this.subtitleTrackController;return e?e.subtitleDisplay:!1},set:function(e){var t=this.subtitleTrackController;t&&(t.subtitleDisplay=e)}},{key:"lowLatencyMode",get:function(){return this.config.lowLatencyMode},set:function(e){this.config.lowLatencyMode=e}},{key:"liveSyncPosition",get:function(){return this.latencyController.liveSyncPosition}},{key:"latency",get:function(){return this.latencyController.latency}},{key:"maxLatency",get:function(){return this.latencyController.maxLatency}},{key:"targetLatency",get:function(){return this.latencyController.targetLatency}},{key:"drift",get:function(){return this.latencyController.drift}},{key:"forceStartLoad",get:function(){return this.streamController.forceStartLoad}}],[{key:"version",get:function(){return"1.4.0"}},{key:"Events",get:function(){return S}},{key:"ErrorTypes",get:function(){return fe}},{key:"ErrorDetails",get:function(){return U}},{key:"DefaultConfig",get:function(){return s.defaultConfig?s.defaultConfig:ll},set:function(e){s.defaultConfig=e}}]),s}();return Ho.defaultConfig=void 0,Ho})})(!1)}}]);
