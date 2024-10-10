{-# LANGUAGE BlockArguments #-}
module LSystems ( LSystem(LSystem), ColouredLine, Command(..)
                , angle, axiom, rules, lookupChar
                , expandOne, expand, move, trace1, trace2
                , expandLSystem, commandMap ) where

import IC.Colour

type Rules a = [(Char, [a])]
data LSystem = LSystem Float [Char] (Rules Char)
type Vertex = (Float, Float)
type TurtleState = (Vertex, Float)
data Command = F | L | R | B [Command]
type ColouredLine = (Vertex, Vertex, Colour)

----------------------------------------------------------
-- Functions for working with systems.

-- Returns the rotation angle for the given system.
angle :: LSystem -> Float
angle (LSystem t _ _) = t

-- Returns the axiom string for the given system.
axiom :: LSystem -> [Char]
axiom (LSystem _ str _) = str

-- Returns the set of rules for the given system.
rules :: LSystem -> Rules Char
rules (LSystem _ _ rs) = rs

--
-- Pre: the character has a binding in the Rules list
--
lookupChar :: Rules a -> Char -> [a]
lookupChar rs x = concat ([s | (c, s) <- rs, c == x])

--
-- Expand command string s once using rule table r
--
expandOne :: Rules Char -> [Char] -> [Char]
expandOne rule = concatMap (lookupChar rule)

--
-- Expand command string s n times using rule table r
--
expand :: [Char] -> Int -> Rules Char -> [Char]
expand cs 0 rule = cs
expand cs n rule = expand (expandOne rule cs) (n-1) rule

-- Move a turtle.
--
-- F moves distance 1 in the current direction.
-- L rotates left according to the given angle.
-- R rotates right according to the given angle.
move :: Command -> Float -> TurtleState -> TurtleState
move cmd value ((x, y), currentAngle)
    case cmd of
        L -> ((x, y), mod (currentAngle + value) 360)
        R -> ((x, y), mod (currentAngle - value) 360)
        F -> ((x + changeX, y + changeY), currentAngle)
    where 
    changeX = a * cos (currentAngle / 180 * Pi)
    changeY = y * sin (currentAngle / 180 * Pi)

parse :: Rules Command -> [Char] -> [Command]
parse = undefined

trace1 :: [Command] -> Float -> Colour -> [ColouredLine]
trace1 = undefined

-- This version uses an explicit stack of residual commands and turtle states
trace2 :: [Command] -> Float -> Colour -> [ColouredLine]
trace2 = undefined

-- Provided Functions
------------------------------------------------------------------------------

expandLSystem :: LSystem -> Int -> [Command]
expandLSystem (LSystem _ axiom rs) n = parse commandMap (expand axiom n rs)

commandMap :: Rules Command
commandMap = [ ('M', [F])
             , ('N', [F])
             , ('X', [])
             , ('Y', [])
             , ('A', [])
             , ('+', [L])
             , ('-', [R])
             ]

-- ghcup set ghc 9.8