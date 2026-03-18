const M = (e) => {
  const n = typeof e;
  return n !== "function" && n !== "object" || e === null;
}, k = (e) => {
  const n = e.flags === "" ? void 0 : e.flags;
  return new RegExp(e.source, n);
}, u = (e, n = /* @__PURE__ */ new WeakMap()) => {
  if (e === null || M(e))
    return e;
  if (n.has(e))
    return n.get(e);
  if (e instanceof RegExp)
    return k(e);
  if (e instanceof Date)
    return new Date(e.getTime());
  if (e instanceof Function)
    return e;
  if (e instanceof Map) {
    const r = /* @__PURE__ */ new Map();
    return n.set(e, r), e.forEach((o, s) => {
      r.set(s, u(o, n));
    }), r;
  }
  if (e instanceof Set) {
    const r = /* @__PURE__ */ new Set();
    n.set(e, r);
    for (const o of e)
      r.add(u(o, n));
    return r;
  }
  if (Array.isArray(e)) {
    const r = [];
    return n.set(e, r), e.forEach((o) => {
      r.push(u(o, n));
    }), r;
  }
  const t = {};
  n.set(e, t);
  for (const r in e)
    Object.prototype.hasOwnProperty.call(e, r) && (t[r] = u(e[r], n));
  return t;
}, x = (e, n = 200) => {
  let t = 0;
  return (...r) => new Promise((o) => {
    t && (clearTimeout(t), o("cancel")), t = window.setTimeout(() => {
      e.apply(void 0, r), t = 0, o("done");
    }, n);
  });
}, L = (e, n = {
  _blank: !0,
  nofollow: !0
}) => {
  const t = document.createElement("a");
  t.href = e, n._blank && (t.target = "_blank"), n.nofollow && (t.rel = "noopener noreferrer"), t.click();
}, R = (e, n = "") => {
  const t = document.createElement("a");
  t.setAttribute("href", e), t.setAttribute("download", n), t.click();
}, C = () => {
  let e = -1;
  return (t, r, o, s = 100) => {
    const c = () => {
      o && (typeof s == "number" ? setTimeout(o, s) : o());
    };
    e !== -1 && (cancelAnimationFrame(e), c());
    let i = t.scrollTop;
    const l = () => {
      e = -1;
      const a = r - i;
      i = i + a / 5, Math.abs(a) < 1 ? (t.scrollTo(0, r), c()) : (t.scrollTo(0, i), e = requestAnimationFrame(l));
    };
    e = requestAnimationFrame(l);
  };
}, $ = C(), F = (e, n = 200) => {
  let t = 0, r = null;
  const o = (s) => {
    t === 0 && (t = s), s - t >= n ? (e.apply(void 0, r), r = null, t = 0) : window.requestAnimationFrame(o);
  };
  return (...s) => {
    r === null && window.requestAnimationFrame(o), r = s;
  };
}, O = (e, n = !0) => {
  if (!e)
    return {};
  const t = {};
  return e.replace(/^\?/, "").split("&").forEach((r) => {
    const [o, s] = r.split("=");
    if (o) {
      const c = n ? decodeURIComponent(o) : o, i = n ? decodeURIComponent(s) : s;
      t[c] !== void 0 ? (Array.isArray(t[c]) || (t[c] = [t[c]]), t[c].push(i)) : t[c] = i;
    }
  }), t;
}, H = (e, n = !0) => e ? Object.keys(e).map((t) => {
  const r = n ? encodeURIComponent(t) : t;
  if (Array.isArray(e[t]))
    return e[t].map((o) => {
      const s = n ? encodeURIComponent(o) : o;
      return `${r}=${s}`;
    }).join("&");
  {
    const o = n ? encodeURIComponent(e[t]) : e[t];
    return `${r}=${o}`;
  }
}).join("&") : "", P = (e, n, t = "asc") => e.sort((r, o) => {
  const s = n(r), c = n(o);
  return s === void 0 && c !== void 0 || s === null && c !== void 0 ? t === "asc" ? -1 : 1 : s == null && c === void 0 || c == null ? t === "asc" ? 1 : -1 : v(s) && v(c) ? t == "asc" ? s - c : c - s : t == "asc" ? String(s).localeCompare(String(c), void 0, {
    numeric: !0
  }) : String(c).localeCompare(String(s), void 0, {
    numeric: !0
  });
}), U = (e) => {
  const n = (t) => {
    const { scrollHeight: r, scrollWidth: o, offsetHeight: s, offsetWidth: c, scrollLeft: i, scrollTop: l } = e, a = t.x, S = t.y, A = (g) => {
      const d = l + S - g.y, m = i + a - g.x, T = r - s, E = o - c, p = {};
      m >= 0 && m <= E && (p.left = m), d >= 0 && d <= T && (p.top = d), e.scroll(p);
    };
    document.addEventListener("mousemove", A);
    const w = () => {
      document.removeEventListener("mousemove", A), document.removeEventListener("mouseup", w);
    };
    document.addEventListener("mouseup", w);
  };
  return e.addEventListener("mousedown", n), () => {
    e.removeEventListener("mousedown", n);
  };
}, W = () => `${Date.now().toString(36)}${Math.random().toString(36).substring(2)}`, v = (e) => {
  if (e == null || ["boolean", "symbol"].includes(typeof e) || /^\s*$/.test(e.toString()))
    return !1;
  const n = Number(e);
  return !isNaN(n) && isFinite(n);
}, f = (e) => e !== null && typeof e == "object" && !Array.isArray(e), h = (e, n, t = {}) => {
  if (Array.isArray(e) && Array.isArray(n))
    return y(e, n, t);
  const { excludeKeys: r } = t;
  for (const o in n) {
    const s = n[o], c = e[o];
    r && r(o) ? e[o] = s : Array.isArray(s) && Array.isArray(c) ? e[o] = y(c, s, t) : f(s) && f(c) ? e[o] = h(
      c,
      s,
      t
    ) : e[o] = s;
  }
  return e;
}, y = (e, n, t) => {
  const r = e.slice();
  return n.forEach((o, s) => {
    const c = r[s];
    Array.isArray(o) && Array.isArray(c) ? r[s] = y(c, o, t) : f(o) && f(c) ? r[s] = h(
      c,
      o,
      t
    ) : r[s] = o;
  }), r;
}, _ = (e, n = document.body) => {
  let t = e.offsetParent, r = e.offsetTop, o = e.offsetLeft;
  for (; t !== null && t !== n; )
    r += t.offsetTop, o += t.offsetLeft, t = t.offsetParent;
  return { offsetTop: r, offsetLeft: o };
}, N = (e = {}) => {
  const { forceScreenWidth: n = !1, threshold: t = 768, fallbackToScreenWidth: r = !0 } = e, o = navigator.userAgent, s = /android|ipad|iphone|ipod|windows phone|blackberry/i.test(o), c = "ontouchstart" in window || navigator.maxTouchPoints > 0, i = window.matchMedia(`(max-width: ${t}px)`).matches;
  return n ? i : s && c ? !0 : r ? i : !1;
};
export {
  C as createSmoothScroll,
  x as debounce,
  u as deepClone,
  h as deepMerge,
  R as download,
  U as draggingScroll,
  _ as getRootOffset,
  N as isMobile,
  v as isNumber,
  M as isPrimitive,
  L as linkTo,
  H as objToSearch,
  P as objectSort,
  W as randomId,
  O as searchToObj,
  $ as smoothScroll,
  F as throttle
};
