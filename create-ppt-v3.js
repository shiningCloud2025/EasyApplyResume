const pptxgen = require("pptxgenjs");
const pres = new pptxgen();
pres.layout = "LAYOUT_16x9";
pres.author = "shiningCloud2025";
pres.title = "EasyApplyResume";

// ===== 全页统一配色：浅色主题 =====
const C = {
  bg:       "F1F5F9",
  white:    "FFFFFF",
  primary:  "1E3A5F",
  accent:   "0EA5E9",
  teal:     "14B8A6",
  dark:     "1E293B",
  mid:      "475569",
  light:    "94A3B8",
  border:   "CBD5E1",
  codeBg:   "0F172A",
};
const FH = "Trebuchet MS";
const FB = "Calibri";

function shd() { return { type:"outer", color:"000000", blur:5, offset:2, angle:135, opacity:0.07 }; }

// 统一顶部装饰条+标题
function titleBar(slide, title, subtitle) {
  slide.addShape(pres.shapes.RECTANGLE, { x:0, y:0, w:10, h:0.05, fill:{color:C.accent} });
  slide.addText(title, { x:0.6, y:0.15, w:8.8, h:0.5, fontSize:26, fontFace:FH, color:C.primary, bold:true, margin:0 });
  if (subtitle) {
    slide.addText(subtitle, { x:0.6, y:0.62, w:8.8, h:0.28, fontSize:10.5, fontFace:FB, color:C.mid, margin:0 });
  }
  slide.addShape(pres.shapes.LINE, { x:0.6, y:0.95, w:8.8, h:0, line:{color:C.border, width:0.8} });
}

function footer(slide, page) {
  slide.addShape(pres.shapes.LINE, { x:0.5, y:5.25, w:9, h:0, line:{color:C.border, width:0.5} });
  slide.addText("EasyApplyResume  |  "+page, { x:0.5, y:5.3, w:9, h:0.22, fontSize:7.5, fontFace:FB, color:C.light, align:"right", valign:"middle" });
}

// ============================================================
// SLIDE 1 - 封面
// ============================================================
const s1 = pres.addSlide();
s1.background = { color: C.bg };

// Top bar
s1.addShape(pres.shapes.RECTANGLE, { x:0, y:0, w:10, h:0.06, fill:{color:C.accent} });
// Left colored block
s1.addShape(pres.shapes.RECTANGLE, { x:0.5, y:1.0, w:0.06, h:3.8, fill:{color:C.primary} });

s1.addText("EasyApplyResume", { x:1.0, y:1.2, w:8, h:0.85, fontSize:40, fontFace:FH, color:C.primary, bold:true, margin:0 });
s1.addShape(pres.shapes.LINE, { x:1.0, y:2.1, w:2.5, h:0, line:{color:C.accent, width:2.5} });
s1.addText("易投简历 — AI 智能求职与简历管理平台", { x:1.0, y:2.3, w:8, h:0.45, fontSize:18, fontFace:FB, color:C.accent, margin:0 });
s1.addText("Spring AI · ReAct Agent · RAG · 多模型集成", { x:1.0, y:2.8, w:8, h:0.35, fontSize:12, fontFace:FB, color:C.light, margin:0 });

s1.addText([
  { text:"技术架构：", options:{bold:true, color:C.dark} },
  { text:"Spring Boot 3.3.5 + Java 21 + MySQL + PostgreSQL + Redis\n", options:{color:C.mid, breakLine:true} },
  { text:"AI 能力：", options:{bold:true, color:C.dark} },
  { text:"Spring AI + DashScope + 智谱AI + 豆包 + Ollama\n", options:{color:C.mid, breakLine:true} },
  { text:"部署方式：", options:{bold:true, color:C.dark} },
  { text:"Docker + Nacos + Spring Boot Admin + Prometheus", options:{color:C.mid} },
], { x:1.0, y:3.3, w:5.5, h:1.4, fontSize:11, fontFace:FB, lineSpacingMultiple:1.35, margin:0 });

s1.addText("shiningCloud2025  |  2026年6月", { x:1.0, y:5.1, w:4, h:0.22, fontSize:9, fontFace:FB, color:C.light, margin:0 });

footer(s1, "1 / 9");

// ============================================================
// SLIDE 2 - 问题背景
// ============================================================
const s2 = pres.addSlide(); s2.background = { color:C.bg };
titleBar(s2, "问题背景", "当前求职市场的核心痛点与 AI 破局之道");

const pains = [
  { t:"简历制作低效", d:"缺乏专业指导，简历内容空洞、结构混乱、表达不专业，严重影响面试邀约率" },
  { t:"求职信息不对称", d:"难以获取全面职位信息、行业动态和公司评价，缺乏有效决策依据" },
  { t:"管理者运营负担重", d:"HR和管理员面临大量重复性简历筛选、用户管理、数据统计，效率低下" },
  { t:"缺乏个性化服务", d:"传统平台千篇一律，无法根据个人背景和目标岗位提供量身定制优化建议" },
];
pains.forEach((p,i)=>{
  const col=i%2, row=Math.floor(i/2);
  const x=0.6+col*4.65, y=1.2+row*1.95;
  s2.addShape(pres.shapes.RECTANGLE, { x,y,w:4.35,h:1.72, fill:{color:C.white}, shadow:shd() });
  s2.addShape(pres.shapes.RECTANGLE, { x,y,w:0.05,h:1.72, fill:{color:row===0?C.accent:C.teal} });
  s2.addText(p.t, { x:x+0.25, y:y+0.15, w:3.85, h:0.33, fontSize:14, fontFace:FH, color:C.dark, bold:true, margin:0 });
  s2.addText(p.d, { x:x+0.25, y:y+0.6, w:3.85, h:0.95, fontSize:10.5, fontFace:FB, color:C.mid, margin:0, lineSpacingMultiple:1.3 });
});
s2.addShape(pres.shapes.RECTANGLE, { x:0.6, y:5.0, w:8.8, h:0.24, fill:{color:C.accent, transparency:88} });
s2.addText("核心思路：基于 Spring AI 生态，构建 AI Agent 驱动的智能求职助手，实现简历优化、智能问答、自动化运营", { x:0.75, y:5.0, w:8.5, h:0.24, fontSize:9.5, fontFace:FB, color:C.primary, italic:true, valign:"middle", margin:0 });
footer(s2, "2 / 9");

// ============================================================
// SLIDE 3 - 技术选型
// ============================================================
const s3 = pres.addSlide(); s3.background = { color:C.bg };
titleBar(s3, "技术选型", "Spring Boot 3.3.5 + Java 21 微服务架构，多层次 AI 能力集成");

const techs = [
  { t:"基础框架", c:C.accent, items:["Spring Boot 3.3.5 (Java 21)","MyBatis-Plus 3.5.7","Spring Security + JWT","Nacos 配置/服务发现","Docker 容器化部署"] },
  { t:"AI & LLM", c:C.teal, items:["Spring AI 1.0.0-M6","通义千问 (DashScope)","智谱AI (GLM-4)","豆包 (OpenAI协议)","Ollama 本地模型"] },
  { t:"数据与存储", c:C.primary, items:["MySQL 8.0 (主库)","PostgreSQL + PgVector","Redis (缓存/JWT)","MinIO / 七牛云 OSS","Spring Data Redis"] },
];
techs.forEach((cat,i)=>{
  const x=0.5+i*3.2, y=1.25;
  s3.addShape(pres.shapes.RECTANGLE, { x,y,w:2.95,h:0.4, fill:{color:cat.c} });
  s3.addText(cat.t, { x,y,w:2.95,h:0.4, fontSize:13, fontFace:FH, color:C.white, bold:true, align:"center", valign:"middle", margin:0 });
  cat.items.forEach((item,j)=>{
    s3.addShape(pres.shapes.RECTANGLE, { x, y:y+0.53+j*0.54, w:2.95,h:0.47, fill:{color:C.white}, shadow:shd() });
    s3.addText(item, { x:x+0.18, y:y+0.53+j*0.54, w:2.6, h:0.47, fontSize:10.5, fontFace:FB, color:C.dark, valign:"middle", margin:0 });
  });
});
s3.addShape(pres.shapes.RECTANGLE, { x:0.5, y:4.3, w:9, h:0.5, fill:{color:C.white}, shadow:shd() });
s3.addShape(pres.shapes.RECTANGLE, { x:0.5, y:4.3, w:0.05, h:0.5, fill:{color:C.teal} });
s3.addText([{text:"AI 能力矩阵：", options:{bold:true, color:C.dark}},{text:"ReAct Agent · RAG 检索增强 · MCP 客户端 · 多模型适配 · 向量数据库 · Function Calling · 对话记忆 · 敏感词过滤", options:{color:C.mid}}], { x:0.75, y:4.32, w:8.5, h:0.45, fontSize:10.5, fontFace:FB, margin:0, valign:"middle" });
footer(s3, "3 / 9");

// ============================================================
// SLIDE 4 - AI智能体架构
// ============================================================
const s4 = pres.addSlide(); s4.background = { color:C.bg };
titleBar(s4, "AI 智能体架构设计", "自研 ReAct Agent 模式：思考 → 行动循环 + 工具调用 + RAG 增强");

s4.addShape(pres.shapes.RECTANGLE, { x:0.6, y:1.2, w:4.5, h:3.7, fill:{color:C.white}, shadow:shd() });
s4.addText("Agent 继承体系", { x:0.8, y:1.28, w:4.1, h:0.3, fontSize:13, fontFace:FH, color:C.primary, bold:true, margin:0 });
[{l:"BaseAgent",d:"状态管理 · 步骤循环 · 流式SSE · 资源清理",w:3.0,c:C.primary},
 {l:"ReActAgent",d:"Think & Act 模式 · 思考-行动循环 · 步骤编排",w:3.4,c:"2563EB"},
 {l:"ToolCallAgent",d:"工具调用管理 · 对话上下文 · 终止判断",w:3.8,c:C.accent}].forEach((ly,i)=>{
  const bx=0.85+(4.0-ly.w)/2, by=1.8+i*1.15;
  s4.addShape(pres.shapes.RECTANGLE, { x:bx, y:by, w:ly.w, h:0.72, fill:{color:ly.c}, shadow:shd() });
  s4.addText(ly.l, { x:bx, y:by+0.04, w:ly.w, h:0.3, fontSize:12, fontFace:FH, color:C.white, bold:true, align:"center", valign:"middle", margin:0 });
  s4.addText(ly.d, { x:bx, y:by+0.36, w:ly.w, h:0.26, fontSize:8, fontFace:FB, color:"CBD5E1", align:"center", valign:"middle", margin:0 });
  if(i<2) s4.addShape(pres.shapes.LINE, { x:2.85, y:by+0.72, w:0, h:0.43, line:{color:C.light, width:1, dashType:"dash"} });
});

s4.addShape(pres.shapes.RECTANGLE, { x:5.35, y:1.2, w:4.15, h:3.7, fill:{color:C.white}, shadow:shd() });
s4.addText("核心组件", { x:5.55, y:1.28, w:3.75, h:0.3, fontSize:13, fontFace:FH, color:C.primary, bold:true, margin:0 });
[{l:"ChatMemory",d:"MySQL+内存混合持久化，多轮对话上下文"},
 {l:"RAG Advisor",d:"PgVector向量库+云知识库+查询重写"},
 {l:"Tools (8个)",d:"网页搜索·终端操作·文件管理·PDF·邮件"},
 {l:"敏感词过滤",d:"双端Advisor，覆盖百种违规类型"},
 {l:"MCP Client",d:"Stdio/SSE协议工具集成"},
 {l:"LLM Router",d:"DashScope/智谱/豆包/Ollama自由切换"}].forEach((cp,i)=>{
  const y=1.8+i*0.5;
  s4.addShape(pres.shapes.RECTANGLE, { x:5.55, y, w:3.75, h:0.44, fill:{color:C.bg}, shadow:shd() });
  s4.addShape(pres.shapes.RECTANGLE, { x:5.55, y, w:0.05, h:0.44, fill:{color:i%2===0?C.accent:C.teal} });
  s4.addText(cp.l, { x:5.8, y:y+0.03, w:3.35, h:0.2, fontSize:10.5, fontFace:FH, color:C.primary, bold:true, margin:0 });
  s4.addText(cp.d, { x:5.8, y:y+0.23, w:3.35, h:0.18, fontSize:7.5, fontFace:FB, color:C.mid, margin:0 });
});
s4.addText([{text:"ResumeAssistantAgent", options:{bold:true, color:C.accent}},{text:" — C端助手　　　", options:{color:C.dark}},{text:"SystemAssistantAgent", options:{bold:true, color:C.teal}},{text:" — B端助手", options:{color:C.dark}}], { x:0.8, y:4.95, w:8.5, h:0.2, fontSize:9.5, fontFace:FB, margin:0 });
footer(s4, "4 / 9");

// ============================================================
// SLIDE 5 - C端功能
// ============================================================
const s5 = pres.addSlide(); s5.background = { color:C.bg };
titleBar(s5, "核心功能：C端 — AI 简历助手", "面向求职者的全流程 AI 辅助：从简历优化到求职决策");
[
  { t:"智能简历润色", d:"语法优化 · 专业表达\n结构重组 · 量化成果\nSTAR法则改写" },
  { t:"定制化建议", d:"按行业/岗位定制\n保研/留学场景适配\n多版本输出 · 关键词优化" },
  { t:"RAG 知识增强", d:"PgVector 向量库\n云知识库检索\n查询重写 · 实时行业数据" },
  { t:"工具调用能力", d:"联网搜索职位\n简历PDF生成 · 邮件\n文件下载 · 网页抓取" },
  { t:"流式对话体验", d:"SSE实时流式输出\n多轮上下文记忆\n混合持久化 · 敏感词过滤" },
  { t:"求职生态服务", d:"职位浏览/收藏\n简历模板 · 面试题库\n行业资讯 · 地区数据" },
].forEach((f,i)=>{
  const col=i%3, row=Math.floor(i/3), x=0.5+col*3.15, y=1.2+row*2.05;
  s5.addShape(pres.shapes.RECTANGLE, { x,y,w:2.95,h:1.83, fill:{color:C.white}, shadow:shd() });
  s5.addShape(pres.shapes.RECTANGLE, { x,y,w:2.95,h:0.05, fill:{color:row===0?C.accent:C.teal} });
  s5.addText(f.t, { x:x+0.2, y:y+0.15, w:2.55, h:0.33, fontSize:13, fontFace:FH, color:C.primary, bold:true, margin:0 });
  s5.addText(f.d, { x:x+0.2, y:y+0.6, w:2.55, h:1.05, fontSize:10, fontFace:FB, color:C.mid, margin:0, lineSpacingMultiple:1.25 });
});
footer(s5, "5 / 9");

// ============================================================
// SLIDE 6 - B端功能
// ============================================================
const s6 = pres.addSlide(); s6.background = { color:C.bg };
titleBar(s6, "核心功能：B端 — AI 系统管理助手", "面向管理员的 AI 运维辅助：权限管理 · 内容运营 · 数据监控 · 智能诊断");

[{t:"管理员管理",d:"增删改查 · RBAC角色权限 · 账户状态管理 · 登录日志"},
 {t:"内容运营",d:"公告/广告管理 · FAQ问答库 · 项目/团队介绍 · 合作伙伴 · 媒体报道"},
 {t:"数据管理",d:"省市区地图 · 行业分类 · 大学库 · 职位库 · 简历模板 · 求职攻略"},
 {t:"AI 运营工具",d:"评分模型训练数据 · 模型版本管理 · LLM工具配置 · 邮件通信群发"},
 {t:"监控统计",d:"日活/访问量统计 · 服务机器监控 · Prometheus+Grafana 指标体系"}].forEach((m,i)=>{
  const y=1.2+i*0.78;
  s6.addShape(pres.shapes.RECTANGLE, { x:0.5, y, w:5.3, h:0.68, fill:{color:C.white}, shadow:shd() });
  s6.addShape(pres.shapes.RECTANGLE, { x:0.5, y, w:0.05, h:0.68, fill:{color:C.accent} });
  s6.addText(m.t, { x:0.8, y:y+0.05, w:4.8, h:0.25, fontSize:12, fontFace:FH, color:C.primary, bold:true, margin:0 });
  s6.addText(m.d, { x:0.8, y:y+0.33, w:4.8, h:0.25, fontSize:9, fontFace:FB, color:C.mid, margin:0 });
});
s6.addShape(pres.shapes.RECTANGLE, { x:6.1, y:1.2, w:3.5, h:3.7, fill:{color:C.white}, shadow:shd() });
s6.addShape(pres.shapes.RECTANGLE, { x:6.1, y:1.2, w:3.5, h:0.42, fill:{color:C.primary} });
s6.addText("AI 管理助手能力", { x:6.1, y:1.2, w:3.5, h:0.42, fontSize:12, fontFace:FH, color:C.white, bold:true, align:"center", valign:"middle", margin:0 });
["引导式故障诊断与排查","系统配置与运维指导","数据报表智能解读","用户/简历问题快速定位","MCP + RAG + 工具调用增强","独立对话记忆管理","管理端专属敏感词过滤","Re2 重读增强理解"].forEach((a,i)=>{
  const y=1.8+i*0.37;
  s6.addShape(pres.shapes.RECTANGLE, { x:6.25, y:y+0.08, w:0.16, h:0.16, fill:{color:C.accent} });
  s6.addText(a, { x:6.55, y:y, w:2.85, h:0.32, fontSize:9.5, fontFace:FB, color:C.dark, valign:"middle", margin:0 });
});
footer(s6, "6 / 9");

// ============================================================
// SLIDE 7 - 代码实现
// ============================================================
const s7 = pres.addSlide(); s7.background = { color:C.bg };
titleBar(s7, "核心代码实现", "ReAct Agent 思考执行 · 工具调用 · 双安全过滤链");

const code1 = `// ToolCallAgent.java — think()
@Override
public boolean think() {
  if (getNextStepPrompt() != null
    && !getNextStepPrompt().isEmpty()){
    getMessageList().add(
      new UserMessage(getNextStepPrompt()));
  }
  Prompt prompt = new Prompt(
    getMessageList(),chatOptions);
  ChatResponse chatResponse =
    getChatClient().prompt(prompt)
    .system(getSystemPrompt())
    .tools(availableTools)
    .call().chatResponse();
  this.toolCallChatResponse =
    chatResponse;
  AssistantMessage am =
    chatResponse.getResult().getOutput();
  List<ToolCall> tcs = am.getToolCalls();
  if(tcs.isEmpty()){
    getMessageList().add(am);
    return false;  // 无需工具
  }
  return true;  // 需要执行工具
}`;

const code2 = `// Security双链配置
@Configuration @EnableWebSecurity
@Order(1)
public class UserSecurityConfig {
 @Bean public SecurityFilterChain
  userSecurityFilterChain(HttpSecurity h){
   h.securityMatcher("/user/**")
    .cors(...)
    .csrf(AbstractHttpConfigurer::disable)
    .sessionManagement(s->s
     .sessionCreationPolicy(
       SessionCreationPolicy.STATELESS))
    .authorizeHttpRequests(auth->auth
     .requestMatchers(
       "/user/auth/**").permitAll()
     .anyRequest().authenticated())
    .exceptionHandling(ex->ex
     .authenticationEntryPoint(...))
    .addFilterBefore(userJwtAuthFilter,
     UsernamePasswordAuthFilter.class);
   return h.build();
}}`;

s7.addShape(pres.shapes.RECTANGLE, { x:0.4, y:1.15, w:4.55, h:3.75, fill:{color:C.codeBg}, shadow:shd() });
s7.addText("ToolCallAgent.java — think() 方法", { x:0.55, y:1.18, w:4.2, h:0.22, fontSize:8.5, fontFace:"Consolas", color:C.teal, bold:true, margin:0 });
s7.addText(code1, { x:0.55, y:1.42, w:4.2, h:3.35, fontSize:7.2, fontFace:"Consolas", color:"CBD5E1", margin:0, lineSpacingMultiple:1.18 });

s7.addShape(pres.shapes.RECTANGLE, { x:5.05, y:1.15, w:4.55, h:3.75, fill:{color:C.codeBg}, shadow:shd() });
s7.addText("UserSecurityConfig.java — 双过滤器链", { x:5.2, y:1.18, w:4.2, h:0.22, fontSize:8.5, fontFace:"Consolas", color:C.teal, bold:true, margin:0 });
s7.addText(code2, { x:5.2, y:1.42, w:4.2, h:3.35, fontSize:7.2, fontFace:"Consolas", color:"CBD5E1", margin:0, lineSpacingMultiple:1.18 });

s7.addShape(pres.shapes.RECTANGLE, { x:0.4, y:4.97, w:9.2, h:0.22, fill:{color:C.accent, transparency:85} });
s7.addText("以上均来自项目真实源码：ToolCallAgent.java 与 UserSecurityConfig.java", { x:0.55, y:4.97, w:8.9, h:0.22, fontSize:7.5, fontFace:FB, color:C.primary, italic:true, valign:"middle", margin:0 });
footer(s7, "7 / 9");

// ============================================================
// SLIDE 8 - 未来前景
// ============================================================
const s8 = pres.addSlide(); s8.background = { color:C.bg };
titleBar(s8, "未来前景", "持续迭代 · 拥抱 AI 前沿 · 打造全链路智能求职生态");

[{p:"近期 (1-3月)",c:C.accent, items:["前端界面开发完成","多租户 SaaS 化改造","接入 DeepSeek 等更多 LLM","简历评分模型上线 (XGBoost)"]},
 {p:"中期 (3-6月)",c:C.teal, items:["微服务化拆分","全链路可观测性增强","智能岗位匹配推荐","移动端适配 (小程序)"]},
 {p:"远期 (6-12月)",c:C.primary, items:["多 Agent 协作架构","AI 模拟面试与反馈","企业端 AI 筛选助手","构建求职知识图谱"]}].forEach((fp,i)=>{
  const x=0.4+i*3.2, y=1.25;
  s8.addShape(pres.shapes.RECTANGLE, { x,y,w:2.95,h:0.42, fill:{color:fp.c} });
  s8.addText(fp.p, { x,y,w:2.95,h:0.42, fontSize:13, fontFace:FH, color:C.white, bold:true, align:"center", valign:"middle", margin:0 });
  fp.items.forEach((it,j)=>{
    s8.addShape(pres.shapes.RECTANGLE, { x, y:y+0.55+j*0.62, w:2.95, h:0.54, fill:{color:C.white}, shadow:shd() });
    s8.addShape(pres.shapes.RECTANGLE, { x, y:y+0.55+j*0.62, w:0.05, h:0.54, fill:{color:fp.c} });
    s8.addText(it, { x:x+0.2, y:y+0.55+j*0.62, w:2.55, h:0.54, fontSize:10, fontFace:FB, color:C.dark, valign:"middle", margin:0 });
  });
});
s8.addShape(pres.shapes.RECTANGLE, { x:0.4, y:4.5, w:9.2, h:0.5, fill:{color:C.primary} });
s8.addText("愿景：成为求职者最信赖的 AI 求职伙伴，用人均 AI 赋能每位求职者找到理想工作", { x:0.6, y:4.5, w:8.8, h:0.5, fontSize:13, fontFace:FH, color:C.white, bold:true, align:"center", valign:"middle", margin:0 });
footer(s8, "8 / 9");

// ============================================================
// SLIDE 9 - 致谢
// ============================================================
const s9 = pres.addSlide(); s9.background = { color:C.bg };

s9.addShape(pres.shapes.RECTANGLE, { x:0, y:0, w:10, h:0.06, fill:{color:C.accent} });
s9.addShape(pres.shapes.LINE, { x:3.5, y:5.545, w:3, h:0, line:{color:C.accent, width:0.5} });

s9.addText("致  谢", { x:1, y:1.0, w:8, h:0.9, fontSize:42, fontFace:FH, color:C.primary, bold:true, align:"center", valign:"middle", margin:0 });
s9.addShape(pres.shapes.LINE, { x:4.0, y:2.0, w:2, h:0, line:{color:C.accent, width:2} });

s9.addText([
  {text:"感谢各位老师和同学对 EasyApplyResume 项目的关注与支持\n\n", options:{breakLine:true, color:C.mid, fontSize:14} },
  {text:"本项目基于 Spring AI 生态，探索了 AI Agent 在求职领域的应用\n", options:{breakLine:true, color:C.mid, fontSize:11.5} },
  {text:"从自研 ReAct Agent 到 RAG 增强检索，从多模型适配到工具集成\n", options:{breakLine:true, color:C.mid, fontSize:11.5} },
  {text:"这是一次将前沿 AI 技术与实际业务场景结合的有益尝试\n\n", options:{breakLine:true, color:C.mid, fontSize:11.5} },
  {text:"技术栈：", options:{bold:true, color:C.accent, fontSize:11.5} },
  {text:"Spring Boot 3.3.5 · Spring AI · MyBatis-Plus · PgVector · Redis · Nacos · Docker\n", options:{breakLine:true, color:C.light, fontSize:11.5} },
  {text:"AI模型：", options:{bold:true, color:C.accent, fontSize:11.5} },
  {text:"通义千问 · 智谱AI (GLM-4) · 豆包 · Ollama\n\n", options:{breakLine:true, color:C.light, fontSize:11.5} },
  {text:"shining_cloud2025@163.com  |  github.com/shiningCloud2025", options:{color:C.accent, fontSize:11.5} },
], { x:1.5, y:2.3, w:7, h:2.8, fontFace:FB, align:"center", valign:"top", margin:0, lineSpacingMultiple:1.25 });

footer(s9, "9 / 9");

pres.writeFile({ fileName:"out/EasyApplyResume-项目介绍-v3.pptx" }).then(()=>console.log("V3 OK")).catch(e=>console.error(e));
