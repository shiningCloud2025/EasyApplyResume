"""
最终版 v13：
- 中文内容
- 文字放到卡片形状里（一块出）
- 翻页 fade 特效（Office 2007 兼容）
"""
from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.dml.color import RGBColor
from pptx.enum.text import PP_ALIGN, MSO_ANCHOR
from pptx.enum.shapes import MSO_SHAPE
from lxml import etree
import zipfile, copy

P = 'http://schemas.openxmlformats.org/presentationml/2006/main'

def extract_timing():
    with zipfile.ZipFile('out/native-anim.pptx', 'r') as z:
        with z.open('ppt/slides/slide1.xml') as f:
            return etree.fromstring(f.read()).find(f'{{{P}}}timing')

TIMING_TEMPLATE = extract_timing()

def clone_timing(card_ids):
    """复制模板 timing，第一个 clickEffect 保留，其余改为 auto-after"""
    timing = copy.deepcopy(TIMING_TEMPLATE)
    all_ces = timing.findall(f'.//{{{P}}}cTn[@nodeType="clickEffect"]')
    click_pars = []
    for ce in all_ces:
        p = ce.getparent()
        while p is not None and p.tag != f'{{{P}}}par': p = p.getparent()
        if p is not None and p not in click_pars: click_pars.append(p)
    
    need = len(card_ids)
    if need == 0: return timing
    parent = click_pars[0].getparent() if click_pars else None
    
    if parent is not None:
        for p in click_pars[need:]:
            try:
                real_parent = p.getparent()
                if real_parent is not None:
                    real_parent.remove(p)
            except ValueError:
                pass
        if need > len(click_pars):
            for i in range(len(click_pars), need):
                parent.append(copy.deepcopy(click_pars[-1]))
    
    # 重新收集：现在数量和 card_ids 一致
    all_ces = timing.findall(f'.//{{{P}}}cTn[@nodeType="clickEffect"]')
    for i, ce in enumerate(all_ces):
        if i == 0:
            # 第一个保留 clickEffect（点击触发）
            pass
        else:
            # 后续改为 afterEffect（自动播放），保留 presetClass 确保初始隐藏
            ce.set('nodeType', 'afterEffect')
            sc = ce.find(f'{{{P}}}stCondLst')
            if sc is not None:
                for cond in sc.findall(f'{{{P}}}cond'):
                    cond.set('delay', str(300 * i))
    
    for i, tgt in enumerate(timing.findall(f'.//{{{P}}}spTgt')):
        tgt.set('spid', str(card_ids[i % need]))
    return timing

def make_transition(trans_type='fade'):
    trans = etree.Element(f'{{{P}}}transition', spd='slow')
    if trans_type in ('push','cover','wipe','uncover'):
        tmap = {'push':'l','cover':'r','wipe':'l','uncover':'d'}
        t = etree.SubElement(trans, f'{{{P}}}{trans_type}')
        t.set('dir', tmap[trans_type])
    elif trans_type == 'dissolve':
        etree.SubElement(trans, f'{{{P}}}dissolve')
    else:
        etree.SubElement(trans, f'{{{P}}}fade')
    return trans

# ============ 配色 / 工具函数 ============
prs=Presentation();prs.slide_width=Inches(13.333);prs.slide_height=Inches(7.5)
SW=prs.slide_width;SH=prs.slide_height

BG=CARD=RGBColor(0xFF,0xFF,0xFF);BLUE=RGBColor(0x02,0x84,0xC7)
TEAL=RGBColor(0x0D,0x94,0x8F);ORANGE=RGBColor(0xEA,0x58,0x0C)
PURPLE=RGBColor(0x7C,0x3A,0xED);DARK=RGBColor(0x0F,0x17,0x2A)
TEXT=RGBColor(0x1E,0x29,0x3B);GRAY=RGBColor(0x64,0x74,0x8B)
WHITE=RGBColor(0xFF,0xFF,0xFF);LIGHT=RGBColor(0xF1,0xF5,0xF9)

def bg(s,c):s.background.fill.solid();s.background.fill.fore_color.rgb=c
def rct(s,l,t,w,h,fl,li=None):
    sh=s.shapes.add_shape(MSO_SHAPE.RECTANGLE,l,t,w,h)
    sh.fill.solid();sh.fill.fore_color.rgb=fl
    if li:sh.line.fill.solid();sh.line.fill.fore_color.rgb=li
    else:sh.line.fill.background()
    return sh
def tx(s,l,t,w,h,text,sz=12,color=TEXT,b=False,font='Calibri',al=PP_ALIGN.LEFT,va=MSO_ANCHOR.TOP):
    tb=s.shapes.add_textbox(l,t,w,h);tf=tb.text_frame;tf.word_wrap=True
    p=tf.paragraphs[0];p.text=text;p.font.size=Pt(sz)
    p.font.color.rgb=color;p.font.bold=b;p.font.name=font;p.alignment=al
    return tb
def tbar(s,tt):
    rct(s,Inches(0),Inches(0),SW,Inches(0.8),DARK)
    tx(s,Inches(0.6),Inches(0.15),Inches(12),Inches(0.5),tt,sz=22,color=WHITE,b=True,font='Trebuchet MS',va=MSO_ANCHOR.MIDDLE)
    rct(s,Inches(0),Inches(0.8),SW,Inches(0.03),BLUE)
def pn(s,n):tx(s,Inches(11.5),Inches(7.1),Inches(1.5),Inches(0.3),f"{n}/9",sz=9,color=GRAY,al=PP_ALIGN.RIGHT)

def card_with_text(s, x, y, w, h, title, desc, accent_color):
    """创建一张卡片（形状内含标题+描述），返回 shape_id"""
    sh = s.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(x), Inches(y), Inches(w), Inches(h))
    sh.fill.solid()
    sh.fill.fore_color.rgb = CARD
    try: sh.line.fill.background()
    except: pass
    
    tf = sh.text_frame
    tf.word_wrap = True
    tf.margin_left = Inches(0.3)
    tf.margin_right = Inches(0.15)
    tf.margin_top = Inches(0.25)
    tf.margin_bottom = Inches(0.1)
    
    # 标题
    p1 = tf.paragraphs[0]
    p1.text = title
    p1.font.size = Pt(18)
    p1.font.color.rgb = accent_color
    p1.font.bold = True
    p1.font.name = 'Trebuchet MS'
    p1.space_after = Pt(8)
    
    # 描述
    p2 = tf.add_paragraph()
    p2.text = desc
    p2.font.size = Pt(14)
    p2.font.color.rgb = TEXT
    p2.font.name = 'Calibri'
    p2.line_spacing = Pt(22)
    
    return sh.shape_id

def card_small(s, x, y, w, h, title, desc, accent_color):
    """小卡片（形状内含标题+描述）"""
    sh = s.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(x), Inches(y), Inches(w), Inches(h))
    sh.fill.solid()
    sh.fill.fore_color.rgb = CARD
    try: sh.line.fill.background()
    except: pass
    
    tf = sh.text_frame
    tf.word_wrap = True
    tf.margin_left = Inches(0.15)
    tf.margin_right = Inches(0.1)
    tf.margin_top = Inches(0.08)
    
    p1 = tf.paragraphs[0]
    p1.text = title
    p1.font.size = Pt(14)
    p1.font.color.rgb = accent_color
    p1.font.bold = True
    p1.font.name = 'Trebuchet MS'
    p1.space_after = Pt(4)
    
    p2 = tf.add_paragraph()
    p2.text = desc
    p2.font.size = Pt(11)
    p2.font.color.rgb = GRAY
    p2.font.name = 'Calibri'
    
    return sh.shape_id

def phase_card(s, x, y, w, h, title, desc, accent_color):
    """阶段卡片（大卡片，居中文案）"""
    sh = s.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(x), Inches(y), Inches(w), Inches(h))
    sh.fill.solid()
    sh.fill.fore_color.rgb = CARD
    try: sh.line.fill.background()
    except: pass
    
    tf = sh.text_frame
    tf.word_wrap = True
    tf.margin_left = Inches(0.2)
    tf.margin_right = Inches(0.2)
    tf.margin_top = Inches(0.3)
    
    p1 = tf.paragraphs[0]
    p1.text = title
    p1.font.size = Pt(22)
    p1.font.color.rgb = accent_color
    p1.font.bold = True
    p1.font.name = 'Trebuchet MS'
    p1.alignment = PP_ALIGN.CENTER
    p1.space_after = Pt(20)
    
    for line in desc.split('\n'):
        p = tf.add_paragraph()
        p.text = line
        p.font.size = Pt(16)
        p.font.color.rgb = TEXT
        p.font.name = 'Calibri'
        p.alignment = PP_ALIGN.CENTER
        p.line_spacing = Pt(28)
    
    return sh.shape_id

# ======================================================================
# 幻灯片 1: 封面
# ======================================================================
s1=prs.slides.add_slide(prs.slide_layouts[6]);bg(s1,LIGHT)
# 中文标题（上）
tx(s1,Inches(1),Inches(1.5),Inches(11.333),Inches(0.8),"基于SpringAI和机器学习的智能简历平台",sz=26,color=BLUE,font='Trebuchet MS',al=PP_ALIGN.CENTER)
# 蓝色装饰线
rct(s1,Inches(4.5),Inches(2.4),Inches(4.333),Inches(0.05),BLUE)
# 英文标题（下）
tx(s1,Inches(1),Inches(2.6),Inches(11.333),Inches(1.0),"EasyApplyResume",sz=52,color=DARK,b=True,font='Trebuchet MS',al=PP_ALIGN.CENTER)
# 信息区
info_lines=["学号：2206010629","班级：计算22-6","姓名：赵云翰","指导老师：高一萌老师","开源项目地址：github.com/shiningCloud2025/EasyApplyResume（赵云翰本人开源，非他人开源）"]
for i,line in enumerate(info_lines):
    tx(s1,Inches(1),Inches(4.0+i*0.42),Inches(11.333),Inches(0.38),line,sz=13,color=DARK,font='Trebuchet MS',al=PP_ALIGN.CENTER)

# ======================================================================
# 幻灯片 2: 目录
# ======================================================================
s2=prs.slides.add_slide(prs.slide_layouts[6]);bg(s2,LIGHT)
tbar_bg2=rct(s2,Inches(0),Inches(0),SW,Inches(0.8),DARK)
tbar_tx2=tx(s2,Inches(0.6),Inches(0.15),Inches(12),Inches(0.5),"目录",sz=22,color=WHITE,b=True,font='Trebuchet MS',va=MSO_ANCHOR.MIDDLE)
tbar_ln2=rct(s2,Inches(0),Inches(0.8),SW,Inches(0.03),BLUE)
pg2=tx(s2,Inches(11.5),Inches(7.1),Inches(1.5),Inches(0.3),"2/10",sz=9,color=GRAY,al=PP_ALIGN.RIGHT)
cards2=[tbar_bg2.shape_id,tbar_tx2.shape_id,tbar_ln2.shape_id,pg2.shape_id]

# 卡片
big=s2.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE,Inches(0.6),Inches(1.1),Inches(12.1),Inches(5.8))
big.fill.solid();big.fill.fore_color.rgb=CARD;big.line.fill.background()
cards2.append(big.shape_id)

toc_items=["项目背景与痛点深度分析","技术选型","系统总体架构图","C端 — 易投简历用户端","B端 — 易投简历管理端","D端 — 广告与监控端","总结与展望"]
ACCENT=BLUE

# 左列 1-3 贴左
for i in range(3):
    y=Inches(1.4+i*1.25)
    circle=s2.shapes.add_shape(MSO_SHAPE.OVAL,Inches(1.0),y,Inches(0.45),Inches(0.45))
    circle.fill.solid();circle.fill.fore_color.rgb=ACCENT;circle.line.fill.background()
    cards2.append(circle.shape_id)
    tf=circle.text_frame;tf.word_wrap=False
    p=tf.paragraphs[0];p.text=str(i+1);p.font.size=Pt(14);p.font.color.rgb=WHITE
    p.font.bold=True;p.font.name='Trebuchet MS';p.alignment=PP_ALIGN.CENTER
    tb=s2.shapes.add_textbox(Inches(1.7),y,Inches(4.5),Inches(0.45))
    cards2.append(tb.shape_id)
    tf2=tb.text_frame;tf2.word_wrap=True
    p2=tf2.paragraphs[0];p2.text=toc_items[i];p2.font.size=Pt(20)
    p2.font.color.rgb=ACCENT;p2.font.bold=True;p2.font.name='Trebuchet MS'

# 右列 4-6 贴右
for i in range(3,6):
    y=Inches(1.4+(i-3)*1.25)
    circle=s2.shapes.add_shape(MSO_SHAPE.OVAL,Inches(8.2),y,Inches(0.45),Inches(0.45))
    circle.fill.solid();circle.fill.fore_color.rgb=ACCENT;circle.line.fill.background()
    cards2.append(circle.shape_id)
    tf=circle.text_frame;tf.word_wrap=False
    p=tf.paragraphs[0];p.text=str(i+1);p.font.size=Pt(14);p.font.color.rgb=WHITE
    p.font.bold=True;p.font.name='Trebuchet MS';p.alignment=PP_ALIGN.CENTER
    tb=s2.shapes.add_textbox(Inches(8.9),y,Inches(3.5),Inches(0.45))
    cards2.append(tb.shape_id)
    tf2=tb.text_frame;tf2.word_wrap=True
    p2=tf2.paragraphs[0];p2.text=toc_items[i];p2.font.size=Pt(20)
    p2.font.color.rgb=ACCENT;p2.font.bold=True;p2.font.name='Trebuchet MS'

# 底部居中 7
y_bottom=Inches(5.3)
circle7=s2.shapes.add_shape(MSO_SHAPE.OVAL,Inches(5.0),y_bottom,Inches(0.45),Inches(0.45))
circle7.fill.solid();circle7.fill.fore_color.rgb=ACCENT;circle7.line.fill.background()
cards2.append(circle7.shape_id)
tf7=circle7.text_frame;tf7.word_wrap=False
p7=tf7.paragraphs[0];p7.text="7";p7.font.size=Pt(14);p7.font.color.rgb=WHITE
p7.font.bold=True;p7.font.name='Trebuchet MS';p7.alignment=PP_ALIGN.CENTER
tb7=s2.shapes.add_textbox(Inches(5.8),y_bottom,Inches(4.0),Inches(0.45))
cards2.append(tb7.shape_id)
tf7b=tb7.text_frame;tf7b.word_wrap=True
p7b=tf7b.paragraphs[0];p7b.text=toc_items[6];p7b.font.size=Pt(20)
p7b.font.color.rgb=ACCENT;p7b.font.bold=True;p7b.font.name='Trebuchet MS'

# ======================================================================
# 幻灯片 3: 项目背景与痛点深度分析
# ======================================================================
s3=prs.slides.add_slide(prs.slide_layouts[6]);bg(s3,LIGHT)
tbar_bg3=rct(s3,Inches(0),Inches(0),SW,Inches(0.8),DARK)
tbar_tx3=tx(s3,Inches(0.6),Inches(0.15),Inches(12),Inches(0.5),"项目背景与痛点深度分析",sz=22,color=WHITE,b=True,font='Trebuchet MS',va=MSO_ANCHOR.MIDDLE)
tbar_ln3=rct(s3,Inches(0),Inches(0.8),SW,Inches(0.03),BLUE)
pg3=tx(s3,Inches(11.5),Inches(7.1),Inches(1.5),Inches(0.3),"3/10",sz=9,color=GRAY,al=PP_ALIGN.RIGHT)
cards3=[tbar_bg3.shape_id,tbar_tx3.shape_id,tbar_ln3.shape_id,pg3.shape_id]

left_card3=s3.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE,Inches(0.6),Inches(1.15),Inches(6.0),Inches(3.85))
left_card3.fill.solid();left_card3.fill.fore_color.rgb=CARD;left_card3.line.fill.background()
cards3.append(left_card3.shape_id)
ltf3=left_card3.text_frame;ltf3.word_wrap=True
ltf3.margin_left=Inches(0.35);ltf3.margin_right=Inches(0.3);ltf3.margin_top=Inches(0.25)

p3a=ltf3.paragraphs[0];p3a.text="01 求职市场的严峻现状"
p3a.font.size=Pt(22);p3a.font.color.rgb=BLUE;p3a.font.bold=True;p3a.font.name='Trebuchet MS'
p3a.space_after=Pt(12)

p3b=ltf3.add_paragraph()
r1=p3b.add_run();r1.text="2025届全国高校毕业生规模预测达 ";r1.font.size=Pt(13);r1.font.color.rgb=GRAY;r1.font.name='Calibri'
r2=p3b.add_run();r2.text="1222 万";r2.font.size=Pt(30);r2.font.color.rgb=BLUE;r2.font.bold=True;r2.font.name='Trebuchet MS'
r3=p3b.add_run();r3.text=" 人，竞争空前激烈。";r3.font.size=Pt(13);r3.font.color.rgb=GRAY;r3.font.name='Calibri'

for title,desc in [("\u25b8 简历竞争力不透明：","缺乏专业的量化分析与针对性的优化建议，难以突出个人优势。"),("\u25b8 咨询服务响应滞后：","专业顾问服务供需严重失衡，信息传递存在壁垒，反馈不及时。"),("\u25b8 笔试准备缺乏资源：","缺乏行业真实题库与场景化模拟，导致备考效率低、实战能力弱。")]:
    p=ltf3.add_paragraph();p.space_before=Pt(16)
    r_title=p.add_run();r_title.text=title;r_title.font.size=Pt(12);r_title.font.color.rgb=BLUE;r_title.font.bold=True;r_title.font.name='Trebuchet MS'
    r_desc=p.add_run();r_desc.text=desc;r_desc.font.size=Pt(11);r_desc.font.color.rgb=GRAY;r_desc.font.name='Calibri'

right_card3=s3.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE,Inches(6.9),Inches(1.15),Inches(5.8),Inches(3.85))
right_card3.fill.solid();right_card3.fill.fore_color.rgb=RGBColor(0xF0,0xF7,0xFF);right_card3.line.fill.background()
cards3.append(right_card3.shape_id)
rtf3=right_card3.text_frame;rtf3.word_wrap=True
rtf3.margin_left=Inches(0.35);rtf3.margin_right=Inches(0.3);rtf3.margin_top=Inches(0.25)

p3c=rtf3.paragraphs[0];p3c.text="02 核心使命"
p3c.font.size=Pt(22);p3c.font.color.rgb=BLUE;p3c.font.bold=True;p3c.font.name='Trebuchet MS'
p3c.space_after=Pt(16)

p3d=rtf3.add_paragraph()
p3d.text="利用人工智能技术结合海量人工收集的行业数据，彻底打破求职过程中的信息差，打造高效、透明、专业的一站式解决方案。"
p3d.font.size=Pt(13);p3d.font.color.rgb=DARK;p3d.font.name='Calibri';p3d.line_spacing=Pt(24)
p3d.space_after=Pt(18)

for title,desc in [("\u25b8 简历量化评估：","基于机器学习模型对简历内容进行多维度量化评分，精准匹配岗位需求，大幅提升简历投递的针对性与通过率。"),("\u25b8 智能交互咨询：","构建具备深度行业语义理解能力的 AI 问答系统，提供 24/7 实时咨询服务，打破传统顾问服务的时间与成本壁垒。"),("\u25b8 全流程实战演练：","打造高度仿真的笔试模拟与岗位实训环境，通过实战反馈辅助职业决策，补齐求职准备的短板。")]:
    p=rtf3.add_paragraph();p.space_before=Pt(13)
    r_title=p.add_run();r_title.text=title;r_title.font.size=Pt(12);r_title.font.color.rgb=BLUE;r_title.font.bold=True;r_title.font.name='Trebuchet MS'
    r_desc=p.add_run();r_desc.text=desc;r_desc.font.size=Pt(10);r_desc.font.color.rgb=GRAY;r_desc.font.name='Calibri'

bot_pain_cards=[
    ("\u2460 简历分析痛点","传统简历无法直观感知在算法系统中的真实得分，导致求职者在投递环节缺乏针对性，盲目投递且筛选通过率与岗位匹配转化率均处于低位。",BLUE),
    ("\u2461 交互咨询痛点","专业的人工职业指导存在成本高昂、响应滞后的局限。求职者急需一个具备行业深度语义理解能力、可提供24/7实时服务的智能咨询助手来解决个性化疑问。",TEAL),
    ("\u2462 实战演练痛点","不同企业的笔试题型差异巨大，且缺乏有效的反馈机制。学生需要一个高度仿真的专业模拟环境，帮助他们提前熟悉考核逻辑，通过实战反馈辅助职业决策。",ORANGE)
]
for i,(ti,de,ac) in enumerate(bot_pain_cards):
    x=0.6+i*4.15
    bot_card=s3.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE,Inches(x),Inches(5.2),Inches(3.8),Inches(1.95))
    bot_card.fill.solid();bot_card.fill.fore_color.rgb=CARD;bot_card.line.fill.background()
    cards3.append(bot_card.shape_id)
    btf=bot_card.text_frame;btf.word_wrap=True
    btf.margin_left=Inches(0.2);btf.margin_right=Inches(0.2);btf.margin_top=Inches(0.2)
    bp=btf.paragraphs[0];bp.text=ti;bp.font.size=Pt(14);bp.font.color.rgb=ac;bp.font.bold=True;bp.font.name='Trebuchet MS'
    bp.space_after=Pt(6)
    bp2=btf.add_paragraph();bp2.text=de;bp2.font.size=Pt(9);bp2.font.color.rgb=GRAY;bp2.font.name='Calibri';bp2.line_spacing=Pt(15)

# ======================================================================
# 幻灯片 4: 技术选型
# ======================================================================
s4=prs.slides.add_slide(prs.slide_layouts[6]);bg(s4,LIGHT)
tbar_bg4=rct(s4,Inches(0),Inches(0),SW,Inches(0.8),DARK)
tbar_tx4=tx(s4,Inches(0.6),Inches(0.15),Inches(12),Inches(0.5),"技术选型",sz=22,color=WHITE,b=True,font='Trebuchet MS',va=MSO_ANCHOR.MIDDLE)
tbar_ln4=rct(s4,Inches(0),Inches(0.8),SW,Inches(0.03),BLUE)
pg4=tx(s4,Inches(11.5),Inches(7.1),Inches(1.5),Inches(0.3),"4/10",sz=9,color=GRAY,al=PP_ALIGN.RIGHT)
cards4=[tbar_bg4.shape_id,tbar_tx4.shape_id,tbar_ln4.shape_id,pg4.shape_id]

# 6 个技术分类，上下各3个，全部同尺寸
tech_cats=[
    ("前端技术",["React","Vue","Ant Design","ElementUI","Vite"],BLUE),
    ("后端技术",["SpringBoot3","SpringMVC","MyBatisPlus","SpringSecurity","SpringTask","JWT","LogBack"],TEAL),
    ("存储技术",["MySQL","PostgreSQL / PgVector","Redis","MinIO","七牛云KODO","Nacos","Yapi"],ORANGE),
    ("AI 技术",["机器学习","Embedding"],PURPLE),
    ("LLM 技术",["SpringAI","SpringAIAlibaba","Prompt 工程","RAG 检索增强","Function Calling","ReAct Agent"],RGBColor(0xDC,0x26,0x26)),
    ("运维技术",["Docker","Docker Compose","SpringBootAdmin","Prometheus","Grafana"],RGBColor(0x65,0xA3,0x0D)),
]
for i,(cat,items,ac) in enumerate(tech_cats):
    x=Inches(0.7+(i%3)*4.1)
    y=Inches(1.15 if i<3 else 4.2)
    card=s4.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE,x,y,Inches(3.8),Inches(2.85))
    card.fill.solid();card.fill.fore_color.rgb=CARD;card.line.fill.background()
    cards4.append(card.shape_id)
    bar=rct(s4,x,y,Inches(3.8),Inches(0.06),ac)
    cards4.append(bar.shape_id)
    ctf=card.text_frame;ctf.word_wrap=True
    ctf.margin_left=Inches(0.2);ctf.margin_right=Inches(0.12);ctf.margin_top=Inches(0.22)
    cp=ctf.paragraphs[0];cp.text=cat;cp.font.size=Pt(15);cp.font.color.rgb=ac;cp.font.bold=True;cp.font.name='Trebuchet MS'
    cp.space_after=Pt(8)
    for item in items:
        ip=ctf.add_paragraph();ip.text=f"\u2022 {item}";ip.font.size=Pt(10);ip.font.color.rgb=GRAY;ip.font.name='Calibri'
        ip.line_spacing=Pt(16)

# ======================================================================
# 幻灯片 5: 系统总体架构图（图片）
# ======================================================================
s5=prs.slides.add_slide(prs.slide_layouts[6]);bg(s5,LIGHT)
# 标题栏
tbar_bg5=rct(s5,Inches(0),Inches(0),SW,Inches(0.8),DARK)
tbar_tx5=tx(s5,Inches(0.6),Inches(0.15),Inches(12),Inches(0.5),"易投简历系统总体架构图",sz=22,color=WHITE,b=True,font='Trebuchet MS',va=MSO_ANCHOR.MIDDLE)
tbar_ln5=rct(s5,Inches(0),Inches(0.8),SW,Inches(0.03),BLUE)
pg5=tx(s5,Inches(11.5),Inches(7.1),Inches(1.5),Inches(0.3),"5/10",sz=9,color=GRAY,al=PP_ALIGN.RIGHT)
cards5=[tbar_bg5.shape_id,tbar_tx5.shape_id,tbar_ln5.shape_id,pg5.shape_id]

# 插入架构图图片（请把图片保存为 out/architecture.png）
try:
    img=s5.shapes.add_picture('out/architecture.png',Inches(0.5),Inches(1.0),width=Inches(12.333))
    cards5.append(img.shape_id)
except FileNotFoundError:
    # 图片不存在时显示提示
    placeholder=s5.shapes.add_textbox(Inches(2),Inches(3),Inches(9),Inches(2))
    tf=placeholder.text_frame
    tf.paragraphs[0].text="[请将架构图保存为 out/architecture.png]"
    tf.paragraphs[0].font.size=Pt(20)
    tf.paragraphs[0].font.color.rgb=GRAY
    cards5.append(placeholder.shape_id)

# ======================================================================
# 幻灯片 6: C端 — 易投简历用户端
# ======================================================================
s6=prs.slides.add_slide(prs.slide_layouts[6]);bg(s6,LIGHT)
cards6=[]
tbar_bg6=rct(s6,Inches(0),Inches(0),SW,Inches(0.8),DARK)
tbar_tx6=tx(s6,Inches(0.6),Inches(0.15),Inches(12),Inches(0.5),"C端 — 易投简历用户端",sz=22,color=WHITE,b=True,font='Trebuchet MS',va=MSO_ANCHOR.MIDDLE)
tbar_ln6=rct(s6,Inches(0),Inches(0.8),SW,Inches(0.03),BLUE)
pg6=tx(s6,Inches(11.5),Inches(7.1),Inches(1.5),Inches(0.3),"6/10",sz=9,color=GRAY,al=PP_ALIGN.RIGHT)
cards6=[tbar_bg6.shape_id,tbar_tx6.shape_id,tbar_ln6.shape_id,pg6.shape_id]

user_mods=[("我的简历模块","导入简历 / 新建简历\n在线编辑 / 给HR发送简历"),("简历模版模块","简历模版库 / 创建新的简历"),("招聘信息模块","招聘公司浏览 / 招聘岗位浏览\n招聘地点浏览"),("求职攻略模块","行业指南 / 面试技巧 / 职场干货"),("AI简历助手","智能润色 / 流式对话\n记忆对话"),("笔试专项","题库分类 / 题目练习\n题目解析")]
for i,(ti,de) in enumerate(user_mods):
    x=Inches(0.7+(i%3)*4.1);y=Inches(1.15+(i//3)*2.8)
    sh=s6.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE,x,y,Inches(3.8),Inches(2.4))
    sh.fill.solid();sh.fill.fore_color.rgb=CARD;sh.line.fill.background()
    cards6.append(sh.shape_id)
    cards6.append(rct(s6,x,y,Inches(3.8),Inches(0.06),BLUE).shape_id)
    stf=sh.text_frame;stf.word_wrap=True
    stf.margin_left=Inches(0.25);stf.margin_right=Inches(0.2);stf.margin_top=Inches(0.25)
    sp=stf.paragraphs[0];sp.text=ti;sp.font.size=Pt(20);sp.font.color.rgb=BLUE;sp.font.bold=True;sp.font.name='Trebuchet MS'
    sp.space_after=Pt(12)
    for line in de.split('\n'):
        spn=stf.add_paragraph();spn.text=line;spn.font.size=Pt(14);spn.font.color.rgb=GRAY;spn.font.name='Calibri';spn.line_spacing=Pt(24)

# ======================================================================
# 幻灯片 7: B端 — 易投简历管理端
# ======================================================================
s7=prs.slides.add_slide(prs.slide_layouts[6]);bg(s7,LIGHT)
cards7=[]
tbar_bg7=rct(s7,Inches(0),Inches(0),SW,Inches(0.8),DARK)
tbar_tx7=tx(s7,Inches(0.6),Inches(0.15),Inches(12),Inches(0.5),"B端 — 易投简历管理端",sz=22,color=WHITE,b=True,font='Trebuchet MS',va=MSO_ANCHOR.MIDDLE)
tbar_ln7=rct(s7,Inches(0),Inches(0.8),SW,Inches(0.03),BLUE)
pg7=tx(s7,Inches(11.5),Inches(7.1),Inches(1.5),Inches(0.3),"7/10",sz=9,color=GRAY,al=PP_ALIGN.RIGHT)
cards7=[tbar_bg7.shape_id,tbar_tx7.shape_id,tbar_ln7.shape_id,pg7.shape_id]

admin_mods=[("网站管理模块","管理员管理 / 角色管理 / 权限管理"),("文章管理模块","求职攻略文章管理"),("招聘管理模块","招聘岗位管理 / 招聘信息管理"),("简历管理模块","简历模版管理 / 系统删除简历管理"),("Map管理模块","行业 / 大学 / 省份 / 城市 / 区县 / 街道Map管理"),("AI管理模块","AI智能问答助手 / AI智能体助手 / LLM调用日志管理"),("反馈管理模块","用户端反馈管理 / 管理端反馈管理 / 用户端反馈记录 / 管理端反馈记录"),("笔试专项管理模块","题库大类管理 / 题库小类管理 / 题库题目管理 / 用户答题管理"),("评分模型管理模块","训练数据管理 / 训练代码管理 / 模型版本管理"),("内外系统等模块","内部系统 / 外部系统 / API文档中心等模块")]
for i,(ti,de) in enumerate(admin_mods):
    x=Inches(0.55+(i%5)*2.45);y=Inches(1.15+(i//5)*3.05)
    sh=s7.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE,x,y,Inches(2.2),Inches(2.7))
    sh.fill.solid();sh.fill.fore_color.rgb=CARD;sh.line.fill.background()
    cards7.append(sh.shape_id)
    cards7.append(rct(s7,x,y,Inches(2.2),Inches(0.05),TEAL).shape_id)
    stf=sh.text_frame;stf.word_wrap=True
    stf.margin_left=Inches(0.12);stf.margin_right=Inches(0.08);stf.margin_top=Inches(0.18)
    sp=stf.paragraphs[0];sp.text=ti;sp.font.size=Pt(12);sp.font.color.rgb=TEAL;sp.font.bold=True;sp.font.name='Trebuchet MS'
    sp.space_after=Pt(6)
    sp2=stf.add_paragraph();sp2.text=de;sp2.font.size=Pt(9);sp2.font.color.rgb=GRAY;sp2.font.name='Calibri';sp2.line_spacing=Pt(14)

# ======================================================================
# 幻灯片 8: D端 — 易投简历广告与监控端
# ======================================================================
s8=prs.slides.add_slide(prs.slide_layouts[6]);bg(s8,LIGHT)
cards8=[]
tbar_bg8=rct(s8,Inches(0),Inches(0),SW,Inches(0.8),DARK)
tbar_tx8=tx(s8,Inches(0.6),Inches(0.15),Inches(12),Inches(0.5),"D端 — 易投简历广告与监控端",sz=22,color=WHITE,b=True,font='Trebuchet MS',va=MSO_ANCHOR.MIDDLE)
tbar_ln8=rct(s8,Inches(0),Inches(0.8),SW,Inches(0.03),BLUE)
pg8=tx(s8,Inches(11.5),Inches(7.1),Inches(1.5),Inches(0.3),"8/10",sz=9,color=GRAY,al=PP_ALIGN.RIGHT)
cards8=[tbar_bg8.shape_id,tbar_tx8.shape_id,tbar_ln8.shape_id,pg8.shape_id]

ad_mods=[("公告管理模块","管理端公告管理 / 用户端公告管理 / 监测端公告管理"),("广告管理模块","管理端广告管理 / 用户端广告管理 / 监测端广告管理"),("用户监测管理模块","网站管理"),("管理监测管理模块","网站管理"),("服务器管理","设备管理 / 设备监控"),("网站安全管理模块","SpringBootAdmin / Prometheus / Grafana")]
for i,(ti,de) in enumerate(ad_mods):
    x=Inches(0.6+(i%3)*4.15);y=Inches(1.15+(i//3)*2.8)
    sh=s8.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE,x,y,Inches(3.8),Inches(2.4))
    sh.fill.solid();sh.fill.fore_color.rgb=CARD;sh.line.fill.background()
    cards8.append(sh.shape_id)
    cards8.append(rct(s8,x,y,Inches(3.8),Inches(0.05),ORANGE).shape_id)
    stf=sh.text_frame;stf.word_wrap=True
    stf.margin_left=Inches(0.2);stf.margin_right=Inches(0.15);stf.margin_top=Inches(0.25)
    sp=stf.paragraphs[0];sp.text=ti;sp.font.size=Pt(17);sp.font.color.rgb=ORANGE;sp.font.bold=True;sp.font.name='Trebuchet MS'
    sp.space_after=Pt(10)
    sp2=stf.add_paragraph();sp2.text=de;sp2.font.size=Pt(12);sp2.font.color.rgb=GRAY;sp2.font.name='Calibri';sp2.line_spacing=Pt(20)

# ======================================================================
# 幻灯片 9: 总结与展望
# ======================================================================
s9=prs.slides.add_slide(prs.slide_layouts[6]);bg(s9,LIGHT)
cards9=[]
tbar_bg9=rct(s9,Inches(0),Inches(0),SW,Inches(0.8),DARK)
tbar_tx9=tx(s9,Inches(0.6),Inches(0.15),Inches(12),Inches(0.5),"总结与展望",sz=22,color=WHITE,b=True,font='Trebuchet MS',va=MSO_ANCHOR.MIDDLE)
tbar_ln9=rct(s9,Inches(0),Inches(0.8),SW,Inches(0.03),BLUE)
pg9=tx(s9,Inches(11.5),Inches(7.1),Inches(1.5),Inches(0.3),"9/10",sz=9,color=GRAY,al=PP_ALIGN.RIGHT)
cards9=[tbar_bg9.shape_id,tbar_tx9.shape_id,tbar_ln9.shape_id,pg9.shape_id]

for ti,de,ac,y in [("已解决的问题","OSS孤儿数据清理 / 逻辑删除updateById修复 / 细粒度权限控制优化 / 多数据源统一管理 / 流式响应性能优化",BLUE,1.1),("未解决的问题","大模型API并发能力受限 / 上下文记忆存储空间不足 / 自部署模型能力较弱 / 推理服务器成本较高",TEAL,3.1),("落地展望","高校市场(B端):赋能就业指导中心 / 个人用户(C端):AI简历优化+笔试模拟 / 招聘生态(D端):为HR提供评分等API接口,辅助筛选 / 智能简历初筛+人岗匹配",ORANGE,5.1)]:
    cards9.append(phase_card(s9,0.7,y,11.9,1.8,ti,de,ac))

# ======================================================================
# 幻灯片 10: 致谢
# ======================================================================
s10=prs.slides.add_slide(prs.slide_layouts[6]);bg(s10,LIGHT);pn(s10,10)
cards10=[rct(s10,Inches(4.5),Inches(2.4),Inches(4.333),Inches(0.05),BLUE).shape_id]
# 感谢聆听
tx(s10,Inches(1),Inches(1.5),Inches(11.333),Inches(0.8),"感谢聆听",sz=48,color=DARK,b=True,font='Trebuchet MS',al=PP_ALIGN.CENTER)
# 中文标题
tx(s10,Inches(1),Inches(2.6),Inches(11.333),Inches(0.6),"基于SpringAI和机器学习的智能简历平台",sz=22,color=BLUE,font='Trebuchet MS',al=PP_ALIGN.CENTER)
# 英文标题
tx(s10,Inches(1),Inches(3.2),Inches(11.333),Inches(0.7),"EasyApplyResume",sz=42,color=DARK,b=True,font='Trebuchet MS',al=PP_ALIGN.CENTER)

# 信息区
tx(s10,Inches(1),Inches(4.2),Inches(11.333),Inches(0.35),"学号：2206010629",sz=13,color=DARK,font='Trebuchet MS',al=PP_ALIGN.CENTER)
tx(s10,Inches(1),Inches(4.55),Inches(11.333),Inches(0.35),"班级：计算22-6",sz=13,color=DARK,font='Trebuchet MS',al=PP_ALIGN.CENTER)
tx(s10,Inches(1),Inches(4.9),Inches(11.333),Inches(0.35),"姓名：赵云翰",sz=13,color=DARK,font='Trebuchet MS',al=PP_ALIGN.CENTER)
tx(s10,Inches(1),Inches(5.25),Inches(11.333),Inches(0.35),"指导老师：高一萌老师",sz=13,color=DARK,font='Trebuchet MS',al=PP_ALIGN.CENTER)
# 底部开源项目
tx(s10,Inches(1),Inches(5.8),Inches(11.333),Inches(0.35),"开源项目地址：github.com/shiningCloud2025/EasyApplyResume（赵云翰本人开源，非他人开源）",sz=11,color=BLUE,font='Trebuchet MS',al=PP_ALIGN.CENTER)

# ============ 保存 & ZIP 注入 ============
out="out/temp-new.pptx"
prs.save(out)

transitions=['fade']*10  # 统一用 fade，Office 2007 兼容

with zipfile.ZipFile(out,'r') as zin:
    with zipfile.ZipFile(out+'.tmp','w',zipfile.ZIP_DEFLATED) as zout:
        slide_files=[]
        for f in zin.namelist():
            parts=f.split('/')
            if len(parts)==3 and parts[0]=='ppt' and parts[1]=='slides':
                fn=parts[2]
                if fn.startswith('slide') and fn.endswith('.xml'):
                    try:int(fn[5:-4]);slide_files.append(f)
                    except:pass
        slide_files.sort(key=lambda x:int(x.split('/')[-1][5:-4]))
        all_cards=[cards2,cards3,cards4,cards5,cards6,cards7,cards8,cards9]
        
        for fname in zin.infolist():
            data=zin.read(fname.filename)
            if fname.filename in slide_files:
                idx=slide_files.index(fname.filename)
                sld=etree.fromstring(data)
                for tag in ('transition','timing','clrMapOvr'):
                    el=sld.find(f'{{{P}}}{tag}')
                    if el is not None:sld.remove(el)
                
                if idx in (0,9):  # s1, s10: 用全部形状
                    cSld=sld.find(f'{{{P}}}cSld');spTree=cSld.find(f'{{{P}}}spTree')
                    aids=set()
                    for st in ('sp','grpSp','pic','graphicFrame'):
                        for el in spTree.findall(f'{{{P}}}{st}'):
                            for nt in ('nvSpPr','nvGrpSpPr','nvPicPr','nvGraphicFramePr','nvCxnSpPr'):
                                nv=el.find(f'{{{P}}}{nt}')
                                if nv is not None:
                                    cnv=nv.find(f'{{{P}}}cNvPr')
                                    if cnv is not None and cnv.get('id'):aids.add(int(cnv.get('id')))
                    ids=sorted(aids)
                    timing=clone_timing(ids)
                elif idx-1<len(all_cards):  # 卡片页: 只用卡片
                    ids=all_cards[idx-1]
                    timing=clone_timing(ids)
                else:
                    timing=clone_timing([])
                
                sld.append(timing)
                trans=make_transition(transitions[idx])
                sld.insert(list(sld).index(sld.find(f'{{{P}}}cSld'))+1,trans)
                sld.append(etree.Element(f'{{{P}}}clrMapOvr'))
                data=etree.tostring(sld,xml_declaration=True,encoding='UTF-8',standalone=True)
            zout.writestr(fname,data)

import os;os.replace(out+'.tmp',out)

# 验证
with zipfile.ZipFile(out,'r') as z:
    for f in sorted(z.namelist()):
        parts=f.split('/')
        if len(parts)==3 and parts[0]=='ppt' and parts[1]=='slides':
            fn=parts[2]
            if fn.startswith('slide') and fn.endswith('.xml'):
                try:int(fn[5:-4])
                except:continue
                with z.open(f) as ff:
                    sld=etree.fromstring(ff.read())
                    tm=sld.find(f'{{{P}}}timing')
                    tr=sld.find(f'{{{P}}}transition')
                    ck=len(tm.findall(f'.//{{{P}}}cTn[@nodeType="clickEffect"]')) if tm is not None else 0
                    trt=tr[0].tag.split('}')[-1] if tr is not None and len(tr)>0 else 'none'
                    print(f"{fn}: clickEffects={ck}, transition={trt}")
print(f"\nSaved: {out}")
