defmodule Day01 do
  @moduledoc """
  Solutions for Day 01 of Advent of Code 2022
  """

  @doc """
  Silver solution for Day 01
  """
  def silver do
    {output} = read_input()
    Enum.map(output, fn x -> convert_input(x) end)
  end

  @spec read_input() :: [String.t()]
  defp read_input do
    {
      "input/day_01.txt"
      |> File.read!()
      |> String.split(~r{\n\n})
      |> Enum.map(&String.trim/1)
    }
  end

  @spec convert_input(String.t()) :: [integer]
  defp convert_input(i) do
    {
      i
      |> String.split(~r{\n})
      |> Enum.map(&String.to_integer/1)
    }
  end

  @spec sum(list(number)) :: number
  defp sum(list) do
    Enum.sum(list)
  end
end
