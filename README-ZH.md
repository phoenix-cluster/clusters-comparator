# 简介
**Cluster Comparer GUI**是一款专注于蛋白质组学质谱数据分析的软件，其主要功能有：

1. 对谱图进行相似性打分（目前支持的文件格式包含MGF文件格式和`*.clustering`文件格式）

2. 对谱图聚类结果进行基本的分析和可视化（目前支持`*.clustering`文件格式）

> 本软件采用的相似性打分算法能够在这篇文章中（[A Pipeline for Differential Proteomics in Unsequenced Species](https://pubs.acs.org/doi/abs/10.1021/acs.jproteome.6b00140)）找到

> `*.clustering`文件格式用于存储MS/MS谱图聚类的结果，关于`*.clustering`文件格式详细介绍可以参考[https://github.com/spectra-cluster/clustering-file-reader](https://github.com/spectra-cluster/clustering-file-reader)

# `*.clustering`文件的分析和可视化

## 1. 导入数据

菜单栏中以此点选`File` > `Import Data`，在弹窗中选择`Get Data From Clustering File`，然后选择并提交需要进行分析的`*.clustering`文件。因为对比需要两个对象，所以需要输入两个文件。

![./image/clustering-import](image/clustering-import.png)

## 2. 查看结果

### cluster table和spectrum table

**cluster table**和**spectrum table**以表格的形式显示了`*.clustering`的内容，**cluster table**中的每一行显示一个簇，即一个`cluster`，**spectrum table**显示了当前选择的这个`cluster`中包含的谱图的信息。

![image/cluster-table](image/cluster-table.png)

支持操作：
- 点击**switch**按钮显示另一个`*.clustering`的内容
- 单击**cluster table**中不同的cluster时，**spectrum table**、**peak map**、**pie chart**、**network graph**会显示对应cluster的分析结果
- 通过**page controller**改变每页显示结果的数目，也可以跳转到指定的页码

### peak map

**peak map**显示了**cluster table**中指定cluster的所有峰

![image/peak-map](image/peak-map.gif)

支持操作：
- 通过颜色选择器调节颜色
- 放大显示矩形选择区域内的峰（鼠标左键拖拽选择）

### pie chart

**pie chart**显示了**cluster table**中指定cluster中包含的谱图在另一个`.clustering`文件中的分布情况。

![image/pie-chart](image/pie-chart.png)

举个例子来说明饼图的含义：

- File I: sample1.clustering

```
=Cluster=
id=cluster1_1
av_precursor_mz=348.128
av_precursor_intens=1.0
consensus_mz=227.292,288.314,...
consensus_intens=29.8,693.84,191.04,...
SPEC	spec1			348.12817	3.0			0.0
SPEC	spec2			348.12817	3.0			0.0
SPEC	spec3			348.12817	3.0			0.0
SPEC	spec4			348.12817	3.0			0.0

=Cluster=
id=cluster1_2
av_precursor_mz=348.128
av_precursor_intens=1.0
consensus_mz=227.292,288.314,...
consensus_intens=29.8,693.84,...
SPEC	spec5			348.12817	3.0			0.0
SPEC	spec7			348.12817	3.0			0.0
SPEC	spec8			348.12817	3.0			0.0
SPEC	spec9			348.12817	3.0			0.0
SPEC	spec10			348.12817	3.0			0.0
```

- File II: sample2.Clustering

```
=Cluster=
id=cluster2_1
av_precursor_mz=348.128
av_precursor_intens=1.0
consensus_mz=227.292,288.314,...
consensus_intens=29.8,693.84,...
SPEC	spec4			348.12817	3.0			0.0
SPEC	spec5			348.12817	3.0			0.0
SPEC	spec6			348.12817	3.0			0.0
SPEC	spec7			348.12817	3.0			0.0
SPEC	spec8			348.12817	3.0			0.0
SPEC	spec9			348.12817	3.0			0.0

=Cluster=
id=cluster2_2
av_precursor_mz=348.128
av_precursor_intens=1.0
consensus_mz=227.292,288.314,...
consensus_intens=29.8,693.84,...
SPEC	spec1			348.12817	3.0			0.0
SPEC	spec2			348.12817	3.0			0.0
SPEC	spec3			348.12817	3.0			0.0

=Cluster=
id=cluster2_3
av_precursor_mz=348.128
av_precursor_intens=1.0
consensus_mz=227.292,288.314,...
consensus_intens=29.8,693.84,191.04,...
SPEC	spec10			348.12817	3.0			0.0
SPEC	spec11			348.12817	3.0			0.0
```
在`sample1.clustering`中的cluster1_1中包含谱图{spec1, spec2, spec3, spec4}，这些谱图在`sample2.clustering`中分散在cluster2_1{spec4}和cluster2_2{spec1, spec2, spec3}中，这就形成了上面的饼图。

支持操作：
- 单击饼图的分块，将显示两个cluster的比较。第一个cluster是当前指定的cluster，第二个cluster来自另一个`*.clustering`文件。

  - 两个cluster重叠的spectrum会高亮显示
  - **peak map**显示了两个cluster峰的对比情况，可以使用之前介绍的操作调节它的显示

![image/cluster-comparison](image/cluster-comparison.png)

- Do Similarity Score

  单击**Do Similarity Score**按钮将会在指定的cluster和饼图中显示的所有cluster之间进行相似性打分（上面的例子应该是针对cluster1_1 vs cluster2_1和cluster2_2进行相似性打分）。

  - ID：同**cluster table**的**ID**
  - Match Rate：在考虑容错率的条件下，两个谱图匹配的峰比例（匹配峰的数目 / 处理后两谱图峰数目中的最大值）
  - Similarity Score：相似性得分，**相似性得分越高越相似**。
  - Detail：点击**Show**按钮显示每次迭代结果的报告

    - 筛选掉的数据具有一定的透明度，实际用于计算的数据不透明

  - 点击**View Visualization Result**按钮将会给出比对的可视化结果——两两比对中最大相似性得分的报告

![  image/similarity-score-clustering](image/similarity-score-clustering.png)

### network graph

![image/network-graph](image/network-graph.png)

**network graph**显示了以**cluster table**中指定的cluster为起点，搜索其相关的spectrum的分布。这和饼图的搜索有所不同，具体我们举一个例子说明**network graph**的含义（依旧使用上面给出的sample1.clustering和sample2.clustering）：

![network-graph-example](network-graph-example.png)

cluster1_1中的谱图分散在cluster2_1和clustering2_2中，但是cluster2_1中还包含了不在cluster1_1中的谱图{spec5, spec7, spec8, spec9}这部分谱图被聚到了cluster1_2中，而cluster1_2中的spec10是在cluster2_3中，在**network graph**中为了表示这种关系，使用节点存储cluster，使用边连接存在重叠谱图的节点，节点的垂直宽度代表了该类簇包含的谱图数量，边的垂直宽度代表了两类簇共同拥有的谱图的数量。

支持操作：
- 点击节点，显示cluster信息
- 点击边，显示两个cluster的比对情况
- 使用调节器调节**network graph**的显示



# MGF文件的分析和可视化

## 1. 读取数据

菜单栏中以此点选`File` > `Import Data`，在弹窗中选择`Get Data From MGF File`，然后选择需要进行分析的MGF文件，因为对比需要两个对象，所以需要输入两个文件。

![./image/mgf-import](image/mgf-import.png)

## 2. 查看结果

### MGF table

**MGF table**显示MGF文件的内容

![image/mgf-table](image/mgf-table.png)

支持操作：
- 点击表格不同的谱图，**Similarity Score Table**、**Peak Map**会随之变化

## Similarity Score table

**Similarity Score table**显示当前谱图与另外一个文件中所有的谱图的相似性打分

![image/similarity-score-table](image/similarity-score-table.png)

- ID：同**MGF table**的Title

> 其他的属性同**Do Similarity Score**介绍的内容


# 下一个小版本

- [ ] 修改相关代码的组织形式，解耦和
- [ ] 修改GUI视图
