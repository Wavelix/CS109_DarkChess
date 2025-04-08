# Dark Chess 象棋暗棋

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://java.com)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Status](https://img.shields.io/badge/2022-Autumn-red.svg)]()
[![Status](https://img.shields.io/badge/status-archived-red.svg)]()

This project was developed for the 2022 CS109 "Introduction to Computer Programming" course.  
本项目为2022年CS109"计算机程序设计基础"课程开发，现已停止维护。

***

## Overview

Dark Chess (Banqi) is a variant of traditional Chinese chess played on a 8×4 grid.  This Java implementation features two-player gameplay with classic rules and graphical interface.  

<img src=".\darkchess.png" style="zoom:40%;" />

## Rules

### Piece Hierarchy
General (30) > Advisor (10) > Minister (5) = Chariot (5) = Horse (5) > Soldier (1)  
将(30) > 士(10) > 象(5) = 车(5) = 马(5) > 卒(1)

### Special Rules
- **Cannon** must jump over exactly one piece to capture  
  **炮**必须隔一个棋子才能吃子
- **Soldier** is the only piece that can capture General  
  **卒**是唯一能吃将的棋子

## Development

This was a course project with the following implementation requirements:  

| Task 任务 | Points 分数 | Description 描述 |
|-----------|------------|------------------|
| Game Initialization | 10 | Board setup and piece shuffling |
| Save/Load System | 20 | Text-based game state persistence |
| Game Logic | 40 | Movement validation and win detection |
| GUI Implementation | 10 | Java Swing interface |