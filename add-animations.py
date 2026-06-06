"""炫酷点击动画：每点一次出现一个元素"""
from pptx import Presentation
from lxml import etree
import os

P = "http://schemas.openxmlformats.org/presentationml/2006/main"
_id = [9900]
def nid(): _id[0]+=1; return str(_id[0])

def add_transition(slide, ttype, dir=None):
    trans = etree.Element(f"{{{P}}}transition", spd="med")
    if ttype=="fade": etree.SubElement(trans, f"{{{P}}}fade")
    elif ttype=="push": e=etree.SubElement(trans, f"{{{P}}}push"); e.set("dir",dir) if dir else None
    elif ttype=="cover": e=etree.SubElement(trans, f"{{{P}}}cover"); e.set("dir",dir) if dir else None
    elif ttype=="wipe": e=etree.SubElement(trans, f"{{{P}}}wipe"); e.set("dir",dir) if dir else None
    elif ttype=="uncover": e=etree.SubElement(trans, f"{{{P}}}uncover"); e.set("dir",dir) if dir else None
    elif ttype=="dissolve": etree.SubElement(trans, f"{{{P}}}dissolve")
    c=slide.element.find(f"{{{P}}}cSld"); ex=slide.element.find(f"{{{P}}}transition")
    if ex is not None: slide.element.remove(ex)
    if c is not None: c.addnext(trans)
    else: slide.element.insert(0,trans)

def get_spid(shape):
    for nv in shape._element.iter(f"{{{P}}}nvSpPr"):
        for cn in nv.iter(f"{{{P}}}cNvPr"):
            sid = cn.get("id")
            if sid: return sid
    return None

# ===== 炫酷点击动画：每个元素单独点击出现 =====
COOL_EFFECTS = [
    "zoom",    # 缩放
    "fade",    # 淡入
    "fly",     # 飞入
    "wipe",    # 擦除
    "float",   # 浮入
]

def add_click_entrance(slide, spid, effect="fade", dur=500, direction=None):
    """添加点击触发的进入动画"""
    se = slide.element
    timing = se.find(f"{{{P}}}timing")
    if timing is None:
        timing = etree.Element(f"{{{P}}}timing")
        tnLst = etree.SubElement(timing, f"{{{P}}}tnLst")
        rp = etree.SubElement(tnLst, f"{{{P}}}par")
        rc = etree.SubElement(rp, f"{{{P}}}cTn", id=nid(), dur="indefinite", restart="never", nodeType="tmRoot")
        rcl = etree.SubElement(rc, f"{{{P}}}childTnLst")
        sq = etree.SubElement(rcl, f"{{{P}}}seq", concurrent="1", nextAc="seek")
        mc = etree.SubElement(sq, f"{{{P}}}cTn", id=nid(), dur="indefinite", nodeType="mainSeq")
        mcl = etree.SubElement(mc, f"{{{P}}}childTnLst")
        cSld = se.find(f"{{{P}}}cSld")
        trans = se.find(f"{{{P}}}transition")
        target = trans if trans is not None else cSld
        if target is not None: target.addnext(timing)
        else: se.append(timing)
    else:
        mcl = timing.find(f"{{{P}}}tnLst/{{{P}}}par/{{{P}}}cTn/{{{P}}}childTnLst/{{{P}}}seq/{{{P}}}cTn/{{{P}}}childTnLst")

    # 构建动画 par（每个 par = 一次点击）
    ap = etree.SubElement(mcl, f"{{{P}}}par")
    ac = etree.SubElement(ap, f"{{{P}}}cTn", id=nid(), dur=str(dur), fill="hold")

    # 条件：点击触发 (不设delay即onclick)
    stl = etree.SubElement(ac, f"{{{P}}}stCondLst")
    cond = etree.SubElement(stl, f"{{{P}}}cond")
    cond.set("delay", "0")  # 点击后立即播放
    # 不设 evt 属性 = 默认 onclick

    acl = etree.SubElement(ac, f"{{{P}}}childTnLst")
    ae = etree.SubElement(acl, f"{{{P}}}animEffect", transition="in", filterType=effect)

    if effect == "fly" and direction:
        dmap = {"b":"b","bottom":"b","l":"l","left":"l","r":"r","right":"r","t":"t","top":"t"}
        ae.set("from", dmap.get(direction, "b"))
    elif effect == "wipe" and direction:
        dmap = {"l":"l","left":"l","r":"r","right":"r","t":"t","top":"t","b":"b","bottom":"b"}
        ae.set("from", dmap.get(direction, "l"))
    elif effect == "zoom":
        ae.set("zoomTransition", "in")
    elif effect == "float":
        ae.set("filterType", "float")
        if direction:
            dmap = {"b":"b","l":"l","r":"r","t":"t"}
            ae.set("from", dmap.get(direction, "t"))

    cb = etree.SubElement(ae, f"{{{P}}}cBhvr")
    etree.SubElement(cb, f"{{{P}}}cTn", id=nid(), dur=str(dur))
    te = etree.SubElement(cb, f"{{{P}}}tgtEl")
    etree.SubElement(te, f"{{{P}}}spTgt", spid=str(spid))


def run(inp, out):
    prs = Presentation(inp)

    # 9种翻页动画
    transitions = [
        ("fade", None), ("push", "l"), ("cover", "r"), ("wipe", "l"),
        ("uncover", "d"), ("fade", None), ("push", "r"), ("cover", "l"), ("dissolve", None),
    ]

    for idx, slide in enumerate(prs.slides):
        add_transition(slide, transitions[idx][0], transitions[idx][1])

        # 收集所有有文字的形状
        shapes = []
        for s in slide.shapes:
            if s.has_text_frame and s.text_frame.text.strip():
                sid = get_spid(s)
                if sid: shapes.append((s, sid))

        # 炫酷特效分配：每个元素点击出现
        count = 0

        for si, (shape, spid) in enumerate(shapes):
            t = shape.text_frame.text.strip()
            # 跳过页脚
            if "EasyApplyResume |" in t: continue

            # 炫酷效果轮换
            effect_idx = si % len(COOL_EFFECTS)
            effect = COOL_EFFECTS[effect_idx]

            if idx == 0:  # 封面
                if "EasyApplyResume" in t:
                    add_click_entrance(slide, spid, "zoom", 800)
                elif "易投简历" in t:
                    add_click_entrance(slide, spid, "fade", 600)
                elif "Spring AI" in t:
                    add_click_entrance(slide, spid, "float", 500, "t")
                elif "技术架构" in t:
                    add_click_entrance(slide, spid, "wipe", 500, "l")
                elif "shiningCloud" in t:
                    add_click_entrance(slide, spid, "fade", 400)
                count += 1

            elif idx == 1:  # 问题背景
                if "问题背景" in t:
                    add_click_entrance(slide, spid, "fade", 500)
                elif "当前求职市场" in t:
                    add_click_entrance(slide, spid, "float", 400, "t")
                elif "简历制作低效" in t:
                    add_click_entrance(slide, spid, "fly", 500, "l")
                elif "求职信息不对称" in t:
                    add_click_entrance(slide, spid, "fly", 500, "r")
                elif "管理者运营负担" in t:
                    add_click_entrance(slide, spid, "fly", 500, "l")
                elif "缺乏个性化" in t:
                    add_click_entrance(slide, spid, "fly", 500, "r")
                elif "核心思路" in t:
                    add_click_entrance(slide, spid, "wipe", 400, "l")
                elif any(k in t for k in ["缺乏专业指导","难以获取","HR和管理员","传统平台"]):
                    add_click_entrance(slide, spid, "fade", 350)
                count += 1

            elif idx == 2:  # 技术选型
                if "技术选型" in t:
                    add_click_entrance(slide, spid, "fade", 500)
                elif "Spring Boot" in t:
                    add_click_entrance(slide, spid, "float", 400, "t")
                elif "基础框架" in t:
                    add_click_entrance(slide, spid, "wipe", 500, "t")
                elif "AI & LLM" in t:
                    add_click_entrance(slide, spid, "wipe", 500, "t")
                elif "数据与存储" in t:
                    add_click_entrance(slide, spid, "wipe", 500, "t")
                elif "AI 能力矩阵" in t:
                    add_click_entrance(slide, spid, "fade", 500)
                elif any(k in t for k in ["Spring Boot 3.3.5","Spring AI 1.0","MySQL 8.0"]):
                    add_click_entrance(slide, spid, "zoom", 400)
                count += 1

            elif idx == 3:  # AI架构
                if "AI 智能体架构" in t:
                    add_click_entrance(slide, spid, "fade", 500)
                elif "Agent 继承体系" in t:
                    add_click_entrance(slide, spid, "fade", 400)
                elif "BaseAgent" in t:
                    add_click_entrance(slide, spid, "fly", 500, "b")
                elif "ReActAgent" in t:
                    add_click_entrance(slide, spid, "fly", 500, "b")
                elif "ToolCallAgent" in t:
                    add_click_entrance(slide, spid, "fly", 500, "b")
                elif "核心组件" in t:
                    add_click_entrance(slide, spid, "fade", 400)
                elif any(x in t for x in ["ChatMemory","RAG Advisor","Tools","敏感词","MCP Client","LLM Router"]):
                    add_click_entrance(slide, spid, "wipe", 400, "l")
                elif "ResumeAssistantAgent" in t or "SystemAssistantAgent" in t:
                    add_click_entrance(slide, spid, "fade", 400)
                count += 1

            elif idx == 4:  # C端
                if "核心功能" in t and "C端" in t:
                    add_click_entrance(slide, spid, "fade", 500)
                elif "面向求职者" in t:
                    add_click_entrance(slide, spid, "float", 400, "t")
                elif any(x in t for x in ["智能简历","定制化","RAG 知识","工具调用","流式对话","求职生态"]):
                    add_click_entrance(slide, spid, "zoom", 450)
                elif any(k in t for k in ["语法优化","按行业","PgVector","联网搜索","SSE实时","职位浏览"]):
                    add_click_entrance(slide, spid, "fade", 350)
                count += 1

            elif idx == 5:  # B端
                if "核心功能" in t and "B端" in t:
                    add_click_entrance(slide, spid, "fade", 500)
                elif "面向管理员" in t:
                    add_click_entrance(slide, spid, "float", 400, "t")
                elif any(x in t for x in ["管理员管理","内容运营","数据管理","AI 运营","监控统计"]):
                    add_click_entrance(slide, spid, "wipe", 450, "l")
                elif "AI 管理助手" in t:
                    add_click_entrance(slide, spid, "fade", 400)
                elif any(x in t for x in ["引导式","系统配置","数据报表","用户/简历","MCP + RAG","独立对话","管理端专属","Re2"]):
                    add_click_entrance(slide, spid, "fly", 350, "r")
                count += 1

            elif idx == 6:  # D端 (新版)
                if "核心功能" in t and "D端" in t:
                    add_click_entrance(slide, spid, "fade", 500)
                elif "面向数据" in t:
                    add_click_entrance(slide, spid, "float", 400, "t")
                elif any(x in t for x in ["实时访问监控","广告投放管理","服务机器监控","智能数据报表"]):
                    add_click_entrance(slide, spid, "zoom", 500)
                elif any(k in t for k in ["日活","广告上下架","SSH远程","Prometheus"]):
                    add_click_entrance(slide, spid, "fly", 350, "l")
                count += 1

            elif idx == 7:  # 未来
                if "未来前景" in t:
                    add_click_entrance(slide, spid, "fade", 500)
                elif "持续迭代" in t:
                    add_click_entrance(slide, spid, "float", 400, "t")
                elif "近期" in t:
                    add_click_entrance(slide, spid, "zoom", 500)
                elif "中期" in t:
                    add_click_entrance(slide, spid, "zoom", 500)
                elif "远期" in t:
                    add_click_entrance(slide, spid, "zoom", 500)
                elif "愿景" in t:
                    add_click_entrance(slide, spid, "wipe", 600, "l")
                count += 1

            elif idx == 8:  # 致谢
                if "致  谢" in t:
                    add_click_entrance(slide, spid, "zoom", 800)
                elif "感谢各位" in t:
                    add_click_entrance(slide, spid, "fade", 600)
                elif "shining_cloud" in t:
                    add_click_entrance(slide, spid, "float", 500, "b")
                count += 1

        print(f"Slide {idx+1}: {count} click animations")

    prs.save(out)
    print(f"\n[DONE] {out}")

    # verify
    prs2 = Presentation(out)
    for i, s in enumerate(prs2.slides):
        tr = s.element.find(f"{{{P}}}transition")
        tm = s.element.find(f"{{{P}}}timing")
        print(f"  Slide {i+1}: trans={'YES' if tr is not None else 'NO'}, anim={'YES' if tm is not None else 'NO'}")


run("out/EasyApplyResume-项目介绍-v4.pptx", "out/EasyApplyResume-项目介绍-v4-animated.pptx")
