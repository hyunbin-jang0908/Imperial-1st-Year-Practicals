module Tests where

import IC.TestSuite

import Sequences

maxOf2Tests = [ maxOf2 1 2 --> 2
              , maxOf2 2 1 --> 2
              , maxOf2 2 2 --> 2
              , maxOf2 9 7 --> 9
              ]

maxOf3Tests = [ maxOf3 1 2 3 --> 3
              , maxOf3 2 1 3 --> 3
              , maxOf3 3 3 3 --> 3
              , maxOf3 1 1 2 --> 2
              , maxOf3 1 2 1 --> 2
              , maxOf3 2 1 1 --> 2
              , maxOf3 3 2 1 --> 3
              ]

isADigitTests = [ isADigit '1' --> True
                , isADigit 'A' --> False
                ]

isAlphaTests = [ isAlpha '1' --> False
               , isAlpha 'A' --> True
               ]

digitToIntTests = [ digitToInt '0' --> 0
                  , digitToInt '9' --> 9
                  ]

toUpperTests = [ toUpper 'a' --> 'A'
               , toUpper 'A' --> 'A'
               ]

--
-- Sequences and series
--

arithmeticSeqTests = [ arithmeticSeq 0.0 10.0 0 --> 0.0
                     , arithmeticSeq 10.0 10.0 0 --> 10.0
                     , arithmeticSeq 0.0 10.0 10 --> 100.0
                     , arithmeticSeq 10.0 0.0 10 --> 10.0
                     ]

geometricSeqTests = [ geometricSeq 0.0 10.0 0 --> 0.0
                    , geometricSeq 10.0 10.0 0 --> 10.0
                    , geometricSeq 0.0 10.0 10 --> 0.0
                    , geometricSeq 10.0 0.0 10 --> 0.0
                    ]

arithmeticSeriesTests = [ arithmeticSeries 0.0 10.0 0 --> 0.0
                        , arithmeticSeries 10.0 10.0 0 --> 10.0
                        , arithmeticSeries 0.0 10.0 10 --> 550.0
                        , arithmeticSeries 10.0 0.0 10 --> 110.0
                        ]

geometricSeriesTests = [ geometricSeries 0.0 10.0 0 --> 0.0
                       , geometricSeries 10.0 10.0 0 --> 10.0
                       , geometricSeries 0.0 10.0 10 --> 0.0
                       , geometricSeries 10.0 0.0 10 --> 10.0
                       ]

-- You can add your own test cases above

tests :: [TestGroup]
tests =
  [ testGroup "maxOf2" maxOf2Tests
  , testGroup "maxOf3" maxOf3Tests
  , testGroup "isADigit" isADigitTests
  , testGroup "isAlpha" isAlphaTests
  , testGroup "digitToInt"digitToIntTests
  , testGroup "toUpper" toUpperTests
  , testGroup "arithmeticSeq"arithmeticSeqTests
  , testGroup "geometricSeq" geometricSeqTests
  , testGroup "arithmeticSeries" arithmeticSeriesTests
  , testGroup "geometricSeries" geometricSeriesTests
  ]

main :: IO ()
main = runTests tests
