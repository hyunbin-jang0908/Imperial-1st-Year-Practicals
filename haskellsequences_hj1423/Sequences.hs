module Sequences where

import Data.Char (ord, chr)

-- | Returns the first argument if it is larger than the second,
-- the second argument otherwise
maxOf2 :: Int -> Int -> Int
maxOf2 x y
    | x > y     = x
    | otherwise = y

-- | Returns the largest of three Ints
maxOf3 :: Int -> Int -> Int -> Int
maxOf3 x y z =
    if x >= y then
        if x >= z then x
        else z
    else if y >= z then y
        else z

-- | Returns True if the character represents a digit '0'..'9';
-- False otherwise
isADigit :: Char -> Bool
isADigit x = (x <= '9' && x >= '0')

-- | Returns True if the character represents an alphabetic
-- character either in the range 'a'..'z' or in the range 'A'..'Z';
-- False otherwise
isAlpha :: Char -> Bool
isAlpha x = (x >= 'a' && x <= 'z') || (x >= 'A' && x <= 'Z')


-- | Returns the integer [0..9] corresponding to the given character.
-- Note: this is a simpler version of digitToInt in module Data.Char,
-- which does not assume the precondition.
digitToInt :: Char -> Int
-- Pre: the character is one of '0'..'9'
digitToInt x = ord x - ord '0'
    

-- | Returns the upper case character corresponding to the input.
-- Uses guards by way of variety.
toUpper :: Char -> Char
toUpper x
    -- normally so good
    | x >= 'a' && x <= 'z' = chr(ord x - ord 'a' + ord 'A')
    | x >= 'A' && x <= 'Z' = x
    | otherwise = undefined

--
-- Sequences and series
--

-- | Arithmetic sequence
arithmeticSeq :: Double -> Double -> Int -> Double
arithmeticSeq a d n = a + fromIntegral n * d

-- | Geometric sequence
geometricSeq :: Double -> Double -> Int -> Double
geometricSeq a r 1 = a * r
-- geometricSeq a r n = r * geometricSeq a r (n-1)
geometricSeq a r n = a * r ^ n

-- | Arithmetic series
arithmeticSeries :: Double -> Double -> Int -> Double
arithmeticSeries a d n = (fromIntegral n + 1) * (a + d * fromIntegral n/2)

-- | Geometric series
geometricSeries :: Double -> Double -> Int -> Double
geometricSeries a r 0 = a
geometricSeries a r n = geometricSeries a r (n-1) + a*(r**(fromIntegral n))


