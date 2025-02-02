-- A simple Haskell program to implement a text-based Tic Tac Toe game

module Main where

import Data.List (intercalate, transpose)
import Safe (atMay)
import Text.Read (readMaybe)

type Cell = String
type Row = [Cell]
type Board = [Row]
type Player = String
type RowIndex = Int
type ColumnIndex = Int
type Location = (RowIndex, ColumnIndex)
data TurnStatus = Fresh | Retrying

add :: Int -> Int -> Int
add a b = a + b

defaultBoard :: Board
defaultBoard = replicate 3 (replicate 3 " ")

printBoard :: Board -> IO ()
printBoard board = do
  putStrLn $ intercalate "\n-+-+-\n" (map (intercalate "|") board)

isValidMove :: Board -> RowIndex -> ColumnIndex -> Bool
isValidMove board rowIndex colIndex =
  let isInBounds = rowIndex >= 0 && rowIndex < 3 &&
                   colIndex >= 0 && colIndex < 3
      cell = case atMay board rowIndex of
        Just v -> atMay v colIndex
        Nothing -> Nothing
      isEmptySpace = case cell of
        Just v -> v == " "
        Nothing -> False
  in isInBounds && isEmptySpace

makeMove :: Board -> RowIndex -> ColumnIndex -> Player -> Board
makeMove board targetRowIdx targetColIdx movingPlayer =
  zipWith
    (\rowIdx curRow ->
        zipWith
          (\colIdx existingCell ->
              if targetRowIdx == rowIdx && targetColIdx == colIdx
                then movingPlayer
                else existingCell)
          [0 ..] curRow)
    [0 ..] board

checkRow :: Board -> Player -> Bool
checkRow board player =
  any (all (== player)) board

checkColumn :: Board -> Player -> Bool
checkColumn board player =
  any (all (== player)) (transpose board)

checkDiagonal :: Board -> Player -> Bool
checkDiagonal board player =
  let bound = length board - 1
  in
    all (== Just player)
    [ do row <- atMay board i
         atMay row i
    | i <- [0..bound] ]

checkAntiDiagonal :: Board -> Player -> Bool
checkAntiDiagonal board player =
  let bound = length board - 1
  in
    all (== Just player)
    [ do row <- atMay board i
         atMay row (2 - i)
    | i <- [0..bound] ]

checkWinner :: Board -> Player -> Bool
checkWinner board player
  = checkRow board player
  || checkColumn board player
  || checkDiagonal board player
  || checkAntiDiagonal board player

switchPlayer :: Player -> Player
switchPlayer player =
  if player == "X"
  then "O" else "X"

parseLocInput :: String -> Maybe Location
parseLocInput input =
  case mapM readMaybe (words input) of
    Just [row, col] -> Just (row, col)
    Just _ -> Nothing
    Nothing -> Nothing

printInstruction :: Board -> Player -> TurnStatus -> IO ()
printInstruction board currentPlayer turnStatus =
  case turnStatus of
    Fresh -> do
      printBoard board
      let msg = "Player " ++ currentPlayer
            ++ ", enter your move "
            ++ "(row and column separated by a space):"
      putStrLn msg
    Retrying -> do
      let msg = "Invalid move. Player " ++ currentPlayer
            ++ ", Try again"
      putStrLn msg

playGame :: Board -> Player -> TurnStatus -> IO ()
playGame board currentPlayer turnStatus = do
  printInstruction board currentPlayer turnStatus
  input <- getLine
  case parseLocInput input of
    Nothing -> playGame board currentPlayer Retrying
    Just (rowIndex, columnIndex) ->
      if isValidMove board rowIndex columnIndex
      then do
        let newBoard = makeMove board rowIndex columnIndex currentPlayer
        if checkWinner newBoard currentPlayer
          then do
          printBoard newBoard
          putStrLn $ "Player " ++ currentPlayer ++ " wins!"
          else playGame newBoard (switchPlayer currentPlayer) Fresh
      else do playGame board currentPlayer Retrying

main :: IO ()
main = do
  putStrLn "Welcome to Tic Tac Toe!"
  playGame defaultBoard "X" Fresh
