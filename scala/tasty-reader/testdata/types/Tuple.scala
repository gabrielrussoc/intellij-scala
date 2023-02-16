package types

trait Tuple {
  type HKT[F[_, _]]

  type T0 = HKT[Tuple2]

  type T1 = Tuple1[Int]

  type T2 = (Int, Long)

  type T3 = (Int, Long, Unit)

  type T4 = scala.Tuple.Size[(Int, Long)]

  def repeated(xs: (Int, Long)*): Unit
}