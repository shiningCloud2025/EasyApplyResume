# EasyApplyResume - ML智能化扩展方案

## 一、项目背景

基于现有简历管理系统，扩展机器学习能力，提升系统智能化水平。

### 现有数据结构
```java
// 用户简历 - 核心字段
userSaveResumeResumeReactCode  // React组件代码（存储简历内容）
userSaveResumeIndustry         // 行业ID

// 招聘信息 - 可用于匹配
employmentInformationCompanyName     // 公司名称
employmentInformationIndustryCategories  // 行业大类
employmentInformationJobDescription  // 岗位描述（如有）
```

---

## 二、技术选型

| 用途 | 框架 | Maven依赖 |
|------|------|-----------|
| 深度学习 | DL4J | `org.deeplearning4j:deeplearning4j-core:1.0.0-M2.1` |
| 传统ML | Smile | `com.github.haifengl:smile-core:3.0.2` |
| 中文NLP | HanLP | `com.hankcs:hanlp:portable-1.8.4` |
| 向量计算 | ND4J | `org.nd4j:nd4j-native-platform:1.0.0-M2.1` |

### pom.xml 依赖配置
```xml
<!-- ML相关依赖 -->
<dependency>
    <groupId>com.github.haifengl</groupId>
    <artifactId>smile-core</artifactId>
    <version>3.0.2</version>
</dependency>
<dependency>
    <groupId>com.hankcs</groupId>
    <artifactId>hanlp</artifactId>
    <version>portable-1.8.4</version>
</dependency>
<dependency>
    <groupId>org.deeplearning4j</groupId>
    <artifactId>deeplearning4j-core</artifactId>
    <version>1.0.0-M2.1</version>
</dependency>
<dependency>
    <groupId>org.nd4j</groupId>
    <artifactId>nd4j-native-platform</artifactId>
    <version>1.0.0-M2.1</version>
</dependency>
```

---

## 三、功能模块详细设计

### 模块1：简历智能评分

#### 1.1 功能描述
对用户简历进行智能评分（0-100分），并给出改进建议。

#### 1.2 评分维度
| 维度 | 权重 | 计算方式 |
|------|------|---------|
| 内容完整度 | 30% | 检测关键模块（基本信息、教育、经验、技能） |
| 关键词密度 | 25% | 行业关键词命中率 |
| 篇幅合理性 | 15% | 文字长度在800-2000字为佳 |
| 结构规范性 | 15% | 检测React代码中的section数量 |
| 量化成果 | 15% | 检测数字、百分比等量化描述 |

#### 1.3 核心代码结构
```
src/main/java/com/zyh/easyapplyresume/
├── ml/
│   ├── config/
│   │   └── MLConfig.java                 // ML配置类
│   ├── service/
│   │   ├── ResumeScoreService.java       // 评分服务接口
│   │   └── impl/
│   │       └── ResumeScoreServiceImpl.java
│   ├── model/
│   │   └── ResumeScoreResult.java        // 评分结果VO
│   ├── feature/
│   │   ├── TextExtractor.java            // React代码文本提取
│   │   └── FeatureExtractor.java         // 特征提取器
│   └── scorer/
│       ├── CompletenessScorer.java       // 完整度评分
│       ├── KeywordScorer.java            // 关键词评分
│       └── StructureScorer.java          // 结构评分
```

#### 1.4 关键实现

**React代码文本提取器**
```java
@Component
public class TextExtractor {
    
    /**
     * 从React代码中提取纯文本
     */
    public String extractText(String reactCode) {
        if (StringUtils.isEmpty(reactCode)) {
            return "";
        }
        return reactCode
            .replaceAll("<[^>]+>", " ")           // 去除HTML/JSX标签
            .replaceAll("\\{[^}]*\\}", " ")       // 去除JS表达式 {xxx}
            .replaceAll("className=['\"][^'\"]*['\"]", "") // 去除className
            .replaceAll("style=\\{[^}]*\\}", "")  // 去除style
            .replaceAll("import[^;]+;", "")       // 去除import语句
            .replaceAll("export[^;]+;", "")       // 去除export语句
            .replaceAll("const [^=]+=", "")       // 去除变量声明
            .replaceAll("//[^\n]*", "")           // 去除单行注释
            .replaceAll("/\\*[^*]*\\*/", "")      // 去除多行注释
            .replaceAll("[\\r\\n]+", " ")         // 换行转空格
            .replaceAll("\\s+", " ")              // 合并多余空白
            .trim();
    }
}
```

**评分服务实现**
```java
@Service
public class ResumeScoreServiceImpl implements ResumeScoreService {
    
    @Autowired
    private TextExtractor textExtractor;
    
    @Autowired
    private IndustryKeywordMapper industryKeywordMapper;
    
    @Override
    public ResumeScoreResult score(String reactCode, Integer industryId) {
        // 1. 提取文本
        String text = textExtractor.extractText(reactCode);
        
        // 2. 计算各维度分数
        double completenessScore = calculateCompleteness(text);
        double keywordScore = calculateKeywordScore(text, industryId);
        double lengthScore = calculateLengthScore(text);
        double structureScore = calculateStructureScore(reactCode);
        double quantifyScore = calculateQuantifyScore(text);
        
        // 3. 加权计算总分
        double totalScore = completenessScore * 0.30 +
                           keywordScore * 0.25 +
                           lengthScore * 0.15 +
                           structureScore * 0.15 +
                           quantifyScore * 0.15;
        
        // 4. 生成改进建议
        List<String> suggestions = generateSuggestions(
            completenessScore, keywordScore, lengthScore, structureScore, quantifyScore
        );
        
        return new ResumeScoreResult(totalScore, suggestions);
    }
    
    // 完整度评分：检测关键模块
    private double calculateCompleteness(String text) {
        String[] requiredModules = {"姓名", "电话", "邮箱", "教育", "经验", "技能", "项目"};
        int hitCount = 0;
        for (String module : requiredModules) {
            if (text.contains(module)) hitCount++;
        }
        return (hitCount / (double) requiredModules.length) * 100;
    }
    
    // 关键词评分：匹配行业关键词
    private double calculateKeywordScore(String text, Integer industryId) {
        List<String> keywords = industryKeywordMapper.selectByIndustryId(industryId);
        if (keywords.isEmpty()) return 60; // 无关键词库时给基础分
        
        int hitCount = 0;
        for (String keyword : keywords) {
            if (text.toLowerCase().contains(keyword.toLowerCase())) {
                hitCount++;
            }
        }
        return Math.min((hitCount / (double) keywords.size()) * 150, 100);
    }
    
    // 篇幅评分
    private double calculateLengthScore(String text) {
        int length = text.length();
        if (length < 300) return 40;
        if (length < 800) return 60 + (length - 300) * 0.08;
        if (length <= 2000) return 100;
        if (length <= 3000) return 90;
        return 70; // 过长扣分
    }
    
    // 结构评分：检测section数量
    private double calculateStructureScore(String reactCode) {
        int sectionCount = countPattern(reactCode, "<(section|div)[^>]*class[^>]*(section|block|module)");
        if (sectionCount >= 5) return 100;
        if (sectionCount >= 3) return 80;
        if (sectionCount >= 1) return 60;
        return 40;
    }
    
    // 量化成果评分
    private double calculateQuantifyScore(String text) {
        int numberCount = countPattern(text, "\\d+[%万个人年月]");
        if (numberCount >= 10) return 100;
        if (numberCount >= 5) return 80;
        if (numberCount >= 2) return 60;
        return 40;
    }
}
```

**评分结果VO**
```java
@Data
public class ResumeScoreResult {
    private Double totalScore;          // 总分 0-100
    private Double completenessScore;   // 完整度分
    private Double keywordScore;        // 关键词分
    private Double lengthScore;         // 篇幅分
    private Double structureScore;      // 结构分
    private Double quantifyScore;       // 量化分
    private List<String> suggestions;   // 改进建议
    private String level;               // 评级：优秀/良好/一般/待改进
    
    public String getLevel() {
        if (totalScore >= 85) return "优秀";
        if (totalScore >= 70) return "良好";
        if (totalScore >= 55) return "一般";
        return "待改进";
    }
}
```

#### 1.5 API接口
```java
@RestController
@RequestMapping("/user/resume/ml")
@Tag(name = "简历ML智能服务-用户端")
public class ResumeMLController {
    
    @Autowired
    private ResumeScoreService resumeScoreService;
    
    @Operation(summary = "简历智能评分")
    @GetMapping("/score/{resumeId}")
    public BaseResult<ResumeScoreResult> scoreResume(@PathVariable Integer resumeId) {
        // 1. 查询简历
        UserSaveResume resume = resumeService.getById(resumeId);
        
        // 2. 评分
        ResumeScoreResult result = resumeScoreService.score(
            resume.getUserSaveResumeResumeReactCode(),
            resume.getUserSaveResumeIndustry()
        );
        
        return BaseResult.ok(result);
    }
}
```

---

### 模块2：岗位匹配预测

#### 2.1 功能描述
计算简历与目标岗位的匹配度百分比。

#### 2.2 匹配算法
采用 **TF-IDF + 余弦相似度** 方案：

```java
@Service
public class JobMatchServiceImpl implements JobMatchService {
    
    @Autowired
    private TextExtractor textExtractor;
    
    @Override
    public JobMatchResult match(String resumeReactCode, String jobDescription) {
        // 1. 提取简历文本
        String resumeText = textExtractor.extractText(resumeReactCode);
        
        // 2. 分词
        List<String> resumeWords = HanLP.segment(resumeText)
            .stream()
            .map(term -> term.word)
            .filter(word -> word.length() > 1)
            .collect(Collectors.toList());
            
        List<String> jobWords = HanLP.segment(jobDescription)
            .stream()
            .map(term -> term.word)
            .filter(word -> word.length() > 1)
            .collect(Collectors.toList());
        
        // 3. 构建词汇表
        Set<String> vocabulary = new HashSet<>();
        vocabulary.addAll(resumeWords);
        vocabulary.addAll(jobWords);
        
        // 4. 计算TF-IDF向量
        double[] resumeVector = calculateTfIdf(resumeWords, vocabulary);
        double[] jobVector = calculateTfIdf(jobWords, vocabulary);
        
        // 5. 计算余弦相似度
        double similarity = cosineSimilarity(resumeVector, jobVector);
        double matchScore = similarity * 100;
        
        // 6. 提取匹配的关键词
        List<String> matchedKeywords = findMatchedKeywords(resumeWords, jobWords);
        
        // 7. 提取缺失的关键词
        List<String> missingKeywords = findMissingKeywords(resumeWords, jobWords);
        
        return new JobMatchResult(matchScore, matchedKeywords, missingKeywords);
    }
    
    private double cosineSimilarity(double[] v1, double[] v2) {
        double dotProduct = 0, norm1 = 0, norm2 = 0;
        for (int i = 0; i < v1.length; i++) {
            dotProduct += v1[i] * v2[i];
            norm1 += v1[i] * v1[i];
            norm2 += v2[i] * v2[i];
        }
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2) + 1e-10);
    }
}
```

#### 2.3 匹配结果VO
```java
@Data
public class JobMatchResult {
    private Double matchScore;           // 匹配度 0-100
    private String matchLevel;           // 匹配等级
    private List<String> matchedKeywords;   // 已匹配关键词
    private List<String> missingKeywords;   // 缺失关键词
    private List<String> suggestions;       // 优化建议
    
    public String getMatchLevel() {
        if (matchScore >= 80) return "高度匹配";
        if (matchScore >= 60) return "较为匹配";
        if (matchScore >= 40) return "部分匹配";
        return "匹配度低";
    }
}
```

#### 2.4 API接口
```java
@Operation(summary = "简历-岗位匹配度分析")
@PostMapping("/match")
public BaseResult<JobMatchResult> matchJob(@RequestBody JobMatchForm form) {
    // form包含: resumeId, jobDescription 或 employmentInformationId
    return BaseResult.ok(jobMatchService.match(form));
}
```

---

### 模块3：简历关键词提取

#### 3.1 功能描述
自动从简历中提取技能标签和关键词。

#### 3.2 实现方案
使用 **HanLP + TextRank** 算法：

```java
@Service
public class KeywordExtractServiceImpl implements KeywordExtractService {
    
    @Autowired
    private TextExtractor textExtractor;
    
    @Override
    public KeywordResult extract(String reactCode, int topN) {
        // 1. 提取文本
        String text = textExtractor.extractText(reactCode);
        
        // 2. TextRank提取关键词
        List<String> keywords = HanLP.extractKeyword(text, topN);
        
        // 3. 提取技能标签（匹配预定义技能库）
        List<String> skills = extractSkills(text);
        
        // 4. 提取教育信息
        List<String> education = extractEducation(text);
        
        return new KeywordResult(keywords, skills, education);
    }
    
    // 技能提取（匹配预定义库）
    private List<String> extractSkills(String text) {
        List<String> skillLibrary = Arrays.asList(
            // 编程语言
            "Java", "Python", "JavaScript", "TypeScript", "Go", "C++", "C#", "PHP", "Ruby", "Swift", "Kotlin",
            // 前端
            "React", "Vue", "Angular", "HTML", "CSS", "Webpack", "Node.js", "jQuery",
            // 后端
            "Spring", "Spring Boot", "MyBatis", "Hibernate", "Django", "Flask", "Express",
            // 数据库
            "MySQL", "PostgreSQL", "MongoDB", "Redis", "Oracle", "SQL Server", "Elasticsearch",
            // 大数据
            "Hadoop", "Spark", "Flink", "Kafka", "Hive", "HBase",
            // 云与DevOps
            "Docker", "Kubernetes", "AWS", "Azure", "阿里云", "Linux", "Git", "Jenkins", "CI/CD",
            // AI/ML
            "机器学习", "深度学习", "TensorFlow", "PyTorch", "NLP", "计算机视觉"
        );
        
        List<String> found = new ArrayList<>();
        String lowerText = text.toLowerCase();
        for (String skill : skillLibrary) {
            if (lowerText.contains(skill.toLowerCase())) {
                found.add(skill);
            }
        }
        return found;
    }
}
```

#### 3.3 关键词结果VO
```java
@Data
public class KeywordResult {
    private List<String> keywords;      // TextRank提取的关键词
    private List<String> skills;        // 技能标签
    private List<String> education;     // 教育信息
    private Integer skillCount;         // 技能数量
}
```

#### 3.4 API接口
```java
@Operation(summary = "简历关键词提取")
@GetMapping("/keywords/{resumeId}")
public BaseResult<KeywordResult> extractKeywords(
    @PathVariable Integer resumeId,
    @RequestParam(defaultValue = "10") Integer topN
) {
    UserSaveResume resume = resumeService.getById(resumeId);
    return BaseResult.ok(keywordExtractService.extract(
        resume.getUserSaveResumeResumeReactCode(), topN
    ));
}
```

---

## 四、数据库扩展

### 4.1 行业关键词表
```sql
CREATE TABLE `ml_industry_keyword` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `industry_id` INT NOT NULL COMMENT '行业ID，关联IndustryMap',
    `keyword` VARCHAR(50) NOT NULL COMMENT '关键词',
    `weight` DOUBLE DEFAULT 1.0 COMMENT '权重',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_industry` (`industry_id`)
) COMMENT='行业关键词库';
```

### 4.2 评分记录表（可选）
```sql
CREATE TABLE `ml_resume_score_log` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `resume_id` INT NOT NULL,
    `user_id` INT NOT NULL,
    `total_score` DOUBLE,
    `completeness_score` DOUBLE,
    `keyword_score` DOUBLE,
    `length_score` DOUBLE,
    `structure_score` DOUBLE,
    `quantify_score` DOUBLE,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_resume` (`resume_id`),
    INDEX `idx_user` (`user_id`)
) COMMENT='简历评分记录';
```

---

## 五、前端对接

### 5.1 用户端页面
在简历详情页添加「智能评分」按钮：

```tsx
// 评分结果展示
interface ScoreResult {
  totalScore: number;
  level: string;
  suggestions: string[];
}

const ResumeScore: React.FC<{ resumeId: number }> = ({ resumeId }) => {
  const [result, setResult] = useState<ScoreResult | null>(null);
  
  const handleScore = async () => {
    const res = await api.get(`/user/resume/ml/score/${resumeId}`);
    setResult(res.data);
  };
  
  return (
    <div>
      <Button onClick={handleScore}>智能评分</Button>
      {result && (
        <div className="score-card">
          <div className="score-number">{result.totalScore.toFixed(1)}分</div>
          <div className="score-level">{result.level}</div>
          <div className="suggestions">
            {result.suggestions.map((s, i) => <p key={i}>{s}</p>)}
          </div>
        </div>
      )}
    </div>
  );
};
```

---

## 六、实施步骤

| 阶段 | 任务 | 预计耗时 |
|------|------|---------|
| 1 | 添加Maven依赖，验证环境 | 0.5天 |
| 2 | 实现TextExtractor文本提取 | 0.5天 |
| 3 | 实现模块1：简历评分 | 2天 |
| 4 | 实现模块2：岗位匹配 | 1.5天 |
| 5 | 实现模块3：关键词提取 | 1天 |
| 6 | 前端对接 | 1天 |
| 7 | 测试优化 | 1天 |
| **总计** | | **7.5天** |

---

## 七、后续扩展

1. **模型训练**：收集用户评分反馈，训练回归模型优化评分
2. **推荐系统**：基于关键词和行业，推荐相似简历模板
3. **投递预测**：预测简历投递成功率
4. **A/B测试**：对比不同评分算法效果

---

## 八、注意事项

1. **性能优化**：首次加载HanLP词典较慢，建议应用启动时预热
2. **关键词库维护**：定期更新行业关键词库
3. **评分校准**：根据用户反馈调整各维度权重
4. **隐私保护**：评分日志脱敏处理
