{-# OPTIONS_GHC -Wno-overlapping-patterns #-}
module Crypto ( gcd, smallestCoPrimeOf, phi, computeCoeffs, inverse
              , modPow, genKeys, rsaEncrypt, rsaDecrypt, toInt, toChar
              , add, subtract, ecbEncrypt, ecbDecrypt
              , cbcEncrypt, cbcDecrypt ) where

import Data.Char

import Prelude hiding (gcd, subtract)
import Text.Read (Lexeme(Char))

{-
The advantage of symmetric encryption schemes like AES is that they are efficient
and we can encrypt data of arbitrary size. The problem is how to share the key.
The flaw of the RSA is that it is slow and we can only encrypt data of size lower
than the RSA modulus n, usually around 1024 bits (64 bits for this exercise!).

We usually encrypt messages with a private encryption scheme like AES-256 with
a symmetric key k. The key k of fixed size 256 bits for example is then exchanged
via the aymmetric RSA.
-}

-------------------------------------------------------------------------------
-- PART 1 : asymmetric encryption

-- | Returns the greatest common divisor of its two arguments
gcd :: Int -> Int -> Int
gcd m n
    | n == 0    = m
    | otherwise = gcd n (mod m n)

-- | Euler Totient function
phi :: Int -> Int
phi m = length [i | i <- [1..m], gcd m i == 1] -- list comprehension

{-|
Calculates (u, v, d) the gcd (d) and Bezout coefficients (u and v)
such that au + bv = d
-}
computeCoeffs :: Int -> Int -> (Int, Int)
computeCoeffs a 0 = (1, 0)
computeCoeffs a b = (v, u - q*v)
    where
    (q, r) = quotRem a b
    (u, v) = computeCoeffs b r

-- | Inverse of a modulo m
inverse :: Int -> Int -> Int
inverse a m
    | gcd a m /= 1 = undefined
    | otherwise = u `mod` m
    where u = fst (computeCoeffs a m)

-- | Calculates (a^k mod m)
modPow :: Int -> Int -> Int -> Int
modPow a 0 m = 1 `mod` m -- base case : a^0 mod m = 1
-- modPow 0 k m = 0         -- base case : 0^k mod m = 0 // unnecessary
modPow a k m
    | even k    = x
    | otherwise = (a * x) `mod` m
    where
    j = k `div` 2
    x = modPow ((a^2) `mod` m) j m

-- | Returns the smallest integer that is coprime with phi
smallestCoPrimeOf :: Int -> Int
smallestCoPrimeOf a = findCoPrime 2
    where
    findCoPrime :: Int -> Int
    -- takes a number x
    -- returns the smallest Co-prime of x and a, by checking the candidate numbers from x
    findCoPrime x
        | gcd a x == 1 = x
        | otherwise    = findCoPrime (x + 1)


{-|
Generates keys pairs (public, private) = ((e, n), (d, n))
given two "large" distinct primes, p and q
-}
genKeys :: Int -> Int -> ((Int, Int), (Int, Int))
genKeys p q = ((e, n), (d, n))
    where
    d = inverse e x
    x = (p - 1)*(q - 1)
    n = p * q
    e = smallestCoPrimeOf x


-- | This function performs RSA encryption
rsaEncrypt :: Int        -- ^ value to encrypt
           -> (Int, Int) -- ^ public key
           -> Int
rsaEncrypt x (e, n) = modPow x e n

-- | This function performs RSA decryption
rsaDecrypt :: Int        -- ^ value to decrypt
           -> (Int, Int) -- ^ public key
           -> Int
rsaDecrypt c (d, n) = modPow c d n
-- or u can just say
-- rsaDecrypt = rsaEncrypt

-------------------------------------------------------------------------------
-- PART 2 : symmetric encryption

-- | Returns position of a letter in the alphabet
toInt :: Char -> Int
toInt x
    | isAsciiLower x = ord x - ord 'a'
    | isAsciiUpper x = ord x - ord 'A'
    | otherwise = undefined

-- | Returns the n^th letter
toChar :: Int -> Char
toChar n = chr (n + ord 'a')

maxLetterintex :: Int
maxLetterintex = 26

-- | "adds" two letters
add :: Char -> Char -> Char
add c1 c2 = toChar ((toInt c1 + toInt c2) `mod` maxLetterintex)

-- | "subtracts" two letters
subtract :: Char -> Char -> Char
subtract c1 c2
    | result < 0 = toChar (result + maxLetterintex)
    | otherwise = toChar result
    where result = toInt c1 - toInt c2

-- the next functions present
-- 2 modes of operation for block ciphers : ECB and CBC
-- based on a symmetric encryption function e/d such as "add"

-- | ecb (electronic codebook) encryption with block size of a letter
ecbEncrypt :: Char -> [Char] -> [Char]
-- ecbEncrypt k = map (add k)
ecbEncrypt _ [] = []
ecbEncrypt k (m : ms) = add m k : ecbEncrypt k ms


-- | ecb (electronic codebook) decryption with a block size of a letter
ecbDecrypt :: Char -> [Char] -> [Char]
ecbDecrypt k = map subK
    where
        subK :: Char -> Char
        subK c1 = subtract c1 k

-- | cbc (cipherblock chaining) encryption with block size of a letter
cbcEncrypt :: Char   -- ^ public key
           -> Char   -- ^ initialisation vector `iv`
           -> [Char] -- ^ message `m`
           -> [Char]
-- cbcEncrypt k iv m = loopC (l - 1)
cbcEncrypt k iv "" = [] -- base case when the message is empty
-- Recursive case: When there's a message, calculate the next ciphertext block 'c' 
-- and continue with the rest of the message.
cbcEncrypt k iv m  = c : cbcEncrypt k c (tail m)
    where
        -- 1st version
        -- l = length m
        -- addK :: Char -> Char
        -- addK y = add y k
        -- loopC :: Int -> [Char]
        -- loopC 0 = [addK (add (head m) iv)]   -- creating c1 (basecase)
        -- loopC i = loopC (i-1) ++ [addK (add (m!!i) (loopC (i-1) !! (i-1)))]
        c = head (ecbEncrypt (add k iv) m)
        -- Calculate the next ciphertext block 'c' by applying ECB encryption with a modified IV.
-- Or, using higher-order function,
cbcEncrypt key _iv [] = []
cbcEncrypt key iv (m : ms) = c : cbcEncrypt key c ms
    where c = add key (add m iv)


-- | cbc (cipherblock chaining) decryption with block size of a letter
cbcDecrypt :: Char   -- ^ private key
           -> Char   -- ^ initialisation vector `iv`
           -> [Char] -- ^ message `m`
           -> [Char]
cbcDecrypt k iv "" = [] -- base case
cbcDecrypt k iv c = x : cbcDecrypt k (head c) (tail c)
    where
        x = subtract (head (ecbDecrypt k c)) iv

isPrime :: Int -> Bool
isPrime 1 = False
isPrime n = null [x | x <- 2 : [3, 5 .. floor(sqrt (fromIntegral n))], mod n x == 0]


-- kmk :: Int -> Bool
-- -- kmk 1 = False
-- -- kmk n
--     | n <= 1    = False -- 0 and 1 are not prime
--     | otherwise = all (\x -> n `mod` x /= 0) [2..intSqrt n]
--     where
--         intSqrt = floor . sqrt . fromIntegral


