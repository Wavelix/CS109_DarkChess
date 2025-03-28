# Dark Chess 象棋暗棋

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://java.com)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Status](https://img.shields.io/badge/status-archived-red.svg)]()

This project was developed for the 2022 CS109 "Introduction to Computer Programming" course.  
本项目为2022年CS109"计算机程序设计基础"课程开发，现已停止维护。

***

## Overview 项目概述

Dark Chess (Banqi) is a variant of traditional Chinese chess played on a 8×4 grid.  
象棋暗棋(翻棋)是中国象棋的变种，使用8×4棋盘。

This Java implementation features two-player gameplay with classic rules and graphical interface.  
该Java实现包含经典规则的双人对战和图形界面。

## Features 功能特点

### Core Gameplay 核心玩法
- Random piece initialization (face-down)  
  随机棋子初始化(背面朝上)
- Complete movement/capture mechanics  
  完整的移动/吃子机制
- Cannon special rules implementation  
  炮的特殊规则实现
- Point-based victory condition (60 points)  
  积分胜利条件(60分)

### Program Features 程序功能
- Java Swing GUI  
  Java Swing图形界面
- Game state saving/loading  
  游戏状态保存/读取
- Turn tracking and score display  
  回合追踪和分数显示

## Rules 游戏规则

### Piece Hierarchy 棋子等级
General (30) > Advisor (10) > Minister (5) = Chariot (5) = Horse (5) > Soldier (1)  
将(30) > 士(10) > 象(5) = 车(5) = 马(5) > 卒(1)

### Special Rules 特殊规则
- **Cannon** must jump over exactly one piece to capture  
  **炮**必须隔一个棋子才能吃子
- **Soldier** is the only piece that can capture General  
  **卒**是唯一能吃将的棋子
- **General** can only be captured by Soldier  
  **将**只能被卒吃掉

## Development 开发说明

This was a course project with the following implementation requirements:  
本项目为课程作业，包含以下开发要求：

| Task 任务 | Points 分数 | Description 描述 |
|-----------|------------|------------------|
| Game Initialization 游戏初始化 | 10 | Board setup and piece shuffling 棋盘设置与棋子洗牌 |
| Save/Load System 存档系统 | 20 | Text-based game state persistence 基于文本的游戏状态保存 |
| Game Logic 游戏逻辑 | 40 | Movement validation and win detection 移动验证与胜负判定 |
| GUI Implementation 图形界面 | 10 | Java Swing interface Java Swing实现 |