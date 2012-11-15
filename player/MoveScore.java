/*
 *      untitled.java
 *      
 *      Copyright 2012 Nicholas K Isaacs <niis0081@csel-46>
 *      
 *      This program is free software; you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation; either version 2 of the License, or
 *      (at your option) any later version.
 *      
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *      
 *      You should have received a copy of the GNU General Public License
 *      along with this program; if not, write to the Free Software
 *      Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 *      MA 02110-1301, USA.
 */


public class MoveScore extends Move {
	
	int score;
	
	public MoveScore(int x, int y, int score){
		super(x, y);
		this.score = score;
	}		
	
	public MoveScore(int x, int y, int x2, int y2, int score){
		super(x, y, x2, y2);
		this.score = score;
	}
}
