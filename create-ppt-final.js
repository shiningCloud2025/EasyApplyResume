const pptxgen = require("pptxgenjs");
const pres = new pptxgen();
pres.layout = "LAYOUT_16x9";
pres.author = "shiningCloud2025";
pres.title = "EasyApplyResume";

// ===== 全页统一风格：浅色主题 =====
const C = { bg:"F1F5F9", white:"FFFFFF", p:"1E3A5F", a:"0EA5E9", t:"14B8A6", dk:"1E293B", md:"475569", lt:"94A3B8", b:"CBD5E1", code:"0F172A" };
const FH="Trebuchet MS", FB="Calibri";
function shd(){return{type:"outer",color:"000000",blur:5,offset:2,angle:135,opacity:0.07}}
function tb(s,ti,sub){
  s.addShape(pres.shapes.RECTANGLE,{x:0,y:0,w:10,h:0.05,fill:{color:C.a}});
  s.addText(ti,{x:0.6,y:0.15,w:8.8,h:0.5,fontSize:26,fontFace:FH,color:C.p,bold:true,margin:0});
  if(sub)s.addText(sub,{x:0.6,y:0.62,w:8.8,h:0.28,fontSize:10.5,fontFace:FB,color:C.md,margin:0});
  s.addShape(pres.shapes.LINE,{x:0.6,y:0.95,w:8.8,h:0,line:{color:C.b,width:0.8}});
}
function ft(s,pn){s.addShape(pres.shapes.LINE,{x:0.5,y:5.25,w:9,h:0,line:{color:C.b,width:0.5}});s.addText("EasyApplyResume | "+pn,{x:0.5,y:5.3,w:9,h:0.22,fontSize:7.5,fontFace:FB,color:C.lt,align:"right",valign:"middle"});}

// ============================================================
// SLIDE 1 - 封面
// ============================================================
const s1=pres.addSlide();s1.background={color:C.bg};
s1.addShape(pres.shapes.RECTANGLE,{x:0,y:0,w:10,h:0.06,fill:{color:C.a}});
s1.addShape(pres.shapes.RECTANGLE,{x:0.5,y:1.0,w:0.06,h:3.8,fill:{color:C.p}});
s1.addText("EasyApplyResume",{x:1.0,y:1.2,w:8,h:0.85,fontSize:40,fontFace:FH,color:C.p,bold:true,margin:0});
s1.addShape(pres.shapes.LINE,{x:1.0,y:2.1,w:2.5,h:0,line:{color:C.a,width:2.5}});
s1.addText("易投简历 — AI 智能求职与简历管理平台",{x:1.0,y:2.3,w:8,h:0.45,fontSize:18,fontFace:FB,color:C.a,margin:0});
s1.addText("Spring AI · ReAct Agent · RAG · 多模型集成",{x:1.0,y:2.8,w:8,h:0.35,fontSize:12,fontFace:FB,color:C.lt,margin:0});
s1.addText([{text:"技术架构：",options:{bold:true,color:C.dk}},{text:"Spring Boot 3.3.5 + Java 21 + MySQL + PostgreSQL + Redis\n",options:{color:C.md,breakLine:true}},{text:"AI 能力：",options:{bold:true,color:C.dk}},{text:"Spring AI + DashScope + 智谱AI + 豆包 + Ollama\n",options:{color:C.md,breakLine:true}},{text:"部署方式：",options:{bold:true,color:C.dk}},{text:"Docker + Nacos + Spring Boot Admin + Prometheus",options:{color:C.md}}],{x:1.0,y:3.3,w:5.5,h:1.4,fontSize:11,fontFace:FB,lineSpacingMultiple:1.35,margin:0});
s1.addText("shiningCloud2025 | 2026年6月",{x:1.0,y:5.1,w:4,h:0.22,fontSize:9,fontFace:FB,color:C.lt,margin:0});
ft(s1,"1 / 9");

// ============================================================
// SLIDE 2 - 问题背景
// ============================================================
const s2=pres.addSlide();s2.background={color:C.bg};tb(s2,"问题背景","当前求职市场的核心痛点与 AI 破局之道");
[{t:"简历制作低效",d:"缺乏专业指导，简历内容空洞、结构混乱、表达不专业，严重影响面试邀约率"},
 {t:"求职信息不对称",d:"难以获取全面职位信息、行业动态和公司评价，缺乏有效决策依据"},
 {t:"管理者运营负担重",d:"HR和管理员面临大量重复性简历筛选、用户管理、数据统计，效率低下"},
 {t:"缺乏个性化服务",d:"传统平台千篇一律，无法根据个人背景和目标岗位提供量身定制优化建议"}].forEach((p,i)=>{
  const c=i%2,r=Math.floor(i/2),x=0.6+c*4.65,y=1.2+r*1.95;
  s2.addShape(pres.shapes.RECTANGLE,{x,y,w:4.35,h:1.72,fill:{color:C.white},shadow:shd()});
  s2.addShape(pres.shapes.RECTANGLE,{x,y,w:0.05,h:1.72,fill:{color:r===0?C.a:C.t}});
  s2.addText(p.t,{x:x+0.25,y:y+0.15,w:3.85,h:0.33,fontSize:14,fontFace:FH,color:C.dk,bold:true,margin:0});
  s2.addText(p.d,{x:x+0.25,y:y+0.6,w:3.85,h:0.95,fontSize:10.5,fontFace:FB,color:C.md,margin:0,lineSpacingMultiple:1.3});
});
s2.addShape(pres.shapes.RECTANGLE,{x:0.6,y:5.0,w:8.8,h:0.24,fill:{color:C.a,transparency:88}});
s2.addText("核心思路：基于 Spring AI 生态，构建 AI Agent 驱动的智能求职助手，实现简历优化、智能问答、自动化运营",{x:0.75,y:5.0,w:8.5,h:0.24,fontSize:9.5,fontFace:FB,color:C.p,italic:true,valign:"middle",margin:0});
ft(s2,"2 / 9");

// ============================================================
// SLIDE 3 - 技术选型
// ============================================================
const s3=pres.addSlide();s3.background={color:C.bg};tb(s3,"技术选型","Spring Boot 3.3.5 + Java 21 微服务架构，多层次 AI 能力集成");
[{t:"基础框架",c:C.a,it:["Spring Boot 3.3.5 (Java 21)","MyBatis-Plus 3.5.7","Spring Security + JWT","Nacos 配置/服务发现","Docker 容器化部署"]},
 {t:"AI & LLM",c:C.t,it:["Spring AI 1.0.0-M6","通义千问 (DashScope)","智谱AI (GLM-4)","豆包 (OpenAI协议)","Ollama 本地模型"]},
 {t:"数据与存储",c:C.p,it:["MySQL 8.0 (主库)","PostgreSQL + PgVector","Redis (缓存/JWT)","MinIO / 七牛云 OSS","Spring Data Redis"]}].forEach((c,i)=>{
  const x=0.5+i*3.2,y=1.25;
  s3.addShape(pres.shapes.RECTANGLE,{x,y,w:2.95,h:0.4,fill:{color:c.c}});s3.addText(c.t,{x,y,w:2.95,h:0.4,fontSize:13,fontFace:FH,color:C.white,bold:true,align:"center",valign:"middle",margin:0});
  c.it.forEach((it,j)=>{s3.addShape(pres.shapes.RECTANGLE,{x,y:y+0.53+j*0.54,w:2.95,h:0.47,fill:{color:C.white},shadow:shd()});s3.addText(it,{x:x+0.18,y:y+0.53+j*0.54,w:2.6,h:0.47,fontSize:10.5,fontFace:FB,color:C.dk,valign:"middle",margin:0});});
});
s3.addShape(pres.shapes.RECTANGLE,{x:0.5,y:4.3,w:9,h:0.5,fill:{color:C.white},shadow:shd()});s3.addShape(pres.shapes.RECTANGLE,{x:0.5,y:4.3,w:0.05,h:0.5,fill:{color:C.t}});
s3.addText([{text:"AI 能力矩阵：",options:{bold:true,color:C.dk}},{text:"ReAct Agent · RAG 检索增强 · MCP 客户端 · 多模型适配 · 向量数据库 · Function Calling · 对话记忆 · 敏感词过滤",options:{color:C.md}}],{x:0.75,y:4.32,w:8.5,h:0.45,fontSize:10.5,fontFace:FB,margin:0,valign:"middle"});
ft(s3,"3 / 9");

// ============================================================
// SLIDE 4 - AI智能体架构
// ============================================================
const s4=pres.addSlide();s4.background={color:C.bg};tb(s4,"AI 智能体架构设计","自研 ReAct Agent 模式：思考 → 行动循环 + 工具调用 + RAG 增强");
s4.addShape(pres.shapes.RECTANGLE,{x:0.6,y:1.2,w:4.5,h:3.7,fill:{color:C.white},shadow:shd()});
s4.addText("Agent 继承体系",{x:0.8,y:1.28,w:4.1,h:0.3,fontSize:13,fontFace:FH,color:C.p,bold:true,margin:0});
[{l:"BaseAgent",d:"状态管理 · 步骤循环 · 流式SSE · 资源清理",w:3.0,c:C.p},{l:"ReActAgent",d:"Think & Act 模式 · 思考-行动循环 · 步骤编排",w:3.4,c:"2563EB"},{l:"ToolCallAgent",d:"工具调用管理 · 对话上下文 · 终止判断",w:3.8,c:C.a}].forEach((ly,i)=>{
  const bx=0.85+(4.0-ly.w)/2,by=1.8+i*1.15;s4.addShape(pres.shapes.RECTANGLE,{x:bx,y:by,w:ly.w,h:0.72,fill:{color:ly.c},shadow:shd()});
  s4.addText(ly.l,{x:bx,y:by+0.04,w:ly.w,h:0.3,fontSize:12,fontFace:FH,color:C.white,bold:true,align:"center",valign:"middle",margin:0});
  s4.addText(ly.d,{x:bx,y:by+0.36,w:ly.w,h:0.26,fontSize:8,fontFace:FB,color:"CBD5E1",align:"center",valign:"middle",margin:0});if(i<2)s4.addShape(pres.shapes.LINE,{x:2.85,y:by+0.72,w:0,h:0.43,line:{color:C.lt,width:1,dashType:"dash"}});
});
s4.addShape(pres.shapes.RECTANGLE,{x:5.35,y:1.2,w:4.15,h:3.7,fill:{color:C.white},shadow:shd()});s4.addText("核心组件",{x:5.55,y:1.28,w:3.75,h:0.3,fontSize:13,fontFace:FH,color:C.p,bold:true,margin:0});
[{l:"ChatMemory",d:"MySQL+内存混合持久化，多轮对话上下文"},{l:"RAG Advisor",d:"PgVector向量库+云知识库+查询重写"},{l:"Tools (8个)",d:"网页搜索·终端操作·文件管理·PDF·邮件"},{l:"敏感词过滤",d:"双端Advisor，覆盖百种违规类型"},{l:"MCP Client",d:"Stdio/SSE协议工具集成"},{l:"LLM Router",d:"DashScope/智谱/豆包/Ollama自由切换"}].forEach((cp,i)=>{
  const y=1.8+i*0.5;s4.addShape(pres.shapes.RECTANGLE,{x:5.55,y,w:3.75,h:0.44,fill:{color:C.bg},shadow:shd()});s4.addShape(pres.shapes.RECTANGLE,{x:5.55,y,w:0.05,h:0.44,fill:{color:i%2===0?C.a:C.t}});
  s4.addText(cp.l,{x:5.8,y:y+0.03,w:3.35,h:0.2,fontSize:10.5,fontFace:FH,color:C.p,bold:true,margin:0});s4.addText(cp.d,{x:5.8,y:y+0.23,w:3.35,h:0.18,fontSize:7.5,fontFace:FB,color:C.md,margin:0});
});
s4.addText([{text:"ResumeAssistantAgent",options:{bold:true,color:C.a}},{text:" — C端助手　　　",options:{color:C.dk}},{text:"SystemAssistantAgent",options:{bold:true,color:C.t}},{text:" — B端助手",options:{color:C.dk}}],{x:0.8,y:4.95,w:8.5,h:0.2,fontSize:9.5,fontFace:FB,margin:0});ft(s4,"4 / 9");

// ============================================================
// SLIDE 5 - C端功能
// ============================================================
const s5=pres.addSlide();s5.background={color:C.bg};tb(s5,"核心功能：C端 — AI 简历助手","面向求职者的全流程 AI 辅助：从简历优化到求职决策");
[{t:"智能简历润色",d:"语法优化 · 专业表达\n结构重组 · 量化成果\nSTAR法则改写"},
 {t:"定制化建议",d:"按行业/岗位定制\n保研/留学场景适配\n多版本输出 · 关键词优化"},
 {t:"RAG 知识增强",d:"PgVector 向量库\n云知识库检索\n查询重写 · 实时行业数据"},
 {t:"工具调用能力",d:"联网搜索职位\n简历PDF生成 · 邮件\n文件下载 · 网页抓取"},
 {t:"流式对话体验",d:"SSE实时流式输出\n多轮上下文记忆\n混合持久化 · 敏感词过滤"},
 {t:"求职生态服务",d:"职位浏览/收藏\n简历模板 · 面试题库\n行业资讯 · 地区数据"}].forEach((f,i)=>{
  const c=i%3,r=Math.floor(i/3),x=0.5+c*3.15,y=1.2+r*2.05;
  s5.addShape(pres.shapes.RECTANGLE,{x,y,w:2.95,h:1.83,fill:{color:C.white},shadow:shd()});s5.addShape(pres.shapes.RECTANGLE,{x,y,w:2.95,h:0.05,fill:{color:r===0?C.a:C.t}});
  s5.addText(f.t,{x:x+0.2,y:y+0.15,w:2.55,h:0.33,fontSize:13,fontFace:FH,color:C.p,bold:true,margin:0});
  s5.addText(f.d,{x:x+0.2,y:y+0.6,w:2.55,h:1.05,fontSize:10,fontFace:FB,color:C.md,margin:0,lineSpacingMultiple:1.25});
});ft(s5,"5 / 9");

// ============================================================
// SLIDE 6 - B端功能
// ============================================================
const s6=pres.addSlide();s6.background={color:C.bg};tb(s6,"核心功能：B端 — AI 系统管理助手","面向管理员的 AI 运维辅助：权限管理 · 内容运营 · 数据监控 · 智能诊断");
[{t:"管理员管理",d:"增删改查 · RBAC角色权限 · 账户状态管理 · 登录日志"},
 {t:"内容运营",d:"公告/广告管理 · FAQ问答库 · 项目/团队介绍 · 合作伙伴 · 媒体报道"},
 {t:"数据管理",d:"省市区地图 · 行业分类 · 大学库 · 职位库 · 简历模板 · 求职攻略"},
 {t:"AI 运营工具",d:"评分模型训练数据 · 模型版本管理 · LLM工具配置 · 邮件通信群发"},
 {t:"监控统计",d:"日活/访问量统计 · 服务机器监控 · Prometheus+Grafana 指标体系"}].forEach((m,i)=>{
  const y=1.2+i*0.78;s6.addShape(pres.shapes.RECTANGLE,{x:0.5,y,w:5.3,h:0.68,fill:{color:C.white},shadow:shd()});s6.addShape(pres.shapes.RECTANGLE,{x:0.5,y,w:0.05,h:0.68,fill:{color:C.a}});
  s6.addText(m.t,{x:0.8,y:y+0.05,w:4.8,h:0.25,fontSize:12,fontFace:FH,color:C.p,bold:true,margin:0});s6.addText(m.d,{x:0.8,y:y+0.33,w:4.8,h:0.25,fontSize:9,fontFace:FB,color:C.md,margin:0});
});
s6.addShape(pres.shapes.RECTANGLE,{x:6.1,y:1.2,w:3.5,h:3.7,fill:{color:C.white},shadow:shd()});s6.addShape(pres.shapes.RECTANGLE,{x:6.1,y:1.2,w:3.5,h:0.42,fill:{color:C.p}});s6.addText("AI 管理助手能力",{x:6.1,y:1.2,w:3.5,h:0.42,fontSize:12,fontFace:FH,color:C.white,bold:true,align:"center",valign:"middle",margin:0});
["引导式故障诊断与排查","系统配置与运维指导","数据报表智能解读","用户/简历问题快速定位","MCP + RAG + 工具调用增强","独立对话记忆管理","管理端专属敏感词过滤","Re2 重读增强理解"].forEach((a,i)=>{const y=1.8+i*0.37;s6.addShape(pres.shapes.RECTANGLE,{x:6.25,y:y+0.08,w:0.16,h:0.16,fill:{color:C.a}});s6.addText(a,{x:6.55,y:y,w:2.85,h:0.32,fontSize:9.5,fontFace:FB,color:C.dk,valign:"middle",margin:0});});ft(s6,"6 / 9");

// ============================================================
// SLIDE 7 - D端功能 (数据与监控)
// ============================================================
const s7 = pres.addSlide(); s7.background = { color: C.bg };
tb(s7, "核心功能：D端 — 数据与监控平台", "面向数据分析与运维：实时监控 · 访问统计 · 机器管理 · 智能报表");

// Left: 4 data feature cards in 2x2
[
  { t: "实时访问监控", d: "日活/总访问量追踪\nC端用户日活统计\nB端管理员登录统计\n多维度数据看板", c: C.a },
  { t: "广告投放管理", d: "广告上下架与排期\n用户端广告展示控制\n投放效果数据追踪\n公告发布与管理", c: C.t },
  { t: "服务机器监控", d: "SSH远程连接管理\n服务器健康状态检测\n服务运行状态实时告警\n自动化巡检策略", c: C.p },
  { t: "智能数据报表", d: "Prometheus 指标采集\nGrafana 可视化大屏\n定时报表自动导出\n异常数据智能预警", c: "7C3AED" },
].forEach((f, i) => {
  const col = i % 2, row = Math.floor(i / 2);
  const x = 0.5 + col * 4.75, y = 1.2 + row * 2.1;
  s7.addShape(pres.shapes.RECTANGLE, { x, y, w: 4.4, h: 1.9, fill: { color: C.white }, shadow: shd() });
  s7.addShape(pres.shapes.RECTANGLE, { x, y, w: 0.06, h: 1.9, fill: { color: f.c } });
  // Icon area (colored circle placeholder)
  s7.addShape(pres.shapes.OVAL, { x: x + 0.3, y: y + 0.3, w: 0.55, h: 0.55, fill: { color: f.c } });
  s7.addText(f.t, { x: x + 1.0, y: y + 0.25, w: 3.1, h: 0.35, fontSize: 14, fontFace: FH, color: C.dk, bold: true, margin: 0 });
  s7.addText(f.d, { x: x + 1.0, y: y + 0.7, w: 3.1, h: 1.0, fontSize: 10, fontFace: FB, color: C.md, margin: 0, lineSpacingMultiple: 1.25 });
});

ft(s7, "7 / 9");

// ============================================================
// SLIDE 8 - 未来前景
// ============================================================
const s8=pres.addSlide();s8.background={color:C.bg};tb(s8,"未来前景","持续迭代 · 拥抱 AI 前沿 · 打造全链路智能求职生态");
[{p:"近期 (1-3月)",c:C.a,it:["前端界面开发完成","多租户 SaaS 化改造","接入 DeepSeek 等更多 LLM","简历评分模型上线 (XGBoost)"]},
 {p:"中期 (3-6月)",c:C.t,it:["微服务化拆分","全链路可观测性增强","智能岗位匹配推荐","移动端适配 (小程序)"]},
 {p:"远期 (6-12月)",c:C.p,it:["多 Agent 协作架构","AI 模拟面试与反馈","企业端 AI 筛选助手","构建求职知识图谱"]}].forEach((fp,i)=>{
  const x=0.4+i*3.2,y=1.25;s8.addShape(pres.shapes.RECTANGLE,{x,y,w:2.95,h:0.42,fill:{color:fp.c}});s8.addText(fp.p,{x,y,w:2.95,h:0.42,fontSize:13,fontFace:FH,color:C.white,bold:true,align:"center",valign:"middle",margin:0});
  fp.it.forEach((it,j)=>{s8.addShape(pres.shapes.RECTANGLE,{x,y:y+0.55+j*0.62,w:2.95,h:0.54,fill:{color:C.white},shadow:shd()});s8.addShape(pres.shapes.RECTANGLE,{x,y:y+0.55+j*0.62,w:0.05,h:0.54,fill:{color:fp.c}});s8.addText(it,{x:x+0.2,y:y+0.55+j*0.62,w:2.55,h:0.54,fontSize:10,fontFace:FB,color:C.dk,valign:"middle",margin:0});});
});
s8.addShape(pres.shapes.RECTANGLE,{x:0.4,y:4.5,w:9.2,h:0.5,fill:{color:C.p}});s8.addText("愿景：成为求职者最信赖的 AI 求职伙伴，用人均 AI 赋能每位求职者找到理想工作",{x:0.6,y:4.5,w:8.8,h:0.5,fontSize:13,fontFace:FH,color:C.white,bold:true,align:"center",valign:"middle",margin:0});ft(s8,"8 / 9");

// ============================================================
// SLIDE 9 - 致谢
// ============================================================
const s9=pres.addSlide();s9.background={color:C.bg};
s9.addShape(pres.shapes.RECTANGLE,{x:0,y:0,w:10,h:0.06,fill:{color:C.a}});
s9.addShape(pres.shapes.LINE,{x:3.5,y:5.545,w:3,h:0,line:{color:C.a,width:0.5}});
s9.addText("致  谢",{x:1,y:1.0,w:8,h:0.9,fontSize:42,fontFace:FH,color:C.p,bold:true,align:"center",valign:"middle",margin:0});
s9.addShape(pres.shapes.LINE,{x:4.0,y:2.0,w:2,h:0,line:{color:C.a,width:2}});
s9.addText([{text:"感谢各位老师和同学对 EasyApplyResume 项目的关注与支持\n\n",options:{breakLine:true,color:C.md,fontSize:14}},{text:"本项目基于 Spring AI 生态，探索了 AI Agent 在求职领域的应用\n",options:{breakLine:true,color:C.md,fontSize:11.5}},{text:"从自研 ReAct Agent 到 RAG 增强检索，从多模型适配到工具集成\n",options:{breakLine:true,color:C.md,fontSize:11.5}},{text:"这是一次将前沿 AI 技术与实际业务场景结合的有益尝试\n\n",options:{breakLine:true,color:C.md,fontSize:11.5}},{text:"技术栈：",options:{bold:true,color:C.a,fontSize:11.5}},{text:"Spring Boot 3.3.5 · Spring AI · MyBatis-Plus · PgVector · Redis · Nacos · Docker\n",options:{breakLine:true,color:C.lt,fontSize:11.5}},{text:"AI模型：",options:{bold:true,color:C.a,fontSize:11.5}},{text:"通义千问 · 智谱AI (GLM-4) · 豆包 · Ollama\n\n",options:{breakLine:true,color:C.lt,fontSize:11.5}},{text:"shining_cloud2025@163.com  |  github.com/shiningCloud2025",options:{color:C.a,fontSize:11.5}}],{x:1.5,y:2.3,w:7,h:2.8,fontFace:FB,align:"center",valign:"top",margin:0,lineSpacingMultiple:1.25});
ft(s9,"9 / 9");

// ===== OUTPUT =====
pres.writeFile({fileName:"out/EasyApplyResume-项目介绍-v4.pptx"}).then(()=>console.log("V4 OK")).catch(e=>console.error(e));
