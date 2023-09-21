var t = t || function (t, e) {
  var r = {}, i = r.lib = {}, n = function () { }, o = i.Base = {
    extend: function (t) {
      n.prototype = this;
      var e = new n();
      return t && e.mixIn(t), e.hasOwnProperty("init") || (e.init = function () {
        e.$super.init.apply(this, arguments);
      }), e.init.prototype = e, e.$super = this, e;
    },
    create: function () {
      var t = this.extend();
      return t.init.apply(t, arguments), t;
    },
    init: function () { },
    mixIn: function (t) {
      for (var e in t) t.hasOwnProperty(e) && (this[e] = t[e]);
      t.hasOwnProperty("toString") && (this.toString = t.toString);
    },
    clone: function () {
      return this.init.prototype.extend(this);
    }
  }, s = i.WordArray = o.extend({
    init: function (t, e) {
      t = this.words = t || [], this.sigBytes = void 0 != e ? e : 4 * t.length;
    },
    toString: function (t) {
      return (t || a).stringify(this);
    },
    concat: function (t) {
      var e = this.words, r = t.words, i = this.sigBytes;
      if (t = t.sigBytes, this.clamp(), i % 4) for (var n = 0; n < t; n++) e[i + n >>> 2] |= (r[n >>> 2] >>> 24 - n % 4 * 8 & 255) << 24 - (i + n) % 4 * 8; else if (65535 < r.length) for (n = 0; n < t; n += 4) e[i + n >>> 2] = r[n >>> 2]; else e.push.apply(e, r);
      return this.sigBytes += t, this;
    },
    clamp: function () {
      var e = this.words, r = this.sigBytes;
      e[r >>> 2] &= 4294967295 << 32 - r % 4 * 8, e.length = t.ceil(r / 4);
    },
    clone: function () {
      var t = o.clone.call(this);
      return t.words = this.words.slice(0), t;
    },
    random: function (e) {
      for (var r = [], i = 0; i < e; i += 4) r.push(4294967296 * t.random() | 0);
      return new s.init(r, e);
    }
  }), c = r.enc = {}, a = c.Hex = {
    stringify: function (t) {
      var e = t.words;
      t = t.sigBytes;
      for (var r = [], i = 0; i < t; i++) {
        var n = e[i >>> 2] >>> 24 - i % 4 * 8 & 255;
        r.push((n >>> 4).toString(16)), r.push((15 & n).toString(16));
      }
      return r.join("");
    },
    parse: function (t) {
      for (var e = t.length, r = [], i = 0; i < e; i += 2) r[i >>> 3] |= parseInt(t.substr(i, 2), 16) << 24 - i % 8 * 4;
      return new s.init(r, e / 2);
    }
  }, f = c.Latin1 = {
    stringify: function (t) {
      var e = t.words;
      t = t.sigBytes;
      for (var r = [], i = 0; i < t; i++) r.push(String.fromCharCode(e[i >>> 2] >>> 24 - i % 4 * 8 & 255));
      return r.join("");
    },
    parse: function (t) {
      for (var e = t.length, r = [], i = 0; i < e; i++) r[i >>> 2] |= (255 & t.charCodeAt(i)) << 24 - i % 4 * 8;
      return new s.init(r, e);
    }
  }, h = c.Utf8 = {
    stringify: function (t) {
      try {
        return decodeURIComponent(escape(f.stringify(t)));
      } catch (t) {
        throw Error("Malformed UTF-8 data");
      }
    },
    parse: function (t) {
      return f.parse(unescape(encodeURIComponent(t)));
    }
  }, u = i.BufferedBlockAlgorithm = o.extend({
    reset: function () {
      this._data = new s.init(), this._nDataBytes = 0;
    },
    _append: function (t) {
      "string" == typeof t && (t = h.parse(t)), this._data.concat(t), this._nDataBytes += t.sigBytes;
    },
    _process: function (e) {
      var r = this._data, i = r.words, n = r.sigBytes, o = this.blockSize, c = n / (4 * o);
      if (e = (c = e ? t.ceil(c) : t.max((0 | c) - this._minBufferSize, 0)) * o, n = t.min(4 * e, n),
        e) {
        for (var a = 0; a < e; a += o) this._doProcessBlock(i, a);
        a = i.splice(0, e), r.sigBytes -= n;
      }
      return new s.init(a, n);
    },
    clone: function () {
      var t = o.clone.call(this);
      return t._data = this._data.clone(), t;
    },
    _minBufferSize: 0
  });
  i.Hasher = u.extend({
    cfg: o.extend(),
    init: function (t) {
      this.cfg = this.cfg.extend(t), this.reset();
    },
    reset: function () {
      u.reset.call(this), this._doReset();
    },
    update: function (t) {
      return this._append(t), this._process(), this;
    },
    finalize: function (t) {
      return t && this._append(t), this._doFinalize();
    },
    blockSize: 16,
    _createHelper: function (t) {
      return function (e, r) {
        return new t.init(r).finalize(e);
      };
    },
    _createHmacHelper: function (t) {
      return function (e, r) {
        return new p.HMAC.init(t, r).finalize(e);
      };
    }
  });
  var p = r.algo = {};
  return r;
}(Math);

!function () {
  var e = t, r = e.lib.WordArray;
  e.enc.Base64 = {
    stringify: function (t) {
      var e = t.words, r = t.sigBytes, i = this._map;
      t.clamp(), t = [];
      for (var n = 0; n < r; n += 3) for (var o = (e[n >>> 2] >>> 24 - n % 4 * 8 & 255) << 16 | (e[n + 1 >>> 2] >>> 24 - (n + 1) % 4 * 8 & 255) << 8 | e[n + 2 >>> 2] >>> 24 - (n + 2) % 4 * 8 & 255, s = 0; 4 > s && n + .75 * s < r; s++) t.push(i.charAt(o >>> 6 * (3 - s) & 63));
      if (e = i.charAt(64)) for (; t.length % 4;) t.push(e);
      return t.join("");
    },
    parse: function (t) {
      var e = t.length, i = this._map;
      (n = i.charAt(64)) && -1 != (n = t.indexOf(n)) && (e = n);
      for (var n = [], o = 0, s = 0; s < e; s++) if (s % 4) {
        var c = i.indexOf(t.charAt(s - 1)) << s % 4 * 2, a = i.indexOf(t.charAt(s)) >>> 6 - s % 4 * 2;
        n[o >>> 2] |= (c | a) << 24 - o % 4 * 8, o++;
      }
      return r.create(n, o);
    },
    _map: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
  };
}(), function (e) {
  function r(t, e, r, i, n, o, s) {
    return ((t = t + (e & r | ~e & i) + n + s) << o | t >>> 32 - o) + e;
  }
  function i(t, e, r, i, n, o, s) {
    return ((t = t + (e & i | r & ~i) + n + s) << o | t >>> 32 - o) + e;
  }
  function n(t, e, r, i, n, o, s) {
    return ((t = t + (e ^ r ^ i) + n + s) << o | t >>> 32 - o) + e;
  }
  function o(t, e, r, i, n, o, s) {
    return ((t = t + (r ^ (e | ~i)) + n + s) << o | t >>> 32 - o) + e;
  }
  for (var s = t, c = (f = s.lib).WordArray, a = f.Hasher, f = s.algo, h = [], u = 0; 64 > u; u++) h[u] = 4294967296 * e.abs(e.sin(u + 1)) | 0;
  f = f.MD5 = a.extend({
    _doReset: function () {
      this._hash = new c.init([1732584193, 4023233417, 2562383102, 271733878]);
    },
    _doProcessBlock: function (t, e) {
      for (s = 0; 16 > s; s++) {
        a = t[c = e + s];
        t[c] = 16711935 & (a << 8 | a >>> 24) | 4278255360 & (a << 24 | a >>> 8);
      }
      var s = this._hash.words, c = t[e + 0], a = t[e + 1], f = t[e + 2], u = t[e + 3], p = t[e + 4], d = t[e + 5], l = t[e + 6], y = t[e + 7], v = t[e + 8], _ = t[e + 9], g = t[e + 10], B = t[e + 11], k = t[e + 12], x = t[e + 13], m = t[e + 14], S = t[e + 15], w = s[0], z = s[1], C = s[2], E = s[3], z = o(z = o(z = o(z = o(z = n(z = n(z = n(z = n(z = i(z = i(z = i(z = i(z = r(z = r(z = r(z = r(z, C = r(C, E = r(E, w = r(w, z, C, E, c, 7, h[0]), z, C, a, 12, h[1]), w, z, f, 17, h[2]), E, w, u, 22, h[3]), C = r(C, E = r(E, w = r(w, z, C, E, p, 7, h[4]), z, C, d, 12, h[5]), w, z, l, 17, h[6]), E, w, y, 22, h[7]), C = r(C, E = r(E, w = r(w, z, C, E, v, 7, h[8]), z, C, _, 12, h[9]), w, z, g, 17, h[10]), E, w, B, 22, h[11]), C = r(C, E = r(E, w = r(w, z, C, E, k, 7, h[12]), z, C, x, 12, h[13]), w, z, m, 17, h[14]), E, w, S, 22, h[15]), C = i(C, E = i(E, w = i(w, z, C, E, a, 5, h[16]), z, C, l, 9, h[17]), w, z, B, 14, h[18]), E, w, c, 20, h[19]), C = i(C, E = i(E, w = i(w, z, C, E, d, 5, h[20]), z, C, g, 9, h[21]), w, z, S, 14, h[22]), E, w, p, 20, h[23]), C = i(C, E = i(E, w = i(w, z, C, E, _, 5, h[24]), z, C, m, 9, h[25]), w, z, u, 14, h[26]), E, w, v, 20, h[27]), C = i(C, E = i(E, w = i(w, z, C, E, x, 5, h[28]), z, C, f, 9, h[29]), w, z, y, 14, h[30]), E, w, k, 20, h[31]), C = n(C, E = n(E, w = n(w, z, C, E, d, 4, h[32]), z, C, v, 11, h[33]), w, z, B, 16, h[34]), E, w, m, 23, h[35]), C = n(C, E = n(E, w = n(w, z, C, E, a, 4, h[36]), z, C, p, 11, h[37]), w, z, y, 16, h[38]), E, w, g, 23, h[39]), C = n(C, E = n(E, w = n(w, z, C, E, x, 4, h[40]), z, C, c, 11, h[41]), w, z, u, 16, h[42]), E, w, l, 23, h[43]), C = n(C, E = n(E, w = n(w, z, C, E, _, 4, h[44]), z, C, k, 11, h[45]), w, z, S, 16, h[46]), E, w, f, 23, h[47]), C = o(C, E = o(E, w = o(w, z, C, E, c, 6, h[48]), z, C, y, 10, h[49]), w, z, m, 15, h[50]), E, w, d, 21, h[51]), C = o(C, E = o(E, w = o(w, z, C, E, k, 6, h[52]), z, C, u, 10, h[53]), w, z, g, 15, h[54]), E, w, a, 21, h[55]), C = o(C, E = o(E, w = o(w, z, C, E, v, 6, h[56]), z, C, S, 10, h[57]), w, z, l, 15, h[58]), E, w, x, 21, h[59]), C = o(C, E = o(E, w = o(w, z, C, E, p, 6, h[60]), z, C, B, 10, h[61]), w, z, f, 15, h[62]), E, w, _, 21, h[63]);
      s[0] = s[0] + w | 0, s[1] = s[1] + z | 0, s[2] = s[2] + C | 0, s[3] = s[3] + E | 0;
    },
    _doFinalize: function () {
      var t = this._data, r = t.words, i = 8 * this._nDataBytes, n = 8 * t.sigBytes;
      r[n >>> 5] |= 128 << 24 - n % 32;
      var o = e.floor(i / 4294967296);
      for (r[15 + (n + 64 >>> 9 << 4)] = 16711935 & (o << 8 | o >>> 24) | 4278255360 & (o << 24 | o >>> 8),
        r[14 + (n + 64 >>> 9 << 4)] = 16711935 & (i << 8 | i >>> 24) | 4278255360 & (i << 24 | i >>> 8),
        t.sigBytes = 4 * (r.length + 1), this._process(), r = (t = this._hash).words, i = 0; 4 > i; i++) n = r[i],
          r[i] = 16711935 & (n << 8 | n >>> 24) | 4278255360 & (n << 24 | n >>> 8);
      return t;
    },
    clone: function () {
      var t = a.clone.call(this);
      return t._hash = this._hash.clone(), t;
    }
  }), s.MD5 = a._createHelper(f), s.HmacMD5 = a._createHmacHelper(f);
}(Math), function () {
  var e = t, r = e.lib, i = r.Base, n = r.WordArray, o = (r = e.algo).EvpKDF = i.extend({
    cfg: i.extend({
      keySize: 4,
      hasher: r.MD5,
      iterations: 1
    }),
    init: function (t) {
      this.cfg = this.cfg.extend(t);
    },
    compute: function (t, e) {
      for (var r = (c = this.cfg).hasher.create(), i = n.create(), o = i.words, s = c.keySize, c = c.iterations; o.length < s;) {
        a && r.update(a);
        var a = r.update(t).finalize(e);
        r.reset();
        for (var f = 1; f < c; f++) a = r.finalize(a), r.reset();
        i.concat(a);
      }
      return i.sigBytes = 4 * s, i;
    }
  });
  e.EvpKDF = function (t, e, r) {
    return o.create(r).compute(t, e);
  };
}(), t.lib.Cipher || function (e) {
  var r = (l = t).lib, i = r.Base, n = r.WordArray, o = r.BufferedBlockAlgorithm, s = l.enc.Base64, c = l.algo.EvpKDF, a = r.Cipher = o.extend({
    cfg: i.extend(),
    createEncryptor: function (t, e) {
      return this.create(this._ENC_XFORM_MODE, t, e);
    },
    createDecryptor: function (t, e) {
      return this.create(this._DEC_XFORM_MODE, t, e);
    },
    init: function (t, e, r) {
      this.cfg = this.cfg.extend(r), this._xformMode = t, this._key = e, this.reset();
    },
    reset: function () {
      o.reset.call(this), this._doReset();
    },
    process: function (t) {
      return this._append(t), this._process();
    },
    finalize: function (t) {
      return t && this._append(t), this._doFinalize();
    },
    keySize: 4,
    ivSize: 4,
    _ENC_XFORM_MODE: 1,
    _DEC_XFORM_MODE: 2,
    _createHelper: function (t) {
      return {
        encrypt: function (e, r, i) {
          return ("string" == typeof r ? y : d).encrypt(t, e, r, i);
        },
        decrypt: function (e, r, i) {
          return ("string" == typeof r ? y : d).decrypt(t, e, r, i);
        }
      };
    }
  });
  r.StreamCipher = a.extend({
    _doFinalize: function () {
      return this._process(!0);
    },
    blockSize: 1
  });
  var f = l.mode = {}, h = function (t, e, r) {
    var i = this._iv;
    i ? this._iv = void 0 : i = this._prevBlock;
    for (var n = 0; n < r; n++) t[e + n] ^= i[n];
  }, u = (r.BlockCipherMode = i.extend({
    createEncryptor: function (t, e) {
      return this.Encryptor.create(t, e);
    },
    createDecryptor: function (t, e) {
      return this.Decryptor.create(t, e);
    },
    init: function (t, e) {
      this._cipher = t, this._iv = e;
    }
  })).extend();
  u.Encryptor = u.extend({
    processBlock: function (t, e) {
      var r = this._cipher, i = r.blockSize;
      h.call(this, t, e, i), r.encryptBlock(t, e), this._prevBlock = t.slice(e, e + i);
    }
  }), u.Decryptor = u.extend({
    processBlock: function (t, e) {
      var r = this._cipher, i = r.blockSize, n = t.slice(e, e + i);
      r.decryptBlock(t, e), h.call(this, t, e, i), this._prevBlock = n;
    }
  }), f = f.CBC = u, u = (l.pad = {}).Pkcs7 = {
    pad: function (t, e) {
      for (var r = 4 * e, i = (r = r - t.sigBytes % r) << 24 | r << 16 | r << 8 | r, o = [], s = 0; s < r; s += 4) o.push(i);
      r = n.create(o, r), t.concat(r);
    },
    unpad: function (t) {
      t.sigBytes -= 255 & t.words[t.sigBytes - 1 >>> 2];
    }
  }, r.BlockCipher = a.extend({
    cfg: a.cfg.extend({
      mode: f,
      padding: u
    }),
    reset: function () {
      a.reset.call(this);
      var t = (e = this.cfg).iv, e = e.mode;
      if (this._xformMode == this._ENC_XFORM_MODE) var r = e.createEncryptor; else r = e.createDecryptor,
        this._minBufferSize = 1;
      this._mode = r.call(e, this, t && t.words);
    },
    _doProcessBlock: function (t, e) {
      this._mode.processBlock(t, e);
    },
    _doFinalize: function () {
      var t = this.cfg.padding;
      if (this._xformMode == this._ENC_XFORM_MODE) {
        t.pad(this._data, this.blockSize);
        var e = this._process(!0);
      } else e = this._process(!0), t.unpad(e);
      return e;
    },
    blockSize: 4
  });
  var p = r.CipherParams = i.extend({
    init: function (t) {
      this.mixIn(t);
    },
    toString: function (t) {
      return (t || this.formatter).stringify(this);
    }
  }), f = (l.format = {}).OpenSSL = {
    stringify: function (t) {
      var e = t.ciphertext;
      return ((t = t.salt) ? n.create([1398893684, 1701076831]).concat(t).concat(e) : e).toString(s);
    },
    parse: function (t) {
      var e = (t = s.parse(t)).words;
      if (1398893684 == e[0] && 1701076831 == e[1]) {
        var r = n.create(e.slice(2, 4));
        e.splice(0, 4), t.sigBytes -= 16;
      }
      return p.create({
        ciphertext: t,
        salt: r
      });
    }
  }, d = r.SerializableCipher = i.extend({
    cfg: i.extend({
      format: f
    }),
    encrypt: function (t, e, r, i) {
      i = this.cfg.extend(i);
      var n = t.createEncryptor(r, i);
      return e = n.finalize(e), n = n.cfg, p.create({
        ciphertext: e,
        key: r,
        iv: n.iv,
        algorithm: t,
        mode: n.mode,
        padding: n.padding,
        blockSize: t.blockSize,
        formatter: i.format
      });
    },
    decrypt: function (t, e, r, i) {
      return i = this.cfg.extend(i), e = this._parse(e, i.format), t.createDecryptor(r, i).finalize(e.ciphertext);
    },
    _parse: function (t, e) {
      return "string" == typeof t ? e.parse(t, this) : t;
    }
  }), l = (l.kdf = {}).OpenSSL = {
    execute: function (t, e, r, i) {
      return i || (i = n.random(8)), t = c.create({
        keySize: e + r
      }).compute(t, i), r = n.create(t.words.slice(e), 4 * r), t.sigBytes = 4 * e, p.create({
        key: t,
        iv: r,
        salt: i
      });
    }
  }, y = r.PasswordBasedCipher = d.extend({
    cfg: d.cfg.extend({
      kdf: l
    }),
    encrypt: function (t, e, r, i) {
      return i = this.cfg.extend(i), r = i.kdf.execute(r, t.keySize, t.ivSize), i.iv = r.iv,
        (t = d.encrypt.call(this, t, e, r.key, i)).mixIn(r), t;
    },
    decrypt: function (t, e, r, i) {
      return i = this.cfg.extend(i), e = this._parse(e, i.format), r = i.kdf.execute(r, t.keySize, t.ivSize, e.salt),
        i.iv = r.iv, d.decrypt.call(this, t, e, r.key, i);
    }
  });
}(), function () {
  for (var e = t, r = e.lib.BlockCipher, i = e.algo, n = [], o = [], s = [], c = [], a = [], f = [], h = [], u = [], p = [], d = [], l = [], y = 0; 256 > y; y++) l[y] = 128 > y ? y << 1 : y << 1 ^ 283;
  for (var v = 0, _ = 0, y = 0; 256 > y; y++) {
    var g = (g = _ ^ _ << 1 ^ _ << 2 ^ _ << 3 ^ _ << 4) >>> 8 ^ 255 & g ^ 99;
    n[v] = g, o[g] = v;
    var B = l[v], k = l[B], x = l[k], m = 257 * l[g] ^ 16843008 * g;
    s[v] = m << 24 | m >>> 8, c[v] = m << 16 | m >>> 16, a[v] = m << 8 | m >>> 24, f[v] = m,
      m = 16843009 * x ^ 65537 * k ^ 257 * B ^ 16843008 * v, h[g] = m << 24 | m >>> 8,
      u[g] = m << 16 | m >>> 16, p[g] = m << 8 | m >>> 24, d[g] = m, v ? (v = B ^ l[l[l[x ^ B]]],
        _ ^= l[l[_]]) : v = _ = 1;
  }
  var S = [0, 1, 2, 4, 8, 16, 32, 64, 128, 27, 54], i = i.AES = r.extend({
    _doReset: function () {
      for (var t = (r = this._key).words, e = r.sigBytes / 4, r = 4 * ((this._nRounds = e + 6) + 1), i = this._keySchedule = [], o = 0; o < r; o++) if (o < e) i[o] = t[o]; else {
        var s = i[o - 1];
        o % e ? 6 < e && 4 == o % e && (s = n[s >>> 24] << 24 | n[s >>> 16 & 255] << 16 | n[s >>> 8 & 255] << 8 | n[255 & s]) : (s = s << 8 | s >>> 24,
          s = n[s >>> 24] << 24 | n[s >>> 16 & 255] << 16 | n[s >>> 8 & 255] << 8 | n[255 & s],
          s ^= S[o / e | 0] << 24), i[o] = i[o - e] ^ s;
      }
      for (t = this._invKeySchedule = [], e = 0; e < r; e++) o = r - e, s = e % 4 ? i[o] : i[o - 4],
        t[e] = 4 > e || 4 >= o ? s : h[n[s >>> 24]] ^ u[n[s >>> 16 & 255]] ^ p[n[s >>> 8 & 255]] ^ d[n[255 & s]];
    },
    encryptBlock: function (t, e) {
      this._doCryptBlock(t, e, this._keySchedule, s, c, a, f, n);
    },
    decryptBlock: function (t, e) {
      var r = t[e + 1];
      t[e + 1] = t[e + 3], t[e + 3] = r, this._doCryptBlock(t, e, this._invKeySchedule, h, u, p, d, o),
        r = t[e + 1], t[e + 1] = t[e + 3], t[e + 3] = r;
    },
    _doCryptBlock: function (t, e, r, i, n, o, s, c) {
      for (var a = this._nRounds, f = t[e] ^ r[0], h = t[e + 1] ^ r[1], u = t[e + 2] ^ r[2], p = t[e + 3] ^ r[3], d = 4, l = 1; l < a; l++) var y = i[f >>> 24] ^ n[h >>> 16 & 255] ^ o[u >>> 8 & 255] ^ s[255 & p] ^ r[d++], v = i[h >>> 24] ^ n[u >>> 16 & 255] ^ o[p >>> 8 & 255] ^ s[255 & f] ^ r[d++], _ = i[u >>> 24] ^ n[p >>> 16 & 255] ^ o[f >>> 8 & 255] ^ s[255 & h] ^ r[d++], p = i[p >>> 24] ^ n[f >>> 16 & 255] ^ o[h >>> 8 & 255] ^ s[255 & u] ^ r[d++], f = y, h = v, u = _;
      y = (c[f >>> 24] << 24 | c[h >>> 16 & 255] << 16 | c[u >>> 8 & 255] << 8 | c[255 & p]) ^ r[d++],
        v = (c[h >>> 24] << 24 | c[u >>> 16 & 255] << 16 | c[p >>> 8 & 255] << 8 | c[255 & f]) ^ r[d++],
        _ = (c[u >>> 24] << 24 | c[p >>> 16 & 255] << 16 | c[f >>> 8 & 255] << 8 | c[255 & h]) ^ r[d++],
        p = (c[p >>> 24] << 24 | c[f >>> 16 & 255] << 16 | c[h >>> 8 & 255] << 8 | c[255 & u]) ^ r[d++],
        t[e] = y, t[e + 1] = v, t[e + 2] = _, t[e + 3] = p;
    },
    keySize: 8
  });
  e.AES = r._createHelper(i);
}(), t.enc.u8array = {
  stringify: function (t) {
    for (var e = t.words, r = t.sigBytes, i = new Uint8Array(r), n = 0; n < r; n++) {
      var o = e[n >>> 2] >>> 24 - n % 4 * 8 & 255;
      i[n] = o;
    }
    return i;
  },
  parse: function (e) {
    for (var r = e.length, i = [], n = 0; n < r; n++) i[n >>> 2] |= (255 & e[n]) << 24 - n % 4 * 8;
    return t.lib.WordArray.create(i, r);
  }
}, t.enc.int8array = {
  stringify: function (t) {
    for (var e = t.words, r = t.sigBytes, i = new Int8Array(r), n = 0; n < r; n++) {
      var o = e[n >>> 2] >> 24 - n % 4 * 8 & 255;
      i[n] = o;
    }
    return i;
  },
  parse: function (e) {
    for (var r = e.length, i = [], n = 0; n < r; n++) i[n >>> 2] |= (255 & e[n]) << 24 - n % 4 * 8;
    return t.lib.WordArray.create(i, r);
  }
}, t.enc.int16array = {
  stringify: function (t) {
    for (var e = t.words, r = t.sigBytes, i = new Uint8Array(r), n = 0; n < r; n++) {
      var o = e[n >>> 2] >>> 24 - n % 4 * 8 & 255;
      i[n] = o;
    }
    return i;
  },
  parse: function (e) {
    for (var r = e.length, i = [], n = 0; n < r; n++) i[n >>> 2] |= (255 & e[n]) << 24 - n % 4 * 8;
    return t.lib.WordArray.create(i, r);
  }
}, t.mode.CFB = function () {
  function e(t, e, r, i) {
    var n = this._iv;
    if (n) {
      o = n.slice(0);
      this._iv = void 0;
    } else var o = this._prevBlock;
    i.encryptBlock(o, 0);
    for (var s = 0; s < r; s++) t[e + s] ^= o[s];
  }
  var r = t.lib.BlockCipherMode.extend();
  return r.Encryptor = r.extend({
    processBlock: function (t, r) {
      var i = this._cipher, n = i.blockSize;
      e.call(this, t, r, n, i), this._prevBlock = t.slice(r, r + n);
    }
  }), r.Decryptor = r.extend({
    processBlock: function (t, r) {
      var i = this._cipher, n = i.blockSize, o = t.slice(r, r + n);
      e.call(this, t, r, n, i), this._prevBlock = o;
    }
  }), r;
}(), t.mode.ECB = function () {
  var e = t.lib.BlockCipherMode.extend();
  return e.Encryptor = e.extend({
    processBlock: function (t, e) {
      this._cipher.encryptBlock(t, e);
    }
  }), e.Decryptor = e.extend({
    processBlock: function (t, e) {
      this._cipher.decryptBlock(t, e);
    }
  }), e;
}(), t.pad.NoPadding = {
  pad: function () { },
  unpad: function () { }
}, module.exports = {
  CryptoJS: t
};